package twomillions.other.cryptoverifier.crypto.verifier.validators;

import lombok.NonNull;
import twomillions.other.cryptoverifier.annotations.ValidatorAutoRegister;
import twomillions.other.cryptoverifier.communication.data.CredentialData;
import twomillions.other.cryptoverifier.crypto.verifier.data.ClientVerifierData;
import twomillions.other.cryptoverifier.crypto.verifier.data.ServerVerifierData;
import twomillions.other.cryptoverifier.validators.impl.interfaces.Validator;

@ValidatorAutoRegister
public class VerificationInfo implements Validator {
    @Override
    public boolean validate(String ip, CredentialData credentialData, Object clientVerifierDataObject, Object serverVerifierDataObject) throws Exception {
        ServerVerifierData serverVerifierData = (ServerVerifierData) serverVerifierDataObject;

        String serverVerificationInfo = serverVerifierData.getVerificationInfo();
        String clientVerificationInfo = ((ClientVerifierData) (clientVerifierDataObject)).getVerificationInfo(serverVerifierData);

        return serverVerificationInfo.equalsIgnoreCase(clientVerificationInfo);
    }

    @Override
    public int getWeight() {
        return 7;
    }

    @Override
    public boolean isIgnoreException() {
        return true;
    }

    @Override
    public @NonNull String getName() {
        return "VerificationInfo";
    }

    @Override
    public @NonNull String getFailedMessage() {
        return "AUTH_FILE_VALIDATION_INFO_ERROR";
    }

    @Override
    public String getDescription() {
        return "验证信息检查";
    }

    @Override
    public Object getServerVerifierData() {
        return null;
    }
}
