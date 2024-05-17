package twomillions.other.cryptoverifier.crypto.encryption.aes;

import lombok.experimental.UtilityClass;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;

/**
 * AES 实用类。
 *
 * <p>
 * 该类提供了使用 AES 算法进行文本加密和解密的方法，生成 AES 密钥，编码和解码密钥等功能。
 * </p>
 *
 * @author 2000000
 * @version 1.2
 * @since 2023/8/12
 */
@UtilityClass
public class AESCryptography {
    /**
     * 密钥长度。
     */
    private static final int SIZE = 256;
    /**
     * 算法。
     */
    private static final String ALGORITHM = "AES";
    /**
     * 加密模式和填充方式。
     */
    private static final String CIPHER_TRANSFORMATION = "AES/ECB/PKCS5Padding";

    /**
     * 对明文进行加密。
     *
     * @param plaintext 明文字符串
     * @param secretKey 密钥
     * @return 加密后的密文字符串
     * @throws Exception 如果加密过程中发生异常
     */
    public static String encrypt(String plaintext, SecretKey secretKey) throws Exception {
        Cipher cipher = Cipher.getInstance(CIPHER_TRANSFORMATION);
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);

        byte[] encryptedBytes = cipher.doFinal(plaintext.getBytes(StandardCharsets.UTF_8));

        return Base64.getEncoder().encodeToString(encryptedBytes);
    }

    /**
     * 对密文进行解密。
     *
     * @param ciphertext 密文字符串
     * @param secretKey  密钥
     * @return 解密后的明文字符串
     * @throws Exception 如果解密过程中发生异常
     */
    public static String decrypt(String ciphertext, SecretKey secretKey) throws Exception {
        Cipher cipher = Cipher.getInstance(CIPHER_TRANSFORMATION);
        cipher.init(Cipher.DECRYPT_MODE, secretKey);

        byte[] decodedBytes = Base64.getDecoder().decode(ciphertext);
        byte[] decryptedBytes = cipher.doFinal(decodedBytes);

        return new String(decryptedBytes, StandardCharsets.UTF_8);
    }

    /**
     * 生成一个随机的 AES 密钥。
     *
     * @return 生成的密钥
     * @throws NoSuchAlgorithmException 如果所请求的算法不可用
     */
    public static SecretKey generateSecretKey() throws NoSuchAlgorithmException {
        KeyGenerator keyGenerator = KeyGenerator.getInstance(ALGORITHM);
        keyGenerator.init(SIZE);
        return keyGenerator.generateKey();
    }

    /**
     * 将密钥编码为字符串。
     *
     * @param secretKey 密钥
     * @return 编码后的密钥字符串
     */
    public static String encode(SecretKey secretKey) {
        byte[] encodedKey = secretKey.getEncoded();
        return Base64.getEncoder().encodeToString(encodedKey);
    }

    /**
     * 将密钥字符串解码为密钥。
     *
     * @param secretKeyString 密钥字符串
     * @return 解码后的密钥
     */
    public static SecretKey decode(String secretKeyString) {
        byte[] decodedKey = Base64.getDecoder().decode(secretKeyString);
        return new SecretKeySpec(decodedKey, ALGORITHM);
    }

    /**
     * 根据共享密钥生成一个派生的 AES 密钥。
     *
     * @param sharedSecret 共享密钥字符串
     * @return 生成的派生密钥
     * @throws NoSuchAlgorithmException 如果所请求的算法不可用
     * @throws InvalidKeySpecException  如果提供的密钥规范无效
     */
    public static SecretKey generateSecretKey(String sharedSecret) throws NoSuchAlgorithmException, InvalidKeySpecException {
        return new SecretKeySpec(SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256")
                .generateSecret(new PBEKeySpec(sharedSecret.toCharArray(), new byte[16], 10000, SIZE)).getEncoded(), ALGORITHM);
    }
}
