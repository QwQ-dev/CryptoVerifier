package twomillions.other.cryptoverifier.security.detectors;

import lombok.Getter;
import twomillions.other.cryptoverifier.security.interfaces.SecurityDetectorInterface;
import twomillions.other.cryptoverifier.io.databases.DataManager;
import twomillions.other.cryptoverifier.io.databases.nonpersistent.NonPersistentDatabasesManager;
import twomillions.other.cryptoverifier.io.yaml.YamlManager;
import twomillions.other.cryptoverifier.thread.VerifierThreadPool;
import twomillions.other.cryptoverifier.util.LoggerUtils;
import twomillions.other.cryptoverifier.util.common.ConstantsUtils;
import twomillions.other.cryptoverifier.util.QuickUtils;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class ParallelLimitDetector implements SecurityDetectorInterface {
    @Getter
    private static final ParallelLimitDetector parallelLimitDetector = new ParallelLimitDetector();

    public void initializeAndStart() {
        long parallelCheckInterval = YamlManager.getConfig().getLong("PARALLEL-CHECK-INTERVAL");
        VerifierThreadPool.getVerifierThreadPool().runTimeAsync(this::parallelLimitDetectorRunnable, parallelCheckInterval, parallelCheckInterval);
        LoggerUtils.getLogger().info("并行检查任务已开始运行, 间隔: {} ms", parallelCheckInterval);
    }

    public List<String> getKeys(String uuid) {
        Map<Object, Object> uuidParallelData = getUuidParallelData(uuid);
        return uuidParallelData.keySet().stream()
                .map(Object::toString)
                .collect(Collectors.toList());
    }

    public Map<Object, Object> getUuidParallelData(String uuid) {
        return NonPersistentDatabasesManager.getNonPersistentDatabasesManager()
                .getOrDefaultMap(uuid, "parallelData", new ConcurrentHashMap<>(), ConstantsUtils.SERVER_DATA);
    }

    public void updateUuidParallelData(String uuid, Map<Object, Object> map) {
        NonPersistentDatabasesManager.getNonPersistentDatabasesManager().update(uuid, "parallelData", map, ConstantsUtils.SERVER_DATA);
    }

    private void parallelLimitDetectorRunnable() {
        List<String> uuids = DataManager.get();

        if (uuids == null || uuids.isEmpty()) {
            return;
        }

        uuids.parallelStream().forEach(uuid -> {
            Map<Object, Object> map = NonPersistentDatabasesManager.getNonPersistentDatabasesManager()
                    .getOrDefaultMap(uuid, "parallelData", new ConcurrentHashMap<>(), ConstantsUtils.SERVER_DATA);

            map.entrySet().removeIf(entry -> {
                String value = entry.getValue().toString();
                return QuickUtils.convertScientificToNormal(value) < System.currentTimeMillis();
            });

            NonPersistentDatabasesManager.getNonPersistentDatabasesManager().update(uuid, "parallelData", map, ConstantsUtils.SERVER_DATA);
        });
    }
}
