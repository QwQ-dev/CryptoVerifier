package twomillions.other.cryptoverifier.events.custom;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import twomillions.other.cryptoverifier.events.impl.interfaces.Event;

/**
 * 许可证创建事件。
 *
 * <p>
 * 通过使用 {@code @Getter} 和 {@code @Accessors(chain = true)} 注解，提供用于获取属性的方法，支持链式调用。
 * </p>
 *
 * <p>
 * 该事件主要用于传入自定义数据。
 * </p>
 *
 * @author 2000000
 * @version 1.0
 * @since 2023/7/18
 */
@Getter
@RequiredArgsConstructor
@Accessors(chain = true)
public class CreateLicenseEvent implements Event {
    /**
     * 服务器验证数据对象。
     */
    private final Object serverVerifierData;
}

