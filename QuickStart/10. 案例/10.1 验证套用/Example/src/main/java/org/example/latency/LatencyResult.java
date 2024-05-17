package org.example.latency;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 一个简单的对象，用来存储 IP 延迟。
 *
 * @author 2000000
 * @version 1.0
 * @since 2023/6/17
 */
@Getter
@RequiredArgsConstructor
public class LatencyResult {
    /**
     * 服务器地址。
     */
    private final String server;

    /**
     * 服务器延迟，以毫秒为单位。
     */
    private final long latency;
}
