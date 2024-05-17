package twomillions.other.cryptoverifier.crypto.verifier.validators;

import lombok.NonNull;
import twomillions.other.cryptoverifier.annotations.ValidatorAutoRegister;
import twomillions.other.cryptoverifier.communication.data.CredentialData;
import twomillions.other.cryptoverifier.crypto.verifier.data.ServerVerifierData;
import twomillions.other.cryptoverifier.io.yaml.YamlManager;
import twomillions.other.cryptoverifier.security.detectors.ParallelLimitDetector;
import twomillions.other.cryptoverifier.validators.impl.interfaces.Validator;

import java.util.Map;

@ValidatorAutoRegister
public class ParallelLimit implements Validator {
    private ServerVerifierData serverVerifierData;

    @Override
    public boolean validate(String ip, CredentialData credentialData, Object clientVerifierDataObject, Object serverVerifierDataObject) throws Exception {
        ServerVerifierData oldServerVerifierData = (ServerVerifierData) serverVerifierDataObject;
        String serverVerifierDataUuid = oldServerVerifierData.getUuid();

        int parallelLimit = oldServerVerifierData.getParallelLimit();
        Map<Object, Object> map = ParallelLimitDetector.getParallelLimitDetector().getUuidParallelData(serverVerifierDataUuid);

        if (parallelLimit != 0 && !map.containsKey(ip) && map.keySet().size() + 1 > parallelLimit) {
            return false;
        }

        map.put(ip, System.currentTimeMillis() + YamlManager.getConfig().getLong("PARALLEL-OFFSET-TIME-THRESHOLD"));
        ParallelLimitDetector.getParallelLimitDetector().updateUuidParallelData(serverVerifierDataUuid, map);

        serverVerifierData = oldServerVerifierData.setLastUsedTime(String.valueOf(System.currentTimeMillis()));

        return true;
    }

    @Override
    public int getWeight() {
        return Integer.MAX_VALUE;
    }

    @Override
    public boolean isIgnoreException() {
        return true;
    }

    @Override
    public @NonNull String getName() {
        return "ParallelLimit";
    }

    @Override
    public @NonNull String getFailedMessage() {
        return "PARALLEL_LIMIT";
    }

    @Override
    public String getDescription() {
        return "并行限制检查";
    }

    @Override
    public Object getServerVerifierData() {
        return serverVerifierData;
    }
}
