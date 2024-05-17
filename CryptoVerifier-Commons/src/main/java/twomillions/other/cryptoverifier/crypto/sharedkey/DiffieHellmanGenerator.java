package twomillions.other.cryptoverifier.crypto.sharedkey;

import javax.crypto.KeyAgreement;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

/**
 * Diffie-Hellman 实用类。
 *
 * <p>
 * 该类提供了用于生成 Diffie-Hellman 密钥对、根据密钥对生成共享密钥，以及将公钥、私钥、密钥对编码和解码等功能。
 * </p>
 *
 * @author 2000000
 * @version 1.2
 * @since 2023/8/12
 */
@SuppressWarnings("unused")
public class DiffieHellmanGenerator {
    /**
     * 算法。
     */
    private static final String ALGORITHM = "DH";
    /**
     * 密钥长度。
     */
    private static final int KEY_SIZE = 2048;

    /**
     * 生成一个 Diffie-Hellman 密钥对。
     *
     * @return 生成的密钥对
     * @throws NoSuchAlgorithmException 如果所请求的算法不可用
     */
    public static KeyPair generateKeyPair() throws NoSuchAlgorithmException {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(ALGORITHM);
        keyPairGenerator.initialize(KEY_SIZE);
        return keyPairGenerator.generateKeyPair();
    }

    /**
     * 根据公钥和私钥生成共享密钥。
     *
     * @param publicKeyBytes  公钥字节数组
     * @param privateKeyBytes 私钥字节数组
     * @return 生成的共享密钥的 Base64 编码字符串
     * @throws NoSuchAlgorithmException 如果所请求的算法不可用
     * @throws InvalidKeySpecException 如果提供的密钥规范无效
     * @throws InvalidKeyException 如果提供的密钥无效
     */
    public static String generateSharedKey(byte[] publicKeyBytes, byte[] privateKeyBytes) throws NoSuchAlgorithmException, InvalidKeySpecException, InvalidKeyException {
        X509EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(publicKeyBytes);
        KeyFactory publicKeyFactory = KeyFactory.getInstance(ALGORITHM);
        PublicKey publicKey = publicKeyFactory.generatePublic(publicKeySpec);

        PKCS8EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(privateKeyBytes);
        KeyFactory privateKeyFactory = KeyFactory.getInstance(ALGORITHM);
        PrivateKey privateKey = privateKeyFactory.generatePrivate(privateKeySpec);

        KeyAgreement keyAgreement = KeyAgreement.getInstance(ALGORITHM);
        keyAgreement.init(privateKey);
        keyAgreement.doPhase(publicKey, true);

        return Base64.getEncoder().encodeToString(keyAgreement.generateSecret());
    }

    /**
     * 将公钥编码为字符串。
     *
     * @param publicKey 公钥
     * @return 编码后的公钥字符串
     */
    public static String encode(PublicKey publicKey) {
        byte[] encodedKey = publicKey.getEncoded();
        return Base64.getEncoder().encodeToString(encodedKey);
    }

    /**
     * 将私钥编码为字符串。
     *
     * @param privateKey 私钥
     * @return 编码后的私钥字符串
     */
    public static String encode(PrivateKey privateKey) {
        byte[] encodedKey = privateKey.getEncoded();
        return Base64.getEncoder().encodeToString(encodedKey);
    }

    /**
     * 将密钥对编码为字符串。
     *
     * @param keyPair 密钥对
     * @return 编码后的密钥对字符串
     */
    public static String encode(KeyPair keyPair) {
        String encodedPublicKey = encode(keyPair.getPublic());
        String encodedPrivateKey = encode(keyPair.getPrivate());

        return encodedPublicKey + ":" + encodedPrivateKey;
    }

    /**
     * 将字符串解码为公钥。
     *
     * @param publicKey 公钥的 Base64 字符串
     * @return 解码后的公钥
     * @throws NoSuchAlgorithmException 如果所请求的算法不可用
     * @throws InvalidKeySpecException 如果提供的密钥规范无效
     */
    public static PublicKey decodePublicKey(String publicKey) throws NoSuchAlgorithmException, InvalidKeySpecException {
        byte[] publicKeyBytes = Base64.getDecoder().decode(publicKey);
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(publicKeyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM);
        return keyFactory.generatePublic(keySpec);
    }

    /**
     * 将字符串解码为私钥。
     *
     * @param privateKey 私钥的 Base64 字符串
     * @return 解码后的私钥
     * @throws NoSuchAlgorithmException 如果所请求的算法不可用
     * @throws InvalidKeySpecException 如果提供的密钥规范无效
     */
    public static PrivateKey decodePrivateKey(String privateKey) throws NoSuchAlgorithmException, InvalidKeySpecException {
        byte[] privateKeyBytes = Base64.getDecoder().decode(privateKey);
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(privateKeyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM);
        return keyFactory.generatePrivate(keySpec);
    }

    /**
     * 将字符串解码为密钥对。
     *
     * @param encodedKeyPair 编码的密钥对字符串
     * @return 解码后的密钥对
     * @throws NoSuchAlgorithmException 如果所请求的算法不可用
     * @throws InvalidKeySpecException 如果提供的密钥规范无效
     */
    public static KeyPair decodeKeyPair(String encodedKeyPair) throws NoSuchAlgorithmException, InvalidKeySpecException {
        String[] keyParts = encodedKeyPair.split(":");

        PublicKey publicKey = decodePublicKey(keyParts[0]);
        PrivateKey privateKey = decodePrivateKey(keyParts[1]);

        return new KeyPair(publicKey, privateKey);
    }
}
