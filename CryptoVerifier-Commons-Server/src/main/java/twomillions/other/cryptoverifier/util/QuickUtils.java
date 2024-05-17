package twomillions.other.cryptoverifier.util;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import lombok.experimental.UtilityClass;

import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * 实用工具类。
 *
 * @author 2000000
 * @version 1.0
 * @since 2023/7/16
 */
@UtilityClass
public class QuickUtils {
    /**
     * 将字符串转换为 {@link ConcurrentLinkedQueue} 对象。
     *
     * @param string 字符串
     * @return {@link ConcurrentLinkedQueue} 对象
     */
    public static <T> ConcurrentLinkedQueue<T> stringToList(String string) {
        Gson gson = new Gson();
        Type type = new TypeToken<ConcurrentLinkedQueue<T>>() {
        }.getType();
        return gson.fromJson(string, type);
    }

    /**
     * 将字符串转换为 {@link ConcurrentHashMap} 对象。
     *
     * @param string 字符串
     * @return {@link ConcurrentHashMap} 对象
     */
    public static <K, V> ConcurrentHashMap<K, V> stringToMap(String string) {
        Gson gson = new Gson();
        Type type = new TypeToken<ConcurrentHashMap<K, V>>() {
        }.getType();
        return gson.fromJson(string, type);
    }

    /**
     * 计算字符串中指定符号的数量。
     *
     * @param string 目标字符串
     * @param symbol 指定符号
     * @return 符号的数量
     */
    public static int countSymbolOccurrences(String string, char symbol) {
        return (int) string.chars().filter(c -> c == symbol).count();
    }

    /**
     * 将科学计数法表示的字符串转换为普通表示形式。
     *
     * @param scientificNotation 科学计数法表示的字符串
     * @return 转换后的普通形式
     */
    public static long convertScientificToNormal(String scientificNotation) {
        return new BigDecimal(scientificNotation).longValue();
    }
}
