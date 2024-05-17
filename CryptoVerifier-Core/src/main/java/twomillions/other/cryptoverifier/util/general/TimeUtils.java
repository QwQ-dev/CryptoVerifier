package twomillions.other.cryptoverifier.util.general;


import lombok.experimental.UtilityClass;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 时间相关操作的实用工具类。
 *
 * <p>
 * 该类使用 {@link UtilityClass} 自动生成私有构造函数，并自动处理类、字段、内部类等。
 * </p>
 *
 * @author 2000000
 * @version 1.0
 * @since 2023/8/27
 */
@UtilityClass
public class TimeUtils {
    /**
     * 将日期字符串转换为对应的时间戳。
     *
     * @param dateString 日期字符串，格式为 "yyyy/MM/dd HH:mm"
     * @return 转换后的时间戳
     */
    public static long convertDateStringToLong(String dateString) {
        return new SimpleDateFormat("yyyy/MM/dd HH:mm").parse(dateString, new ParsePosition(0)).getTime();
    }

    /**
     * 将时间戳转换为对应的日期字符串。
     *
     * @param timestamp 时间戳
     * @return 转换后的日期字符串，格式为 "yyyy/MM/dd HH:mm"
     */
    public static String convertLongToDateString(long timestamp) {
        return new SimpleDateFormat("yyyy/MM/dd HH:mm").format(new Date(timestamp));
    }

    /**
     * 将字符串形式的时间戳转换为对应的日期字符串。
     *
     * @param timestamp 字符串形式的时间戳
     * @return 转换后的日期字符串，格式为 "yyyy/MM/dd HH:mm"
     * @throws NumberFormatException 如果无法将字符串解析为有效的时间戳
     */
    public static String convertLongToDateString(String timestamp) {
        return new SimpleDateFormat("yyyy/MM/dd HH:mm").format(new Date(Long.parseLong(timestamp)));
    }
}
