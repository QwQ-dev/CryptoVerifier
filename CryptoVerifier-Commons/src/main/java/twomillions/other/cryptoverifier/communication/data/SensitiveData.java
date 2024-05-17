package twomillions.other.cryptoverifier.communication.data;

import com.google.gson.GsonBuilder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.crypto.SecretKey;
import java.security.PublicKey;

/**
 * 敏感数据对象。
 *
 * <p>
 * 该类用于存储共享密钥、秘密共享密钥、客户端公钥和服务器公钥等敏感信息。
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
public class SensitiveData {
    /**
     * 共享密钥。
     */
    private String sharedKey;
    /**
     * 通过共享密钥派生的 AES 密钥。
     *
     * @see twomillions.other.cryptoverifier.crypto.encryption.aes.AESCryptography#generateSecretKey(String)
     */
    private SecretKey secretSharedKey;
    /**
     * 客户端公钥。
     */
    private PublicKey clientPublicKey;
    /**
     * 服务端公钥。
     */
    private PublicKey serverPublicKey;

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
     * 将字符串转换为 {@link SensitiveData} 对象。
     *
     * @param string 要转换的 JSON 字符串
     * @return 转换后的 {@link SensitiveData} 对象
     */
    public static SensitiveData fromString(String string) {
        return new GsonBuilder().create().fromJson(string, SensitiveData.class);
    }
}
