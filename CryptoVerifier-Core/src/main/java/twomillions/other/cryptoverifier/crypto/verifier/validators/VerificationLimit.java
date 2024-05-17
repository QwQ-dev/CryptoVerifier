package twomillions.other.cryptoverifier.crypto.verifier.validators;

import lombok.NonNull;
import twomillions.other.cryptoverifier.annotations.ValidatorAutoRegister;
import twomillions.other.cryptoverifier.communication.data.CredentialData;
import twomillions.other.cryptoverifier.crypto.verifier.data.ServerVerifierData;
import twomillions.other.cryptoverifier.security.detectors.ParallelLimitDetector;
import twomillions.other.cryptoverifier.validators.impl.interfaces.Validator;

import java.util.Map;

@ValidatorAutoRegister
public class VerificationLimit implements Validator {
    private ServerVerifierData serverVerifierData;

    @Override
    public boolean validate(String ip, CredentialData credentialData, Object clientVerifierDataObject, Object serverVerifierDataObject) throws Exception {
        ServerVerifierData oldServerVerifierData = (ServerVerifierData) serverVerifierDataObject;

        int verificationLimit = oldServerVerifierData.getVerificationLimit();
        int verificationTimes = oldServerVerifierData.getVerificationTimes();

        // 如果并行的库类没有找到该 IP 那么算作一次验证 添加次数
        Map<Object, Object> map = ParallelLimitDetector.getParallelLimitDetector().getUuidParallelData(oldServerVerifierData.getUuid());

        if (!map.containsKey(ip)) {
            verificationTimes = verificationTimes + 1;
            oldServerVerifierData.setVerificationTimes(verificationTimes);
            serverVerifierData = oldServerVerifierData;
        }

        return verificationLimit <= 0 || verificationTimes <= verificationLimit;
    }

    @Override
    public int getWeight() {
        return 11;
    }

    @Override
    public boolean isIgnoreException() {
        return true;
    }

    @Override
    public @NonNull String getName() {
        return "VerificationLimit";
    }

    @Override
    public @NonNull String getFailedMessage() {
        return "VERIFICATION_LIMIT";
    }

    @Override
    public String getDescription() {
        return "使用次数检查";
    }

    @Override
    public Object getServerVerifierData() {
        return serverVerifierData;
    }
}
