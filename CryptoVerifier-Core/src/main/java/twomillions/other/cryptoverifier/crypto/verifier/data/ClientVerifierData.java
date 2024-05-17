package twomillions.other.cryptoverifier.crypto.verifier.data;

import com.google.gson.GsonBuilder;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.apache.commons.io.FileUtils;
import twomillions.other.cryptoverifier.crypto.encryption.aes.AESCryptography;
import twomillions.other.cryptoverifier.crypto.encryption.rsa.RSACryptography;
import twomillions.other.cryptoverifier.io.config.ConfigManager;

import java.io.File;

@Setter
@Accessors(chain = true)
public class ClientVerifierData {
    /**
     * Basic.
     */
    private String uuid = "";
    private String creationDate = "";
    private String verificationInfo = "";

    /**
     * fromString.
     *
     * @param string ClientVerifierData string.
     * @return ClientVerifierData.
     */
    public static ClientVerifierData fromString(String string) throws Exception {
        if (string.startsWith("{")) {
            return new GsonBuilder().create().fromJson(string, ClientVerifierData.class);
        }

        return new GsonBuilder().create().fromJson(AESCryptography.decrypt(string, ConfigManager.getClientAES256SecretKey()), ClientVerifierData.class);
    }

    public String getUuid() throws Exception {
        return RSACryptography.decrypt(uuid, ConfigManager.getUuidPrivateKey());
    }

    public String getCreationDate(ServerVerifierData serverVerifierData) throws Exception {
        return RSACryptography.decrypt(creationDate, RSACryptography.decodePrivateKey(serverVerifierData.getCreationDateDecryptionPrivateKey()));
    }

    public String getVerificationInfo(ServerVerifierData serverVerifierData) throws Exception {
        return RSACryptography.decrypt(verificationInfo, RSACryptography.decodePrivateKey(serverVerifierData.getVerificationInfoDecryptionPrivateKey()));
    }

    /**
     * Create file.
     */
    public File createFile(String fileName, String filePath) {
        try {
            File file = new File(filePath, fileName);
            FileUtils.writeStringToFile(file, AESCryptography.encrypt(toString(), ConfigManager.getClientAES256SecretKey()), "UTF-8");
            return file;
        } catch (Exception exception) {
            return null;
        }
    }

    /**
     * toString.
     *
     * @return ClientVerifierData string
     */
    @Override
    public String toString() {
        return new GsonBuilder().create().toJson(this);
    }
}
