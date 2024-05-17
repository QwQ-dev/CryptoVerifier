package twomillions.other.cryptoverifier.crypto.verifier.validators;

import lombok.NonNull;
import twomillions.other.cryptoverifier.annotations.ValidatorAutoRegister;
import twomillions.other.cryptoverifier.communication.data.CredentialData;
import twomillions.other.cryptoverifier.crypto.verifier.data.ServerVerifierData;
import twomillions.other.cryptoverifier.validators.impl.interfaces.Validator;

import java.util.Set;

@ValidatorAutoRegister
public class IpLimit implements Validator {
    private ServerVerifierData serverVerifierData;

    @Override
    public boolean validate(String ip, CredentialData credentialData, Object clientVerifierDataObject, Object serverVerifierDataObject) throws Exception {
        ServerVerifierData oldServerVerifierData = (ServerVerifierData) serverVerifierDataObject;

        int ipLimit = oldServerVerifierData.getIpLimit();
        Set<String> ips = oldServerVerifierData.getIps();

        if (ipLimit > 0 && !ips.contains(ip)) {
            if (ips.size() + 1 > ipLimit) {
                return false;
            }

            ips.add(ip);
            oldServerVerifierData.setHwids(ips);
            serverVerifierData = oldServerVerifierData;
        }

        return true;
    }

    @Override
    public int getWeight() {
        return 10;
    }

    @Override
    public boolean isIgnoreException() {
        return true;
    }

    @Override
    public @NonNull String getName() {
        return "IpLimit";
    }

    @Override
    public @NonNull String getFailedMessage() {
        return "IP_LIMIT";
    }

    @Override
    public String getDescription() {
        return "IP 限制检查";
    }

    @Override
    public Object getServerVerifierData() {
        return serverVerifierData;
    }
}
