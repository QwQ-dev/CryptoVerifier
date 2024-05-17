package twomillions.other.cryptoverifier.crypto.verifier.validators;

import lombok.NonNull;
import twomillions.other.cryptoverifier.annotations.ValidatorAutoRegister;
import twomillions.other.cryptoverifier.crypto.encryption.hash.HashUtils;
import twomillions.other.cryptoverifier.communication.data.CredentialData;
import twomillions.other.cryptoverifier.crypto.verifier.data.ServerVerifierData;
import twomillions.other.cryptoverifier.validators.impl.interfaces.Validator;

@ValidatorAutoRegister
public class Hash implements Validator {
    @Override
    public boolean validate(String ip, CredentialData credentialData, Object clientVerifierDataObject, Object serverVerifierDataObject) {
        ServerVerifierData serverVerifierData = (ServerVerifierData) serverVerifierDataObject;

        String file = credentialData.getFile();

        String clientVerifierFileHash = HashUtils.sha256Hex(file);
        String serverVerifierFileHash = serverVerifierData.getHash();

        return serverVerifierFileHash.equalsIgnoreCase(clientVerifierFileHash);
    }

    @Override
    public int getWeight() {
        return 3;
    }

    @Override
    public boolean isIgnoreException() {
        return true;
    }

    @Override
    public @NonNull String getName() {
        return "Hash";
    }

    @Override
    public @NonNull String getFailedMessage() {
        return "AUTH_FILE_HASH_ERROR";
    }

    @Override
    public String getDescription() {
        return "哈希检查";
    }

    @Override
    public Object getServerVerifierData() {
        return null;
    }
}
