package twomillions.other.cryptoverifier.events.custom;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import twomillions.other.cryptoverifier.events.impl.interfaces.Event;

/**
 * 插件加载事件。
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
@RequiredArgsConstructor
@Accessors(chain = true)
public class LoadPluginsEvent implements Event {
    /**
     * 路径。
     */
    private final String directoryPath;
}

