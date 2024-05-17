package twomillions.other.cryptoverifier.events.custom;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import twomillions.other.cryptoverifier.events.impl.interfaces.Event;

/**
 * 许可证验证完成事件。
 *
 * <p>
 * 通过使用 {@code @Getter} {@code @Setter} 和 {@code @Accessors(chain = true)} 注解，提供用于获取和设置属性的方法，支持链式调用。
 * </p>
 *
 * <p>
 * 若为这些返回值则不会触发该事件: {@code UNKNOWN}、{@code FILE_ERROR}、{@code FILE_NOT_FOUND_ERROR}.
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
public class LicenseValidationCompleteEvent implements Event {
    /**
     * 客户端 UUID.
     */
    private final String uuid;

    /**
     * 客户端 IP.
     */
    private final String ip;

    /**
     * 客户端硬件特征码。
     */
    private final String hwid;

    /**
     * 客户端发送的额外数据。
     */
    private final String extraData;

    /**
     * 返回结果。
     *
     * <p>
     * 您可以更改此处自定义内容以达到插件混淆的目的，甚至读取类、Jar 以达到远程加载的目的。
     * </p>
     *
     * <p>
     * 您不必特意加密这里的数据，这里的数据会自己进行加密处理。
     * </p>
     */
    private String result;
}
