package twomillions.other.cryptoverifier.events.custom;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.Accessors;
import twomillions.other.cryptoverifier.events.impl.interfaces.Event;
import twomillions.other.cryptoverifier.crypto.verifier.enums.VerifierServerType;

/**
 * 许可证验证服务事件。
 *
 * <p>
 * 通过使用 {@code @Getter} 和 {@code @Accessors(chain = true)} 注解，提供用于获取属性的方法，支持链式调用。
 * </p>
 *
 * @author 2000000
 * @version 1.0
 * @since 2023/7/10
 */
@Getter
@AllArgsConstructor
@Accessors(chain = true)
public class VerifierServerStartEvent implements Event {
    /**
     * 服务开启的端口。
     */
    private final int port;

    /**
     * 许可证验证服务类型，为 {@link VerifierServerType} 枚举类。
     */
    private final VerifierServerType verifierServerType;
}
