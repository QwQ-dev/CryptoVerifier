package twomillions.other.cryptoverifier.events.impl;

import lombok.AllArgsConstructor;
import lombok.Getter;
import twomillions.other.cryptoverifier.events.impl.enums.EventPriority;
import twomillions.other.cryptoverifier.events.impl.interfaces.Listener;

import java.lang.reflect.Method;

/**
 * 已注册监听器对象。
 *
 * @author 2000000
 * @version 1.0
 * @since 2023/7/2
 */
@Getter
@AllArgsConstructor
public class RegisteredListener {
    /**
     * 实现 {@link Listener} 的类。
     */
    private final Listener listener;

    /**
     * 事件处理方法。
     */
    private final Method method;

    /**
     * 事件处理优先级。
     */
    private final EventPriority priority;
}
