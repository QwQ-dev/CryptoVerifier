package twomillions.other.cryptoverifier.crypto.encryption.hash;

import lombok.experimental.UtilityClass;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.RandomStringUtils;

import java.io.File;
import java.io.IOException;
import java.security.SecureRandom;

@UtilityClass
public class HashUtils {
    public static String sha256Hex(File file) throws IOException {
        return DigestUtils.sha256Hex(FileUtils.readFileToByteArray(file));
    }

    public static String sha256Hex(String string) {
        return DigestUtils.sha256Hex(string);
    }

    public static String generateNewString(int length) {
        return RandomStringUtils.random(length, 0, 0, true, true, null, new SecureRandom());
    }
}