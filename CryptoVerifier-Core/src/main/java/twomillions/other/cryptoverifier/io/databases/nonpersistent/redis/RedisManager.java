package twomillions.other.cryptoverifier.io.databases.nonpersistent.redis;

import de.leonhard.storage.Yaml;
import lombok.*;
import lombok.experimental.Accessors;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import twomillions.other.cryptoverifier.io.databases.enums.status.AuthStatus;
import twomillions.other.cryptoverifier.io.databases.enums.status.ConnectStatus;
import twomillions.other.cryptoverifier.io.databases.enums.status.CustomUrlStatus;
import twomillions.other.cryptoverifier.io.databases.interfaces.DatabasesInterface;
import twomillions.other.cryptoverifier.util.LoggerUtils;
import twomillions.other.cryptoverifier.util.QuickUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;

@Getter
@Setter
@Accessors(chain = true)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RedisManager implements DatabasesInterface {
    @Getter
    private static final RedisManager redisManager = new RedisManager();
    private static final String TABLES_KEY = "CryptoVerifier";

    private String ip;
    private String port;
    private String username;
    private String password;
    private JedisPool jedisPool;
    private String redisUrlString;
    private AuthStatus authStatus;
    private CustomUrlStatus customUrlStatus;
    private ConnectStatus connectStatus = ConnectStatus.TurnOff;

    @Override
    public ConnectStatus setup(Yaml yaml) {
        setIp(yaml.getString("REDIS.IP"));
        setPort(yaml.getString("REDIS.PORT"));
        setUsername(yaml.getString("REDIS.AUTH.USER"));
        setPassword(yaml.getString("REDIS.AUTH.PASSWORD"));

        setCustomUrlStatus(yaml.getString("REDIS.CUSTOM-URL").isEmpty() ? CustomUrlStatus.TurnOff : CustomUrlStatus.TurnOn);

        if (getCustomUrlStatus() == CustomUrlStatus.TurnOn) {
            setRedisUrlString(yaml.getString("REDIS.CUSTOM-URL"));
            setJedisPool(new JedisPool(getRedisUrlString()));
        } else {
            setJedisPool(new JedisPool(getIp(), Integer.parseInt(getPort())));
        }

        try (Jedis jedis = getResource()) {
            jedis.ping();
            setConnectStatus(ConnectStatus.Connected);
            LoggerUtils.getLogger().info("已建立与 Redis 的连接!");
        } catch (Exception exception) {
            setConnectStatus(ConnectStatus.CannotConnect);
            LoggerUtils.getLogger().error("未能正确连接到 Redis, 请检查 Redis 服务状态!", exception);
        }

        return getConnectStatus();
    }

    private Jedis getResource() {
        Jedis jedis = jedisPool.getResource();

        if (!getUsername().isEmpty() && !getPassword().isEmpty()) {
            jedis.auth(getUsername(), getPassword());
            return jedis;
        }

        if (getUsername().isEmpty() && !getPassword().isEmpty()) {
            jedis.auth(getPassword());
            return jedis;
        }

        return jedis;
    }

    @Override
    public Object getOrDefault(String uuid, String key, Object defaultValue, String databaseCollection) {
        @Cleanup Jedis jedis = getResource();

        String tableKey = getTableKey(databaseCollection);
        String value = jedis.hget(tableKey, getKey(uuid, key));

        if (value == null) {
            update(uuid, key, defaultValue, databaseCollection);
            return defaultValue;
        }

        return value;
    }

    @Override
    public ConcurrentLinkedQueue<Object> getOrDefaultList(String uuid, String key, ConcurrentLinkedQueue<Object> defaultValue, String databaseCollection) {
        @Cleanup Jedis jedis = getResource();

        String tableKey = getTableKey(databaseCollection);
        String value = jedis.hget(tableKey, getKey(uuid, key));

        if (value == null) {
            update(uuid, key, defaultValue, databaseCollection);
            return defaultValue;
        }

        return QuickUtils.stringToList(value);
    }

    @Override
    public Map<Object, Object> getOrDefaultMap(String uuid, String key, Map<Object, Object> defaultValue, String databaseCollection) {
        return QuickUtils.stringToMap(getOrDefault(uuid, key, defaultValue, databaseCollection).toString());
    }

    @Override
    public void update(String uuid, String key, Object value, String databaseCollection) {
        @Cleanup Jedis jedis = getResource();

        if (value == null || value.toString().equals("{}")) {
            delete(uuid, databaseCollection);
            return;
        }

        String tableKey = getTableKey(databaseCollection);
        jedis.hset(tableKey, getKey(uuid, key), value.toString());
    }

    @Override
    public void delete(String uuid, String databaseCollection) {
        @Cleanup Jedis jedis = getResource();

        String tableKey = getTableKey(databaseCollection);
        jedis.hdel(tableKey, uuid);
    }

    @Override
    public List<String> getColumnNames(String column, String collectionName) {
        @Cleanup Jedis jedis = getResource();

        String tableKey = getTableKey(collectionName);
        return new ArrayList<>(jedis.hkeys(tableKey));
    }

    private String getTableKey(String collectionName) {
        return TABLES_KEY + ":" + collectionName;
    }

    private String getKey(String uuid, String key) {
        return uuid + ":" + key;
    }
}
