package twomillions.other.cryptoverifier.io.databases.persistent;

import de.leonhard.storage.Yaml;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import twomillions.other.cryptoverifier.io.databases.enums.status.ConnectStatus;
import twomillions.other.cryptoverifier.io.databases.enums.types.PersistentDataStorageType;
import twomillions.other.cryptoverifier.io.databases.interfaces.DatabasesInterface;
import twomillions.other.cryptoverifier.io.yaml.YamlManager;
import twomillions.other.cryptoverifier.io.databases.persistent.mongo.MongoManager;
import twomillions.other.cryptoverifier.io.databases.persistent.mysql.MySQLManager;
import twomillions.other.cryptoverifier.util.LoggerUtils;
import twomillions.other.cryptoverifier.util.common.ConstantsUtils;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * 该类实现 {@link DatabasesInterface}, 处理总数据操作。
 *
 * @author 2000000
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PersistentDatabasesManager implements DatabasesInterface {
    /**
     * PersistentDataStorageType.
     */
    @Getter
    @Setter
    private static PersistentDataStorageType persistentDataStorageType;

    /**
     * PersistentDatabasesManager.
     */
    @Getter
    @Setter
    private static PersistentDatabasesManager persistentDatabasesManager = new PersistentDatabasesManager();

    /**
     * PersistentDatabasesManager.getRedisManager().
     */
    @Getter
    @Setter
    private static MongoManager mongoManager = MongoManager.getMongoManager();

    /**
     * PersistentDatabasesManager.getMySQLManager().
     */
    @Getter
    @Setter
    private static MySQLManager mySQLManager = MySQLManager.getMySQLManager();

    public static boolean initPersistentDatabases() {
        switch (YamlManager.getConfig().getString("DATA-STORAGE-TYPE").toLowerCase()) {
            case ConstantsUtils.MYSQL_DB_TYPE:
                PersistentDatabasesManager.setPersistentDataStorageType(PersistentDataStorageType.MySQL);

                if (PersistentDatabasesManager.getPersistentDatabasesManager().setup(YamlManager.getConfig()) == ConnectStatus.CannotConnect) {
                    return false;
                }

                break;

            case ConstantsUtils.MONGODB_DB_TYPE:
                PersistentDatabasesManager.setPersistentDataStorageType(PersistentDataStorageType.MongoDB);

                if (PersistentDatabasesManager.getPersistentDatabasesManager().setup(YamlManager.getConfig()) == ConnectStatus.CannotConnect) {
                    return false;
                }

                break;

            case ConstantsUtils.FILE_DB_TYPE:
            default:
                PersistentDatabasesManager.setPersistentDataStorageType(PersistentDataStorageType.File);
                break;
        }

        LoggerUtils.getLogger().info("持久化存储初始化完毕, 服务端持久化数据存储方式: {}", PersistentDatabasesManager.getPersistentDataStorageType());
        return true;
    }

    /**
     * 根据指定的 YAML 配置, 初始化数据库连接。
     *
     * @param yaml 包含数据库连接信息的 YAML 配置
     * @return 数据库连接状态
     */
    @Override
    public ConnectStatus setup(Yaml yaml) {
        switch (getPersistentDataStorageType()) {
            case MongoDB:
                return getMongoManager().setup(yaml);

            case MySQL:
                return getMySQLManager().setup(yaml);

            default:
                return null;
        }
    }

    /**
     * 根据给定的 UUID、Key 和默认值获取对应的值, 若未找到则插入默认值并返回。若找到的值为 null, 则更新为默认值并返回。
     *
     * @param uuid               标识符
     * @param key                查询的 Key
     * @param defaultValue       查询的默认值
     * @param databaseCollection 查询的数据集合
     * @return 对应的值
     */
    @Override
    public Object getOrDefault(String uuid, String key, Object defaultValue, String databaseCollection) {
        switch (getPersistentDataStorageType()) {
            case MongoDB:
                return getMongoManager().getOrDefault(uuid, key, defaultValue, databaseCollection);

            case MySQL:
                return getMySQLManager().getOrDefault(uuid, key, defaultValue, databaseCollection);

            default:
                return null;
        }
    }

    /**
     * 根据给定的 UUID、Key 和默认值获取对应的 List 值, 若未找到则插入默认值并返回。若找到的值为 null, 则更新为默认值并返回。
     *
     * @param uuid               标识符
     * @param key                查询的 Key
     * @param defaultValue       查询的默认值
     * @param databaseCollection 查询的数据集合
     * @return 对应的 List 值
     */
    @Override
    public ConcurrentLinkedQueue<Object> getOrDefaultList(String uuid, String key, ConcurrentLinkedQueue<Object> defaultValue, String databaseCollection) {
        switch (getPersistentDataStorageType()) {
            case MongoDB:
                return getMongoManager().getOrDefaultList(uuid, key, defaultValue, databaseCollection);

            case MySQL:
                return getMySQLManager().getOrDefaultList(uuid, key, defaultValue, databaseCollection);

            default:
                return null;
        }
    }

    /**
     * 根据给定的 UUID、Key 和默认值获取对应的 Map 值, 若未找到则插入默认值并返回。若找到的值为 null, 则更新为默认值并返回。
     *
     * @param uuid               标识符
     * @param key                查询的 Key
     * @param defaultValue       查询的默认值
     * @param databaseCollection 查询的数据集合
     * @return 对应的 Map 值
     */
    @Override
    public Map<Object, Object> getOrDefaultMap(String uuid, String key, Map<Object, Object> defaultValue, String databaseCollection) {
        switch (getPersistentDataStorageType()) {
            case MongoDB:
                return getMongoManager().getOrDefaultMap(uuid, key, defaultValue, databaseCollection);

            case MySQL:
                return getMySQLManager().getOrDefaultMap(uuid, key, defaultValue, databaseCollection);

            default:
                return null;
        }
    }

    /**
     * 根据给定的 UUID、Key 更新数据值, 若未找到则插入数据值并返回。
     *
     * @param uuid               标识符
     * @param key                查询的 Key
     * @param value              数据的值
     * @param databaseCollection 更新的数据集合
     */
    @Override
    public void update(String uuid, String key, Object value, String databaseCollection) {
        switch (getPersistentDataStorageType()) {
            case MongoDB:
                getMongoManager().update(uuid, key, value, databaseCollection);
                break;

            case MySQL:
                getMySQLManager().update(uuid, key, value, databaseCollection);
                break;
        }
    }

    /**
     * 获取指定列的所有名称。
     *
     * @param column         指定的列名
     * @param collectionName 数据集合名称
     * @return 包含所有名称的列表
     */
    @Override
    public List<String> getColumnNames(String column, String collectionName) {
        switch (getPersistentDataStorageType()) {
            case MongoDB:
                return getMongoManager().getColumnNames(column, collectionName);

            case MySQL:
                return getMySQLManager().getColumnNames(column, collectionName);

            default:
                return null;
        }
    }

    /**
     * 删除指定 UUID 的所有数据。
     *
     * @param uuid               要删除数据的 UUID
     * @param databaseCollection 数据集合
     */
    @Override
    public void delete(String uuid, String databaseCollection) {
        switch (getPersistentDataStorageType()) {
            case MongoDB:
                getMongoManager().delete(uuid, databaseCollection);
                break;

            case MySQL:
                getMySQLManager().delete(uuid, databaseCollection);
                break;
        }
    }
}
