package twomillions.other.cryptoverifier.crypto.verifier.validators;

import lombok.NonNull;
import twomillions.other.cryptoverifier.annotations.ValidatorAutoRegister;
import twomillions.other.cryptoverifier.communication.data.CredentialData;
import twomillions.other.cryptoverifier.security.detectors.FastVerifierDetector;
import twomillions.other.cryptoverifier.validators.impl.interfaces.Validator;

@ValidatorAutoRegister
public class BlockIP implements Validator {
    @Override
    public boolean validate(String ip, CredentialData credentialData, Object clientVerifierDataObject, Object serverVerifierDataObject) {
        FastVerifierDetector.getFastVerifierDetector().processRequest(ip);
        return !FastVerifierDetector.getFastVerifierDetector().getBlockIps().contains(ip);
    }

    @Override
    public int getWeight() {
        return 1;
    }

    @Override
    public boolean isIgnoreException() {
        return true;
    }

    @Override
    public @NonNull String getName() {
        return "BlockIP";
    }

    @Override
    public @NonNull String getFailedMessage() {
        return "BLOCKED_IP";
    }

    @Override
    public String getDescription() {
        return "速率黑名单检查";
    }

    @Override
    public Object getServerVerifierData() {
        return null;
    }
}
