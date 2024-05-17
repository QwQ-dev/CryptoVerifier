package twomillions.other.cryptoverifier.communication.data;

import com.google.gson.GsonBuilder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 凭证数据对象。
 *
 * <p>
 * 该类用于存储验证文件、HWID、文件名、额外数据。
 * </p>
 *
 * <p>
 * 通过使用 {@code @Getter} 和 {@code @RequiredArgsConstructor} 注解，提供用于获取属性的方法与对于 {@code final} 字段的构造器。
 * </p>
 *
 * @author 2000000
 * @version 1.1
 * @since 2023/8/12
 */
@Getter
@RequiredArgsConstructor
public class CredentialData {
    /**
     * 文件内容。
     */
    private final String file;
    /**
     * 文件名。
     */
    private final String fileName;
    /**
     * Hwid.
     */
    private final String hwid;
    /**
     * 额外数据。
     */
    private final String extraData;

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
     * 将字符串转换为 {@link CredentialData} 对象。
     *
     * @param string 要转换的 JSON 字符串
     * @return 转换后的 {@link CredentialData} 对象
     */
    public static CredentialData fromString(String string) {
        return new GsonBuilder().create().fromJson(string, CredentialData.class);
    }
}
