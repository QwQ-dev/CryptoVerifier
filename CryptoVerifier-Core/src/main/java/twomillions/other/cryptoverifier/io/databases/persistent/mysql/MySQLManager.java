package twomillions.other.cryptoverifier.io.databases.persistent.mysql;

import de.leonhard.storage.Yaml;
import lombok.*;
import org.apache.commons.dbcp2.BasicDataSource;
import twomillions.other.cryptoverifier.io.databases.enums.status.AuthStatus;
import twomillions.other.cryptoverifier.io.databases.enums.status.ConnectStatus;
import twomillions.other.cryptoverifier.io.databases.enums.status.CustomUrlStatus;
import twomillions.other.cryptoverifier.io.databases.interfaces.DatabasesInterface;
import twomillions.other.cryptoverifier.util.LoggerUtils;
import twomillions.other.cryptoverifier.util.QuickUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MySQLManager implements DatabasesInterface {
    @Getter
    private static final MySQLManager mySQLManager = new MySQLManager();

    private volatile String ip;
    private volatile String port;
    private volatile String driver;
    private volatile String username;
    private volatile String password;
    private volatile String jdbcUrlString;
    private volatile AuthStatus authStatus;
    private volatile CustomUrlStatus customUrlStatus;
    private volatile BasicDataSource dataSource = new BasicDataSource();
    private volatile ConnectStatus connectStatus = ConnectStatus.TurnOff;

    @Override
    public ConnectStatus setup(Yaml yaml) {
        setIp(yaml.getString("MYSQL.IP"));
        setPort(yaml.getString("MYSQL.PORT"));
        setDriver(yaml.getString("MYSQL.DRIVER"));
        setUsername(yaml.getString("MYSQL.AUTH.USER"));
        setPassword(yaml.getString("MYSQL.AUTH.PASSWORD"));

        setCustomUrlStatus(yaml.getString("MYSQL.CUSTOM-URL").isEmpty() ? CustomUrlStatus.TurnOff : CustomUrlStatus.TurnOn);

        if (getCustomUrlStatus() == CustomUrlStatus.TurnOn) {
            setJdbcUrlString(yaml.getString("MYSQL.CUSTOM-URL"));
        } else {
            setJdbcUrlString("jdbc:mysql://" + getIp() + ":" + getPort() + "/CryptoVerifier");
        }

        try {
            getDataSource().setInitialSize(5);
            getDataSource().setUrl(getJdbcUrlString());
            getDataSource().setUsername(getUsername());
            getDataSource().setPassword(getPassword());
            getDataSource().setDriverClassName(getDriver());

            setConnectStatus(ConnectStatus.Connected);
            LoggerUtils.getLogger().info("已建立与 MySQL 的连接!");
        } catch (Exception exception) {
            setConnectStatus(ConnectStatus.CannotConnect);
            LoggerUtils.getLogger().error("您打开了 MySQL 数据库选项, 但未能正确连接到 MySQL, 请检查 MySQL 服务状态.");
        }

        return getConnectStatus();
    }

    @Override
    @SneakyThrows
    public Object getOrDefault(String uuid, String key, Object defaultValue, String databaseCollection) {
        @Cleanup Connection connection = getDataSource().getConnection();

        checkCollectionType(key, databaseCollection);
        checkColumn(key, databaseCollection);

        String query = "SELECT COALESCE((SELECT `" + key + "` FROM `" + databaseCollection + "` WHERE uuid = ?), ?)";

        @Cleanup PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, uuid);
        preparedStatement.setObject(2, defaultValue);

        @Cleanup ResultSet resultSet = preparedStatement.executeQuery();
        resultSet.next();
        Object value = resultSet.getObject(1);

        if (value == null) {
            if (defaultValue != null) {
                update(uuid, key, defaultValue, databaseCollection);
            }

            return defaultValue;
        }

        return value;
    }

    @Override
    @SneakyThrows
    public ConcurrentLinkedQueue<Object> getOrDefaultList(String uuid, String key, ConcurrentLinkedQueue<Object> defaultValue, String databaseCollection) {
        @Cleanup Connection connection = getDataSource().getConnection();

        checkCollectionType(key, databaseCollection);
        checkColumn(key, databaseCollection);

        String query = "SELECT COALESCE((SELECT `" + key + "` FROM `" + databaseCollection + "` WHERE uuid = ?), ?)";

        @Cleanup PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, uuid);
        preparedStatement.setString(2, defaultValue.toString());

        @Cleanup ResultSet resultSet = preparedStatement.executeQuery();
        resultSet.next();
        String value = resultSet.getString(1);

        if (value == null || value.isEmpty()) {
            if (!defaultValue.isEmpty()) {
                update(uuid, key, defaultValue, databaseCollection);
            }

            return defaultValue;
        }

        return QuickUtils.stringToList(value);
    }

    @Override
    public Map<Object, Object> getOrDefaultMap(String uuid, String key, Map<Object, Object> defaultValue, String databaseCollection) {
        return QuickUtils.stringToMap(getOrDefault(uuid, key, defaultValue, databaseCollection).toString());
    }

    @Override
    @SneakyThrows
    public void update(String uuid, String key, Object value, String databaseCollection) {
        value = value.toString();

        @Cleanup Connection connection = getDataSource().getConnection();

        checkCollectionType(key, databaseCollection);
        checkColumn(key, databaseCollection);

        String query = "INSERT INTO `" + databaseCollection + "` (uuid, `" + key + "`) VALUES (?, ?) ON DUPLICATE KEY UPDATE `" + key + "`=?";

        @Cleanup PreparedStatement preparedStatement = connection.prepareStatement(query);

        preparedStatement.setString(1, uuid);
        preparedStatement.setObject(2, value);
        preparedStatement.setObject(3, value);
        preparedStatement.executeUpdate();
    }

    @Override
    @SneakyThrows
    public List<String> getColumnNames(String column, String collectionName) {
        List<String> columnNames = new ArrayList<>();

        @Cleanup Connection connection = getDataSource().getConnection();
        @Cleanup ResultSet columns = connection.getMetaData().getColumns(null, null, collectionName, column);

        while (columns.next()) {
            String columnName = columns.getString("COLUMN_NAME");
            columnNames.add(columnName);
        }

        return columnNames;
    }

    @Override
    @SneakyThrows
    public void delete(String uuid, String databaseCollection) {
        @Cleanup Connection connection = getDataSource().getConnection();

        String query = "DELETE FROM `" + databaseCollection + "` WHERE uuid = ?";

        @Cleanup PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, uuid);
        preparedStatement.executeUpdate();
    }

    @SneakyThrows
    private void checkCollectionType(String key, String databaseCollection) {
        @Cleanup Connection connection = getDataSource().getConnection();
        @Cleanup ResultSet tables = connection.getMetaData().getTables(null, null, databaseCollection, null);

        if (!tables.next()) {
            String createTableQuery = "CREATE TABLE `" + databaseCollection + "` (`uuid` VARCHAR(36) NOT NULL, `" + key + "` TEXT DEFAULT NULL, PRIMARY KEY (`uuid`))";
            @Cleanup PreparedStatement createTableStatement = connection.prepareStatement(createTableQuery);
            createTableStatement.executeUpdate();
        }
    }

    @SneakyThrows
    private void checkColumn(String key, String databaseCollection) {
        @Cleanup Connection connection = getDataSource().getConnection();
        @Cleanup ResultSet columns = connection.getMetaData().getColumns(null, null, databaseCollection, key);

        if (!columns.next()) {
            String alterTableQuery = "ALTER TABLE `" + databaseCollection + "` ADD `" + key + "` TEXT DEFAULT NULL";
            @Cleanup PreparedStatement alterTableStatement = connection.prepareStatement(alterTableQuery);
            alterTableStatement.executeUpdate();
        }
    }
}
