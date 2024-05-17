package twomillions.other.cryptoverifier.security.detectors;

import com.google.common.collect.Sets;
import lombok.Getter;
import twomillions.other.cryptoverifier.events.custom.FastVerifierDetectorProcessRequestEvent;
import twomillions.other.cryptoverifier.events.custom.FastVerifierDetectorResetRequestEvent;
import twomillions.other.cryptoverifier.events.impl.EventManager;
import twomillions.other.cryptoverifier.security.interfaces.SecurityDetectorInterface;
import twomillions.other.cryptoverifier.io.yaml.YamlManager;
import twomillions.other.cryptoverifier.thread.VerifierThreadPool;
import twomillions.other.cryptoverifier.util.LoggerUtils;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class FastVerifierDetector implements SecurityDetectorInterface {
    @Getter
    private static final FastVerifierDetector fastVerifierDetector = new FastVerifierDetector();

    @Getter
    private Set<String> blockIps = Sets.newConcurrentHashSet();

    @Getter
    private Map<String, Integer> requestMap = new ConcurrentHashMap<>();

    @Override
    public void initializeAndStart() {
        long resetInterval = YamlManager.getConfig().getLong("RESET-INTERVAL");
        VerifierThreadPool.getVerifierThreadPool().runTimeAsync(this::resetRequest, resetInterval, resetInterval);
    }

    public void processRequest(String ipAddress) {
        FastVerifierDetectorProcessRequestEvent fastVerifierDetectorProcessRequestEvent =
                new FastVerifierDetectorProcessRequestEvent(ipAddress, requestMap, blockIps, false);

        EventManager.getEventManager().callEvent(fastVerifierDetectorProcessRequestEvent);

        if (fastVerifierDetectorProcessRequestEvent.isCancel()) {
            return;
        }

        int count = requestMap.getOrDefault(ipAddress, 0);
        requestMap.put(ipAddress, count + 1);

        blockIps = fastVerifierDetectorProcessRequestEvent.getBlockIps();
        requestMap = fastVerifierDetectorProcessRequestEvent.getRequestMap();

        if (count + 1 > YamlManager.getConfig().getInt("THRESHOLD")) {
            blockIp(ipAddress);
        }
    }

    public void blockIp(String ipAddress) {
        blockIps.add(ipAddress);
    }

    public void resetRequest() {
        if (requestMap.isEmpty()) {
            return;
        }

        FastVerifierDetectorResetRequestEvent fastVerifierDetectorResetRequestEvent =
                new FastVerifierDetectorResetRequestEvent(requestMap, blockIps, false);

        EventManager.getEventManager().callEvent(fastVerifierDetectorResetRequestEvent);

        blockIps = fastVerifierDetectorResetRequestEvent.getBlockIps();
        requestMap = fastVerifierDetectorResetRequestEvent.getRequestMap();

        LoggerUtils.sendEmptyInfo();
        LoggerUtils.getLogger().info("即将清除所有的 IP 黑名单, 在过去的一分钟之内请求状况如下:");

        for (String ip : requestMap.keySet()) {
            LoggerUtils.getLogger().info("IP: {}, 请求次数: {}", ip, requestMap.get(ip));
        }

        for (String blockIp : blockIps) {
            LoggerUtils.getLogger().info("IP: {}, 被屏蔽为黑名单, 请求次数: {}", blockIp, requestMap.get(blockIp));
        }

        LoggerUtils.sendEmptyInfo();

        blockIps.clear();
        requestMap.clear();
    }
}
