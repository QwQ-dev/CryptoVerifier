package twomillions.other.cryptoverifier.events.impl.interfaces;

import twomillions.other.cryptoverifier.util.ThreadUtils;

/**
 * 定义事件接口。
 *
 * @author 2000000
 * @version 1.0
 * @since 2023/7/2
 */
public interface Event {
    /**
     * 获取事件是否为同步事件。
     *
     * @return 获取事件是否为同步事件
     */
    default boolean isSync() {
        return ThreadUtils.isSync();
    }

    /**
     * 获取事件执行线程。
     *
     * @return 获取事件执行线程
     */
    default Thread getThread() {
        return Thread.currentThread();
    }
}
