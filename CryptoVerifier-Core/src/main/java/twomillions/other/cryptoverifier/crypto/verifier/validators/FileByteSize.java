package twomillions.other.cryptoverifier.crypto.verifier.validators;

import lombok.NonNull;
import twomillions.other.cryptoverifier.annotations.ValidatorAutoRegister;
import twomillions.other.cryptoverifier.communication.data.CredentialData;
import twomillions.other.cryptoverifier.crypto.verifier.data.ServerVerifierData;
import twomillions.other.cryptoverifier.validators.impl.interfaces.Validator;

import java.nio.charset.StandardCharsets;

@ValidatorAutoRegister
public class FileByteSize implements Validator {
    @Override
    public boolean validate(String ip, CredentialData credentialData, Object clientVerifierDataObject, Object serverVerifierDataObject) {
        ServerVerifierData serverVerifierData = (ServerVerifierData) serverVerifierDataObject;

        String file = credentialData.getFile();

        long serverVerifierFileSize = serverVerifierData.getFileSize();
        long clientVerifierFileSize = file.getBytes(StandardCharsets.UTF_8).length;

        return clientVerifierFileSize == serverVerifierFileSize;
    }

    @Override
    public int getWeight() {
        return 4;
    }

    @Override
    public boolean isIgnoreException() {
        return true;
    }

    @Override
    public @NonNull String getName() {
        return "FileByteSize";
    }

    @Override
    public @NonNull String getFailedMessage() {
        return "AUTH_FILE_BYTE_SIZE_ERROR";
    }

    @Override
    public String getDescription() {
        return "字节检查";
    }

    @Override
    public Object getServerVerifierData() {
        return null;
    }
}
