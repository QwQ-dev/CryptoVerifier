package twomillions.other.cryptoverifier.crypto.verifier.validators;

import lombok.NonNull;
import twomillions.other.cryptoverifier.annotations.ValidatorAutoRegister;
import twomillions.other.cryptoverifier.communication.data.CredentialData;
import twomillions.other.cryptoverifier.crypto.verifier.data.ServerVerifierData;
import twomillions.other.cryptoverifier.validators.impl.interfaces.Validator;

@ValidatorAutoRegister
public class TempBanned implements Validator {
    private ServerVerifierData serverVerifierData;

    @Override
    public boolean validate(String ip, CredentialData credentialData, Object clientVerifierDataObject, Object serverVerifierDataObject) {
        serverVerifierData = (ServerVerifierData) serverVerifierDataObject;
        return !serverVerifierData.isTempBanned();
    }

    @Override
    public int getWeight() {
        return 2;
    }

    @Override
    public boolean isIgnoreException() {
        return true;
    }

    @Override
    public @NonNull String getName() {
        return "TempBanned";
    }

    @Override
    public @NonNull String getFailedMessage() {
        return serverVerifierData.getTempBannedInfo();
    }

    @Override
    public String getDescription() {
        return "临时封禁检查";
    }

    @Override
    public Object getServerVerifierData() {
        return null;
    }
}
