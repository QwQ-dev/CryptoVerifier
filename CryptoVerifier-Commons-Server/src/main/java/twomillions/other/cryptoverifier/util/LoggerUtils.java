package twomillions.other.cryptoverifier.util;

import lombok.experimental.UtilityClass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 该类提供获取 {@link Logger} 实例和发送空日志信息的方法。
 *
 * @author 2000000
 * @version 1.0
 * @since 2023/7/16
 */
@UtilityClass
public class LoggerUtils {
    /**
     * 获取 {@code NORMAL} {@link Logger} 实例。
     *
     * @return {@link Logger} 实例
     */
    public static Logger getLogger() {
        return LoggerFactory.getLogger("NORMAL");
    }

    /**
     * 发送空的信息日志。
     */
    public static void sendEmptyInfo() {
        LoggerFactory.getLogger("SIMPLE").info(" ");
    }
}