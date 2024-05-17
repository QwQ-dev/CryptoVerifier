package twomillions.other.cryptoverifier.io.databases;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import org.apache.commons.io.FileUtils;
import twomillions.other.cryptoverifier.io.databases.enums.types.PersistentDataStorageType;
import twomillions.other.cryptoverifier.io.yaml.YamlManager;
import twomillions.other.cryptoverifier.io.databases.nonpersistent.NonPersistentDatabasesManager;
import twomillions.other.cryptoverifier.io.databases.persistent.PersistentDatabasesManager;
import twomillions.other.cryptoverifier.util.LoggerUtils;
import twomillions.other.cryptoverifier.util.common.ConstantsUtils;

import java.io.File;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@SuppressWarnings("ResultOfMethodCallIgnored")
public class DataManager {
    private static final Cache<String, String> cache = CacheBuilder
            .newBuilder()
            .expireAfterWrite(YamlManager.getConfig().getInt("CACHE-EXPIRE-TIME"), TimeUnit.MINUTES)
            .build();

    public static List<String> get() {
        try {
            if (PersistentDatabasesManager.getPersistentDataStorageType() == PersistentDataStorageType.File) {
                File file = new File("data");
                file.mkdirs();

                return FileUtils.listFiles(file, null, false)
                        .stream()
                        .map(File::getName)
                        .collect(Collectors.toList());
            }

            String column = PersistentDatabasesManager.getPersistentDataStorageType() == PersistentDataStorageType.MongoDB ? "_id" : "uuid";
            return PersistentDatabasesManager.getPersistentDatabasesManager().getColumnNames(column, ConstantsUtils.SERVER_DATA);
        } catch (Exception exception) {
            LoggerUtils.getLogger().error("获取 Uuids 时错误!", exception);
            return null;
        }
    }

    public static String get(String uuid) {
        try {
            String cacheData = cache.getIfPresent(uuid);

            if (cacheData != null) {
                return cacheData;
            }

            String result;

            if (PersistentDatabasesManager.getPersistentDataStorageType() == PersistentDataStorageType.File) {
                File file = new File("data", uuid);
                result = FileUtils.readFileToString(file, "UTF-8");
            } else {
                result = PersistentDatabasesManager.getPersistentDatabasesManager().getOrDefault(uuid, "data", null, ConstantsUtils.SERVER_DATA).toString();
            }

            cache.put(uuid, result);
            return result;
        } catch (Exception exception) {
            return null;
        }
    }

    public static void delete(String uuid) {
        try {
            if (cache.getIfPresent(uuid) != null) {
                cache.invalidate(uuid);
            }

            NonPersistentDatabasesManager.getNonPersistentDatabasesManager().delete(uuid, ConstantsUtils.SERVER_DATA);

            if (PersistentDatabasesManager.getPersistentDataStorageType() == PersistentDataStorageType.File) {
                new File("data", uuid).delete();
                return;
            }

            PersistentDatabasesManager.getPersistentDatabasesManager().delete(uuid, ConstantsUtils.SERVER_DATA);
        } catch (Exception exception) {
            LoggerUtils.getLogger().error("删除 Uuid 对应数据时错误! 此 Uuid 对应的数据真的存在吗?", exception);
        }
    }

    public static void save(String uuid, String value) {
        try {
            if (cache.getIfPresent(uuid) != null) {
                cache.invalidate(uuid);
            }

            if (PersistentDatabasesManager.getPersistentDataStorageType() == PersistentDataStorageType.File) {
                FileUtils.writeStringToFile(new File("data", uuid), value, "UTF-8");
                return;
            }

            PersistentDatabasesManager.getPersistentDatabasesManager().update(uuid, "data", value, ConstantsUtils.SERVER_DATA);
        } catch (Exception exception) {
            LoggerUtils.getLogger().error("保存 Uuid 对应数据时错误!", exception);
        }
    }
}
