package twomillions.other.cryptoverifier.crypto.verifier.validators;

import lombok.NonNull;
import twomillions.other.cryptoverifier.annotations.ValidatorAutoRegister;
import twomillions.other.cryptoverifier.communication.data.CredentialData;
import twomillions.other.cryptoverifier.crypto.verifier.data.ServerVerifierData;
import twomillions.other.cryptoverifier.validators.impl.interfaces.Validator;

import java.util.Set;

@ValidatorAutoRegister
public class HwidLimit implements Validator {
    private ServerVerifierData serverVerifierData;

    @Override
    public boolean validate(String ip, CredentialData credentialData, Object clientVerifierDataObject, Object serverVerifierDataObject) throws Exception {
        ServerVerifierData oldServerVerifierData = (ServerVerifierData) serverVerifierDataObject;

        String hwid = credentialData.getHwid();

        int hwidLimit = oldServerVerifierData.getHwidLimit();
        Set<String> hwids = oldServerVerifierData.getHwids();

        if (hwidLimit > 0 && !hwids.contains(hwid)) {
            if (hwids.size() + 1 > hwidLimit) {
                return false;
            }

            hwids.add(hwid);
            oldServerVerifierData.setHwids(hwids);
            serverVerifierData = oldServerVerifierData;
        }

        return true;
    }

    @Override
    public int getWeight() {
        return 9;
    }

    @Override
    public boolean isIgnoreException() {
        return true;
    }

    @Override
    public @NonNull String getName() {
        return "HwidLimit";
    }

    @Override
    public @NonNull String getFailedMessage() {
        return "HWID_LIMIT";
    }

    @Override
    public String getDescription() {
        return "硬件特征码限制检查";
    }

    @Override
    public Object getServerVerifierData() {
        return serverVerifierData;
    }
}
