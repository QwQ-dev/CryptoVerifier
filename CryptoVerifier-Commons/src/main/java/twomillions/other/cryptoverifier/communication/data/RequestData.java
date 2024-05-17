package twomillions.other.cryptoverifier.communication.data;

import com.google.gson.GsonBuilder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import twomillions.other.cryptoverifier.communication.enums.RequestHeader;

/**
 * 请求数据对象。
 *
 * <p>
 * 该类用于存储请求消息和请求头信息。
 * </p>
 *
 * <p>
 * 通过使用 {@code @Getter}、{@code @Setter} 和 {@code @Accessors(chain = true)} 注解，提供用于获取和设置属性的方法，支持链式调用。
 * </p>
 *
 * @author 2000000
 * @version 1.0
 * @since 2023/8/12
 */
@Getter
@Setter
@Accessors(chain = true)
public class RequestData {
    /**
     * 请求消息内容。
     */
    private String message;
    /**
     * 请求头信息。
     */
    private RequestHeader requestHeader;

    /**
     * 将对象转换为 JSON 格式的字符串。
     *
     * @return 对象的 JSON 字符串表示
     */
    @Override
    public String toString() {
        return new GsonBuilder().create().toJson(this);
    }

    /**
     * 将字符串转换为 {@link RequestData} 对象。
     *
     * @param string 要转换的 JSON 字符串
     * @return 转换后的 {@link RequestData} 对象
     */
    public static RequestData fromString(String string) {
        return new GsonBuilder().create().fromJson(string, RequestData.class);
    }
}
