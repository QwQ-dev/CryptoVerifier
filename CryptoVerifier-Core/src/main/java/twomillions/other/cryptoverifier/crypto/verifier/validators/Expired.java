package twomillions.other.cryptoverifier.crypto.verifier.validators;

import lombok.Getter;
import lombok.NonNull;
import twomillions.other.cryptoverifier.annotations.ValidatorAutoRegister;
import twomillions.other.cryptoverifier.communication.data.CredentialData;
import twomillions.other.cryptoverifier.crypto.verifier.data.ServerVerifierData;
import twomillions.other.cryptoverifier.io.yaml.YamlManager;
import twomillions.other.cryptoverifier.validators.impl.interfaces.Validator;

@Getter
@ValidatorAutoRegister
public class Expired implements Validator {
    private boolean deleteData = false;

    @Override
    public boolean validate(String ip, CredentialData credentialData, Object clientVerifierDataObject, Object serverVerifierDataObject) throws Exception {
        ServerVerifierData serverVerifierData = ((ServerVerifierData) serverVerifierDataObject);

        long serverVerifierFileExpirationDate = Long.parseLong(serverVerifierData.getExpirationDate());

        if (serverVerifierFileExpirationDate > 0 && serverVerifierFileExpirationDate < System.currentTimeMillis()) {
            deleteData = YamlManager.getConfig().getBoolean("EXPIRED-DELETE");
            return false;
        }

        return true;
    }

    @Override
    public int getWeight() {
        return 8;
    }

    @Override
    public boolean isIgnoreException() {
        return true;
    }

    @Override
    public @NonNull String getName() {
        return "Expired";
    }

    @Override
    public @NonNull String getFailedMessage() {
        return "AUTH_FILE_EXPIRED";
    }

    @Override
    public String getDescription() {
        return "过期时间检查";
    }

    @Override
    public Object getServerVerifierData() {
        return deleteData ? "" : null;
    }
}
