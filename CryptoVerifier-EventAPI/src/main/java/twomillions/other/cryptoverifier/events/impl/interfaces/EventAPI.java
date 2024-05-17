package twomillions.other.cryptoverifier.events.impl.interfaces;

/**
 * 接口类。
 *
 * @author 2000000
 * @version 1.0
 * @since 2023/7/2
 */
public interface EventAPI {
    /**
     * 注册监听器。
     *
     * @param listener {@link Listener}
     */
    void registerListener(Listener listener);

    /**
     * 触发事件。
     *
     * @param event {@link Event}
     */
    void callEvent(Event event);
}

