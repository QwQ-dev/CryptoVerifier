package twomillions.other.cryptoverifier.crypto.verifier.data;

import com.google.common.collect.Sets;
import com.google.gson.GsonBuilder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import twomillions.other.cryptoverifier.crypto.encryption.aes.AESCryptography;
import twomillions.other.cryptoverifier.io.config.ConfigManager;

import java.util.Set;

@Getter
@Setter
@Accessors(chain = true)
public class ServerVerifierData {
    /**
     * Basic.
     */
    private String uuid = "";
    private String lastUsedTime = "";

    /**
     * Ban.
     */
    private boolean tempBanned = false;
    private String tempBannedInfo = "";

    /**
     * Hwid limit.
     */
    private int hwidLimit = 0;
    private Set<String> hwids = Sets.newConcurrentHashSet();

    /**
     * IP limit.
     */
    private int ipLimit = 0;
    private Set<String> ips = Sets.newConcurrentHashSet();

    /**
     * Verification limit.
     */
    private int verificationLimit = 0;
    private int verificationTimes = 0;

    /**
     * Parallel limit.
     */
    private int parallelLimit = 0;

    /**
     * Creation date.
     */
    private String creationDate = "";
    private String creationDateDecryptionPrivateKey = "";

    /**
     * File size.
     */
    private long fileSize = 0;

    /**
     * File name.
     */
    private String fileName = "";

    /**
     * Verification info.
     */
    private String verificationInfo = "";
    private String verificationInfoDecryptionPrivateKey = "";

    /**
     * Hash.
     */
    private String hash = "";

    /**
     * Expiration date.
     */
    private String expirationDate = "";

    /**
     * Custom data.
     */
    private String customData = "";
    private String clientSecureCustomDataDecryptionPrivateKey = "";

    /**
     * fromString.
     *
     * @param string ServerVerifierData string.
     * @return ServerVerifierData.
     */
    public static ServerVerifierData fromString(String string) throws Exception {
        if (string.startsWith("{")) {
            return new GsonBuilder().create().fromJson(string, ServerVerifierData.class);
        }

        return new GsonBuilder().create().fromJson(AESCryptography.decrypt(string, ConfigManager.getGlobalSecretKey()), ServerVerifierData.class);
    }

    /**
     * toString.
     *
     * @return ServerVerifierData string
     */
    @Override
    public String toString() {
        try {
            return AESCryptography.encrypt(new GsonBuilder().create().toJson(this), ConfigManager.getGlobalSecretKey());
        } catch (Exception exception) {
            return null;
        }
    }
}
