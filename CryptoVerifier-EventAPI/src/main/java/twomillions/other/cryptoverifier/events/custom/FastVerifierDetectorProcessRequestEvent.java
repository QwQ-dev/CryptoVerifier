package twomillions.other.cryptoverifier.events.custom;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import twomillions.other.cryptoverifier.events.impl.interfaces.Event;

import java.util.Map;
import java.util.Set;

/**
 * 验证速率检查的请求处理事件。
 *
 * <p>
 * 通过使用 {@code @Getter} {@code @Setter} 和 {@code @Accessors(chain = true)} 注解，提供用于获取和设置属性的方法，支持链式调用。
 * </p>
 *
 * @author 2000000
 * @version 1.0
 * @since 2023/7/10
 */
@Getter
@Setter
@AllArgsConstructor
@Accessors(chain = true)
public class FastVerifierDetectorProcessRequestEvent implements Event {
    /**
     * 请求地址。
     */
    private final String ipAddress;

    /**
     * 存储所有 IP 与对应请求次数的 Map.
     */
    private Map<String, Integer> requestMap;

    /**
     * 所有黑名单 IP.
     */
    private Set<String> blockIps;

    /**
     * 是否取消该事件。
     */
    private boolean cancel;
}
