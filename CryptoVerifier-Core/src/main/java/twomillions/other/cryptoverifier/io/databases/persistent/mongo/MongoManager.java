package twomillions.other.cryptoverifier.io.databases.persistent.mongo;

import com.mongodb.DBCollection;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.UpdateOptions;
import de.leonhard.storage.Yaml;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bson.Document;
import twomillions.other.cryptoverifier.io.databases.enums.status.AuthStatus;
import twomillions.other.cryptoverifier.io.databases.enums.status.ConnectStatus;
import twomillions.other.cryptoverifier.io.databases.enums.status.CustomUrlStatus;
import twomillions.other.cryptoverifier.io.databases.interfaces.DatabasesInterface;
import twomillions.other.cryptoverifier.util.LoggerUtils;
import twomillions.other.cryptoverifier.util.QuickUtils;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MongoManager implements DatabasesInterface {
    @Getter
    private static final MongoManager mongoManager = new MongoManager();

    private volatile String ip;
    private volatile String port;
    private volatile String username;
    private volatile String password;
    private volatile AuthStatus authStatus;
    private volatile MongoClient mongoClient;
    private volatile DBCollection dbCollection;
    private volatile MongoDatabase mongoDatabase;
    private volatile String mongoClientUrlString;
    private volatile MongoClientURI mongoClientUrl;
    private volatile CustomUrlStatus customUrlStatus;
    private volatile ConnectStatus connectStatus = ConnectStatus.TurnOff;

    @Override
    public ConnectStatus setup(Yaml yaml) {
        setIp(yaml.getString("MONGO.IP"));
        setPort(yaml.getString("MONGO.PORT"));
        setUsername(yaml.getString("MONGO.AUTH.USER"));
        setPassword(yaml.getString("MONGO.AUTH.PASSWORD"));

        setCustomUrlStatus(yaml.getString("MONGO.CUSTOM-URL").isEmpty() ? CustomUrlStatus.TurnOff : CustomUrlStatus.TurnOn);

        if (getCustomUrlStatus() == CustomUrlStatus.TurnOn) {
            setMongoClientUrlString(yaml.getString("MONGO.CUSTOM-URL"));
        } else {
            if (getUsername().isEmpty() || getPassword().isEmpty()) {
                setAuthStatus(AuthStatus.TurnOff);
                setMongoClientUrlString("mongodb://" + getIp() + ":" + getPort() + "/CryptoVerifier");
            } else {
                setAuthStatus(AuthStatus.UsingAuth);
                setMongoClientUrlString("mongodb://" + getUsername() + ":" + getPassword() + "@" + getIp() + ":" + getPort() + "/CryptoVerifier");

                LoggerUtils.getLogger().info("检查到 Mongo 开启身份验证, 已设置身份验证信息!");
            }
        }

        try {
            setMongoClientUrl(new MongoClientURI(getMongoClientUrlString()));
            setMongoClient(new MongoClient(getMongoClientUrl()));
            setMongoDatabase(getMongoClient().getDatabase("CryptoVerifier"));

            setConnectStatus(ConnectStatus.Connected);
            LoggerUtils.getLogger().info("已建立与 MongoDB 的连接!");
        } catch (Exception exception) {
            setConnectStatus(ConnectStatus.CannotConnect);
            LoggerUtils.getLogger().error("您打开了 MongoDB 数据库选项, 但未能正确连接到 MongoDB, 请检查 MongoDB 服务状态!", exception);
        }

        return getConnectStatus();
    }

    @Override
    public Object getOrDefault(String uuid, String key, Object defaultValue, String databaseCollection) {
        MongoCollection<Document> collection = getMongoDatabase().getCollection(databaseCollection);

        Document filter = new Document("_id", uuid);
        Document document = collection.find(filter).first();

        if (document == null) {
            document = new Document("_id", uuid).append(key, defaultValue);
            collection.insertOne(document);
            return defaultValue;
        }

        Object value = document.get(key);

        if (value == null) {
            collection.updateOne(filter, new Document("$set", new Document(key, defaultValue)));
            return defaultValue;
        }

        return value;
    }

    @Override
    public ConcurrentLinkedQueue<Object> getOrDefaultList(String uuid, String key, ConcurrentLinkedQueue<Object> defaultValue, String databaseCollection) {
        MongoCollection<Document> collection = getMongoDatabase().getCollection(databaseCollection);

        Document filter = new Document("_id", uuid);
        Document document = collection.find(filter).first();

        if (document == null) {
            document = new Document("_id", uuid).append(key, defaultValue);
            collection.insertOne(document);
            return defaultValue;
        }

        ConcurrentLinkedQueue<Object> value = new ConcurrentLinkedQueue<>(document.getList(key, Object.class));

        if (value.size() == 0) {
            collection.updateOne(filter, new Document("$set", new Document(key, defaultValue)));
            return defaultValue;
        }

        return value;
    }

    @Override
    public Map<Object, Object> getOrDefaultMap(String uuid, String key, Map<Object, Object> defaultValue, String databaseCollection) {
        return QuickUtils.stringToMap(getOrDefault(uuid, key, defaultValue, databaseCollection).toString());
    }

    @Override
    public void update(String uuid, String key, Object value, String databaseCollection) {
        MongoCollection<Document> collection = getMongoDatabase().getCollection(databaseCollection);
        collection.updateOne(new Document("_id", uuid), new Document("$set", new Document(key, value.toString())), new UpdateOptions().upsert(true));
    }

    @Override
    public List<String> getColumnNames(String column, String collectionName) {
        MongoCollection<Document> collection = getMongoDatabase().getCollection(collectionName);

        return StreamSupport.stream(collection.find().spliterator(), false)
                .map(document -> document.get(column))
                .filter(Objects::nonNull)
                .map(Object::toString)
                .collect(Collectors.toList());
    }

    @Override
    public void delete(String uuid, String databaseCollection) {
        MongoCollection<Document> collection = getMongoDatabase().getCollection(databaseCollection);
        collection.deleteMany(new Document("_id", uuid));
    }
}
