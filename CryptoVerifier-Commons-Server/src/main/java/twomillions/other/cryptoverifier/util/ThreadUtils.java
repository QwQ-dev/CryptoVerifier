package twomillions.other.cryptoverifier.util;

import lombok.experimental.UtilityClass;

/**
 * 该类用于线程操作的实用方法的工具类
 *
 * @author 2000000
 * @version 1.0
 * @since 2023/7/10
 */
@UtilityClass
public class ThreadUtils {
    /**
     * 检查当前线程是否在主线程中执行。
     *
     * @return 是否在主线程中执行
     */
    public static boolean isSync() {
        return Thread.currentThread().getName().equals("main");
    }
}
