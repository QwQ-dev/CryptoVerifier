package org.example.utils;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 时间相关操作的实用工具类。
 *
 * @author 2000000
 * @version 1.0
 * @since 2023/6/23
 */
@SuppressWarnings("unused")
public class TimeUtils {
    /**
     * 将长整型时间戳转换为格式为 "yyyy/MM/dd HH:mm" 的日期字符串。
     *
     * @param timestamp 要转换的时间戳
     * @return 格式化后的日期字符串
     */
    public static String convertLongToDateString(long timestamp) {
        return new SimpleDateFormat("yyyy/MM/dd HH:mm").format(new Date(timestamp));
    }

    /**
     * 将日期字符串转换为长整型时间戳。
     *
     * @param dateString 要转换的日期字符串，格式为 "yyyy/MM/dd HH:mm"
     * @return 转换后的长整型时间戳
     */
    public static long convertDateStringToLong(String dateString) {
        return new SimpleDateFormat("yyyy/MM/dd HH:mm").parse(dateString, new ParsePosition(0)).getTime();
    }
}
