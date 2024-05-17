package twomillions.other.cryptoverifier.crypto.verifier.validators;

import lombok.NonNull;
import twomillions.other.cryptoverifier.annotations.ValidatorAutoRegister;
import twomillions.other.cryptoverifier.communication.data.CredentialData;
import twomillions.other.cryptoverifier.crypto.verifier.data.ServerVerifierData;
import twomillions.other.cryptoverifier.validators.impl.interfaces.Validator;

@ValidatorAutoRegister
public class FileName implements Validator {
    @Override
    public boolean validate(String ip, CredentialData credentialData, Object clientVerifierDataObject, Object serverVerifierDataObject) {
        String fileName = credentialData.getFileName();
        System.out.println(fileName);
        System.out.println(((ServerVerifierData) serverVerifierDataObject).getFileName());
        return ((ServerVerifierData) serverVerifierDataObject).getFileName().equals(fileName);
    }

    @Override
    public int getWeight() {
        return 5;
    }

    @Override
    public boolean isIgnoreException() {
        return true;
    }

    @Override
    public @NonNull String getName() {
        return "FileName";
    }

    @Override
    public @NonNull String getFailedMessage() {
        return "AUTH_FILE_NAME_ERROR";
    }

    @Override
    public String getDescription() {
        return "名称检查";
    }

    @Override
    public Object getServerVerifierData() {
        return null;
    }
}
