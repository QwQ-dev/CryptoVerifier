package twomillions.other.cryptoverifier.crypto.encryption.rsa;

import lombok.experimental.UtilityClass;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

@UtilityClass
public class RSACryptography {
    private static final int KEY_SIZE = 4096;
    private static final String RSA_ALGORITHM = "RSA";

    public static String encrypt(String plaintext, PublicKey publicKey) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        Cipher cipher = Cipher.getInstance(RSA_ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);

        byte[] plaintextBytes = plaintext.getBytes(StandardCharsets.UTF_8);
        byte[] encryptedBytes = cipher.doFinal(plaintextBytes);

        return Base64.getEncoder().encodeToString(encryptedBytes);
    }

    public static String decrypt(String ciphertext, PrivateKey privateKey) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        Cipher cipher = Cipher.getInstance(RSA_ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, privateKey);

        byte[] ciphertextBytes = Base64.getDecoder().decode(ciphertext);
        byte[] decryptedBytes = cipher.doFinal(ciphertextBytes);

        return new String(decryptedBytes, StandardCharsets.UTF_8);
    }

    public static KeyPair generateKeyPair() throws NoSuchAlgorithmException {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(KEY_SIZE);
        return keyPairGenerator.generateKeyPair();
    }

    public static String encode(PublicKey publicKey) {
        return Base64.getEncoder().encodeToString(publicKey.getEncoded());
    }

    public static String encode(PrivateKey privateKey) {
        return Base64.getEncoder().encodeToString(privateKey.getEncoded());
    }

    public static String encode(KeyPair keyPair) {
        String encodedPublicKey = encode(keyPair.getPublic());
        String encodedPrivateKey = encode(keyPair.getPrivate());

        return encodedPublicKey + ":" + encodedPrivateKey;
    }

    public static PublicKey decodePublicKey(String publicKey) throws NoSuchAlgorithmException, InvalidKeySpecException {
        byte[] publicKeyBytes = Base64.getDecoder().decode(publicKey);
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(publicKeyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return keyFactory.generatePublic(keySpec);
    }

    public static PrivateKey decodePrivateKey(String privateKey) throws NoSuchAlgorithmException, InvalidKeySpecException {
        byte[] privateKeyBytes = Base64.getDecoder().decode(privateKey);
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(privateKeyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return keyFactory.generatePrivate(keySpec);
    }

    public static KeyPair decodeKeyPair(String encodedKeyPair) throws NoSuchAlgorithmException, InvalidKeySpecException {
        String[] keyParts = encodedKeyPair.split(":");

        PublicKey publicKey = decodePublicKey(keyParts[0]);
        PrivateKey privateKey = decodePrivateKey(keyParts[1]);

        return new KeyPair(publicKey, privateKey);
    }
}
