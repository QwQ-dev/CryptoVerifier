package twomillions.other.cryptoverifier.crypto.verifier.validators;

import lombok.NonNull;
import twomillions.other.cryptoverifier.annotations.ValidatorAutoRegister;
import twomillions.other.cryptoverifier.communication.data.CredentialData;
import twomillions.other.cryptoverifier.crypto.verifier.data.ClientVerifierData;
import twomillions.other.cryptoverifier.crypto.verifier.data.ServerVerifierData;
import twomillions.other.cryptoverifier.validators.impl.interfaces.Validator;

@ValidatorAutoRegister
public class CreationData implements Validator {
    @Override
    public boolean validate(String ip, CredentialData credentialData, Object clientVerifierDataObject, Object serverVerifierDataObject) throws Exception {
        ServerVerifierData serverVerifierData = (ServerVerifierData) serverVerifierDataObject;

        String serverCreationData = serverVerifierData.getCreationDate();
        String clientCreationData = ((ClientVerifierData) clientVerifierDataObject).getCreationDate(serverVerifierData);

        return serverCreationData.equalsIgnoreCase(clientCreationData);
    }

    @Override
    public int getWeight() {
        return 6;
    }

    @Override
    public boolean isIgnoreException() {
        return true;
    }

    @Override
    public @NonNull String getName() {
        return "CreationData";
    }

    @Override
    public @NonNull String getFailedMessage() {
        return "AUTH_FILE_CREATION_DATE_ERROR";
    }

    @Override
    public String getDescription() {
        return "创建时间检查";
    }

    @Override
    public Object getServerVerifierData() {
        return null;
    }
}
