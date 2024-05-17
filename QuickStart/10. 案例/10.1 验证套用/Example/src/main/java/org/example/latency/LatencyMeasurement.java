package org.example.latency;


import java.net.InetAddress;
import java.util.Comparator;
import java.util.List;

/**
 * 延迟测量工具类。
 *
 * <p>
 * 该工具类提供了测量服务器延迟的方法，以及查找延迟最低的服务器的功能。
 * </p>
 *
 * @author 2000000
 * @version 1.1
 * @since 2023/6/17
 */
public class LatencyMeasurement {
    /**
     * 查找延迟最低的服务器。
     *
     * @param serverList 服务器 IP 列表
     * @return 延迟最低的服务器 IP
     */
    public static String findLowestLatencyServer(@SuppressWarnings("SameParameterValue") List<String> serverList) {
        return serverList.parallelStream()
                .map(server -> new LatencyResult(server, measureLatency(server)))
                .filter(result -> result.getLatency() != Long.MAX_VALUE)
                .min(Comparator.comparingLong(LatencyResult::getLatency))
                .map(LatencyResult::getServer)
                .orElse(null);
    }

    /**
     * 使用 ICMP Echo 请求 (Ping) 检测服务器的延迟。
     *
     * <p>
     * 检测的服务器不应禁止 ICMP 协议，否则将导致延迟检测被拒绝。
     * </p>
     *
     * @param server 服务器 IP 地址，包含端口以及类型声明等
     * @return 延迟 (以毫秒为单位)
     */
    public static long measureLatency(String server) {
        server = server.contains(":") ? server.split(":")[0] : server;

        long startTime = System.currentTimeMillis();

        try {
            return InetAddress.getByName(server).isReachable(5000) ? System.currentTimeMillis() - startTime : Long.MAX_VALUE;
        } catch (Exception exception) {
            return Long.MAX_VALUE;
        }
    }
}
