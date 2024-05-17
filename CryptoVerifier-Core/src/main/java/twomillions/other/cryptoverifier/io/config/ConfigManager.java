package twomillions.other.cryptoverifier.io.config;

import lombok.experimental.UtilityClass;
import twomillions.other.cryptoverifier.crypto.encryption.aes.AESCryptography;
import twomillions.other.cryptoverifier.crypto.encryption.hash.HashUtils;
import twomillions.other.cryptoverifier.crypto.encryption.rsa.RSACryptography;
import twomillions.other.cryptoverifier.io.yaml.YamlManager;
import twomillions.other.cryptoverifier.util.LoggerUtils;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.util.Base64;

@UtilityClass
public class ConfigManager {
    public static void init() throws Exception {
        initGlobalSecretKey();
        getUuidKeyPair();
        getClientAES256SecretKey();
    }

    public static void initGlobalSecretKey() throws NoSuchAlgorithmException {
        String secretKey;
        String configKey = YamlManager.getConfig().getString("SECURE-FIELD");

        LoggerUtils.getLogger().info("开始初始化动态加密字段。");

        if (configKey.isEmpty()) {
            secretKey = createConfigKey();
        } else {
            String[] base64DecodeSplit = new String(Base64.getDecoder().decode(configKey.getBytes(StandardCharsets.UTF_8)), StandardCharsets.UTF_8).split("\\|");
            int indexSplit = Integer.parseInt(base64DecodeSplit[0]);
            secretKey = createConfigKey(base64DecodeSplit[1].substring(indexSplit, indexSplit + 44));
        }

        YamlManager.getConfig().set("SECURE-FIELD", secretKey);

        LoggerUtils.getLogger().info("初始化动态加密字段完成。");
    }

    public static SecretKey getGlobalSecretKey() throws NoSuchAlgorithmException {
        String configKey = YamlManager.getConfig().getString("SECURE-FIELD");

        if (configKey.isEmpty()) {
            configKey = createConfigKey();
            YamlManager.getConfig().set("SECURE-FIELD", configKey);
        }

        String[] base64DecodeSplit = new String(Base64.getDecoder().decode(configKey.getBytes(StandardCharsets.UTF_8)), StandardCharsets.UTF_8).split("\\|");
        int indexSplit = Integer.parseInt(base64DecodeSplit[0]);

        return AESCryptography.decode(base64DecodeSplit[1].substring(indexSplit, indexSplit + 44));
    }

    public static PublicKey getUuidPublicKey() throws Exception {
        return getUuidKeyPair().getPublic();
    }

    public static PrivateKey getUuidPrivateKey() throws Exception {
        return getUuidKeyPair().getPrivate();
    }

    public static KeyPair getUuidKeyPair() throws Exception {
        String configUuidKeyPair = YamlManager.getConfig().getString("UUID-KEY-PAIR");

        if (configUuidKeyPair.isEmpty()) {
            String newKeyPair = AESCryptography.encrypt(RSACryptography.encode(RSACryptography.generateKeyPair()), getGlobalSecretKey());
            YamlManager.getConfig().set("UUID-KEY-PAIR", newKeyPair);
            configUuidKeyPair = newKeyPair;
        }

        return RSACryptography.decodeKeyPair(AESCryptography.decrypt(configUuidKeyPair, getGlobalSecretKey()));
    }

    public static SecretKey getClientAES256SecretKey() throws Exception {
        String configAES256SecretKey = YamlManager.getConfig().getString("AES-256-SECRET-KEY");

        if (configAES256SecretKey.isEmpty()) {
            String newSecretKey = AESCryptography.encrypt(AESCryptography.encode(AESCryptography.generateSecretKey()), getGlobalSecretKey());
            YamlManager.getConfig().set("AES-256-SECRET-KEY", newSecretKey);
            configAES256SecretKey = newSecretKey;
        }

        return AESCryptography.decode(AESCryptography.decrypt(configAES256SecretKey, getGlobalSecretKey()));
    }

    private static String createConfigKey() throws NoSuchAlgorithmException {
        String aes = AESCryptography.encode(AESCryptography.generateSecretKey());
        int index = new SecureRandom().nextInt(123412 - 2000 + 1) + 2000;
        return Base64.getEncoder().encodeToString((index + "|" + new StringBuilder(HashUtils.generateNewString(123412)).insert(index, aes)).getBytes(StandardCharsets.UTF_8));
    }

    private static String createConfigKey(String aes) {
        int index = new SecureRandom().nextInt(123412 - 2000 + 1) + 2000;
        return Base64.getEncoder().encodeToString((index + "|" + new StringBuilder(HashUtils.generateNewString(123412)).insert(index, aes)).getBytes(StandardCharsets.UTF_8));
    }
}
