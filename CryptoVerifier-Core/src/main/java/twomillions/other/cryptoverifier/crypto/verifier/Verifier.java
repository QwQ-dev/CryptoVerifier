package twomillions.other.cryptoverifier.crypto.verifier;

import org.apache.commons.io.FileUtils;
import twomillions.other.cryptoverifier.annotations.AnnotationProcessor;
import twomillions.other.cryptoverifier.annotations.CommandAutoRegister;
import twomillions.other.cryptoverifier.annotations.ValidatorAutoRegister;
import twomillions.other.cryptoverifier.commands.impl.CommandManager;
import twomillions.other.cryptoverifier.commands.impl.interfaces.Command;
import twomillions.other.cryptoverifier.communication.ValidationInfoData;
import twomillions.other.cryptoverifier.communication.data.CredentialData;
import twomillions.other.cryptoverifier.crypto.encryption.hash.HashUtils;
import twomillions.other.cryptoverifier.crypto.encryption.rsa.RSACryptography;
import twomillions.other.cryptoverifier.crypto.verifier.data.ClientVerifierData;
import twomillions.other.cryptoverifier.crypto.verifier.data.ServerVerifierData;
import twomillions.other.cryptoverifier.crypto.verifier.server.SocketVerifierServer;
import twomillions.other.cryptoverifier.events.custom.CreateLicenseEvent;
import twomillions.other.cryptoverifier.events.custom.LicenseValidationCompleteEvent;
import twomillions.other.cryptoverifier.events.impl.EventManager;
import twomillions.other.cryptoverifier.exception.VerifierException;
import twomillions.other.cryptoverifier.io.config.ConfigManager;
import twomillions.other.cryptoverifier.io.databases.DataManager;
import twomillions.other.cryptoverifier.io.yaml.YamlManager;
import twomillions.other.cryptoverifier.util.LoggerUtils;
import twomillions.other.cryptoverifier.validators.impl.ValidatorManager;
import twomillions.other.cryptoverifier.validators.impl.interfaces.Validator;
import twomillions.other.cryptoverifier.validators.impl.objects.ValidatorResult;

import java.io.File;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.List;

public class Verifier {
    public static void initVerifierServer() throws Exception {
        registerValidators();
        registerCommands();
        new SocketVerifierServer().start();
    }

    private static void registerValidators() throws Exception {
        ValidatorManager validatorManager = ValidatorManager.getValidatorManager();

        for (Class<?> clazz : AnnotationProcessor.getClassesWithAnnotation("twomillions.other.cryptoverifier", ValidatorAutoRegister.class)) {
            validatorManager.registerValidator((Validator) clazz.newInstance());
        }
    }

    private static void registerCommands() throws Exception {
        CommandManager commandManager = CommandManager.getCommandManager();

        for (Class<?> clazz : AnnotationProcessor.getClassesWithAnnotation("twomillions.other.cryptoverifier", CommandAutoRegister.class)) {
            commandManager.registerCommand((Command) clazz.newInstance());
        }
    }

    public static boolean create(String uuid, String info, int hwidLimit, int ipLimit, int verificationLimit, int parallelLimit, long expirationData, String clientFileName)
            throws VerifierException {
        try {
            List<String> uuids = DataManager.get();

            if (uuids == null) {
                return false;
            }

            if (uuids.contains(uuid)) {
                LoggerUtils.getLogger().error("创建重复的 UUID 许可证! UUID: {}", uuid);
                return false;
            }

            KeyPair timeKeyPair = RSACryptography.generateKeyPair();
            KeyPair infoKeyPair = RSACryptography.generateKeyPair();

            PublicKey timePublicKey = timeKeyPair.getPublic();
            PublicKey infoPublicKey = infoKeyPair.getPublic();

            PrivateKey timePrivateKey = timeKeyPair.getPrivate();
            PrivateKey infoPrivateKey = infoKeyPair.getPrivate();

            String time = String.valueOf(System.currentTimeMillis());

            String encryptedUuid = RSACryptography.encrypt(uuid, ConfigManager.getUuidPublicKey());
            String encryptedTime = RSACryptography.encrypt(time, timePublicKey);
            String encryptedInfo = RSACryptography.encrypt(info, infoPublicKey);

            String encodedTimePrivateKey = RSACryptography.encode(timePrivateKey);
            String encodedInfoPrivateKey = RSACryptography.encode(infoPrivateKey);

            ClientVerifierData clientVerifierData = new ClientVerifierData()
                    .setUuid(encryptedUuid)
                    .setCreationDate(encryptedTime)
                    .setVerificationInfo(encryptedInfo);

            File clientFile = clientVerifierData.createFile(clientFileName, "clientFiles");

            ServerVerifierData serverVerifierData = new ServerVerifierData()
                    .setUuid(uuid)
                    .setHwidLimit(hwidLimit)
                    .setIpLimit(ipLimit)
                    .setVerificationLimit(verificationLimit)
                    .setParallelLimit(parallelLimit)
                    .setCreationDate(time)
                    .setVerificationInfo(info)
                    .setFileSize(FileUtils.sizeOf(clientFile))
                    .setFileName(clientFileName)
                    .setHash(HashUtils.sha256Hex(clientFile))
                    .setExpirationDate(String.valueOf(expirationData))
                    .setCreationDateDecryptionPrivateKey(encodedTimePrivateKey)
                    .setVerificationInfoDecryptionPrivateKey(encodedInfoPrivateKey);

            CreateLicenseEvent createLicenseEvent =
                    new CreateLicenseEvent(serverVerifierData);

            EventManager.getEventManager().callEvent(createLicenseEvent);

            serverVerifierData = (ServerVerifierData) createLicenseEvent.getServerVerifierData();

            DataManager.save(uuid, serverVerifierData.toString());

            return true;
        } catch (Exception exception) {
            throw new VerifierException(exception);
        }
    }

    @SuppressWarnings("DataFlowIssue")
    public static ValidationInfoData verification(ValidationInfoData validationInfoData) {
        CredentialData credentialData;
        String message = validationInfoData.getMessage();

        try {
            credentialData = CredentialData.fromString(message);
        } catch (Exception ignore) {
            return validationInfoData;
        }

        try {
            ClientVerifierData clientVerifierData;
            ServerVerifierData serverVerifierData;

            String file = credentialData.getFile();
            String hwid = credentialData.getHwid();
            String extraData = credentialData.getExtraData();

            try {
                clientVerifierData = ClientVerifierData.fromString(file);
                validationInfoData.setUuid(clientVerifierData.getUuid());
                serverVerifierData = ServerVerifierData.fromString(DataManager.get(clientVerifierData.getUuid()));
            } catch (Exception exception) {
                return validationInfoData.setResult("FILE_NOT_FOUND_ERROR");
            }

            ValidatorResult callValidatorsResult =
                    ValidatorManager.getValidatorManager().callValidators(
                            validationInfoData.getIp(), credentialData, clientVerifierData, serverVerifierData
                    );

            Object validatorServerVerifierData = callValidatorsResult.getServerVerifierData();

            if (validatorServerVerifierData != null) {
                if (validatorServerVerifierData.toString().isEmpty()) {
                    DataManager.delete(validationInfoData.getUuid());
                } else {
                    ServerVerifierData newServerVerifierData = (ServerVerifierData) validatorServerVerifierData;

                    serverVerifierData = newServerVerifierData;
                    DataManager.save(validationInfoData.getUuid(), newServerVerifierData.toString());
                }
            }

            String result = customResultHandler(
                    callValidatorsResult.getResultString(), validationInfoData.getUuid(), validationInfoData.getIp(),
                    hwid, extraData, serverVerifierData
            );

            LicenseValidationCompleteEvent licenseValidationCompleteEvent = new LicenseValidationCompleteEvent(
                    validationInfoData.getUuid(), validationInfoData.getIp(), hwid, extraData, result
            );

            EventManager.getEventManager().callEvent(licenseValidationCompleteEvent);

            validationInfoData.setResult(licenseValidationCompleteEvent.getResult());
        } catch (Exception exception) {
            return validationInfoData.setResult("FILE_ERROR");
        }

        return validationInfoData;
    }

    private static String customResultHandler(String result, String uuid, String ip, String hwid, String extraData, ServerVerifierData serverVerifierData) {
        String configResult = "";
        String config = YamlManager.getConfig().getString("CUSTOM-RESULT." + result);

        if (!config.isEmpty()) {
            configResult = buildCustomResult(config, uuid, ip, hwid, extraData, serverVerifierData);
        }

        return configResult.isEmpty() ? result : configResult;
    }

    private static String buildCustomResult(String config, String uuid, String ip, String hwid, String extraData, ServerVerifierData serverVerifierData) {
        return config.replace("<uuid>", uuid)
                .replace("<ip>", ip)
                .replace("<hwid>", hwid)
                .replace("<extraData>", extraData)
                .replace("<isTempBanned>", String.valueOf(serverVerifierData.isTempBanned()))
                .replace("<tempBannedInfo>", serverVerifierData.getTempBannedInfo())
                .replace("<hwidLimit>", String.valueOf(serverVerifierData.getHwidLimit()))
                .replace("<hwidAmount>", String.valueOf(serverVerifierData.getHwids().size()))
                .replace("<ipLimit>", String.valueOf(serverVerifierData.getIpLimit()))
                .replace("<ipAmount>", String.valueOf(serverVerifierData.getIps().size()))
                .replace("<verificationLimit>", String.valueOf(serverVerifierData.getVerificationLimit()))
                .replace("<verificationTimes>", String.valueOf(serverVerifierData.getVerificationTimes()))
                .replace("<parallelLimit>", String.valueOf(serverVerifierData.getParallelLimit()))
                .replace("<creationDate>", serverVerifierData.getCreationDate())
                .replace("<verificationInfo>", serverVerifierData.getVerificationInfo())
                .replace("<expirationDate>", String.valueOf(serverVerifierData.getExpirationDate()));
    }
}
