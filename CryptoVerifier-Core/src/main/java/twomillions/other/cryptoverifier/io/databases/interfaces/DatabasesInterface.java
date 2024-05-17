package twomillions.other.cryptoverifier.io.databases.interfaces;

import de.leonhard.storage.Yaml;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;

public interface DatabasesInterface {
    Object setup(Yaml yaml);

    Object getOrDefault(String uuid, String key, Object defaultValue, String databaseCollection);

    ConcurrentLinkedQueue<Object> getOrDefaultList(String uuid, String key, ConcurrentLinkedQueue<Object> defaultValue, String databaseCollection);

    Map<Object, Object> getOrDefaultMap(String uuid, String key, Map<Object, Object> defaultValue, String databaseCollection);

    void update(String uuid, String key, Object value, String databaseCollection);

    void delete(String uuid, String databaseCollection);

    List<String> getColumnNames(String column, String collectionName);
}
