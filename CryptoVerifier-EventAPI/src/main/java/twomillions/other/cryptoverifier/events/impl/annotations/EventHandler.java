package twomillions.other.cryptoverifier.events.impl.annotations;

import twomillions.other.cryptoverifier.events.impl.enums.EventPriority;
import twomillions.other.cryptoverifier.events.impl.interfaces.Listener;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 事件监听注解，事件处理方法必须使用此注解，监听器类需实现 {@link Listener} 接口。
 *
 * @author 2000000
 * @version 1.0
 * @since 2023/7/2
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface EventHandler {
    /**
     * 事件优先级。
     *
     * @return {@link EventPriority}
     */
    EventPriority priority() default EventPriority.NORMAL;
}
