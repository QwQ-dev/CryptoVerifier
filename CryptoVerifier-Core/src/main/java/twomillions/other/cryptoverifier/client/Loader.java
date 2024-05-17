package twomillions.other.cryptoverifier.client;

import org.apache.commons.io.FileUtils;
import twomillions.other.cryptoverifier.commands.impl.handlers.CommandHandler;
import twomillions.other.cryptoverifier.communication.data.CredentialData;
import twomillions.other.cryptoverifier.crypto.hardware.HardwareInfoManager;
import twomillions.other.cryptoverifier.crypto.verifier.Verifier;
import twomillions.other.cryptoverifier.events.custom.InitializationCompleteEvent;
import twomillions.other.cryptoverifier.events.impl.EventManager;
import twomillions.other.cryptoverifier.io.config.ConfigManager;
import twomillions.other.cryptoverifier.io.databases.nonpersistent.NonPersistentDatabasesManager;
import twomillions.other.cryptoverifier.io.databases.persistent.PersistentDatabasesManager;
import twomillions.other.cryptoverifier.io.yaml.YamlManager;
import twomillions.other.cryptoverifier.plugin.JarLoader;
import twomillions.other.cryptoverifier.thread.VerifierThreadPool;
import twomillions.other.cryptoverifier.util.LoggerUtils;

import java.io.File;

public class Loader {
    static {
        LoggerUtils.getLogger().info("⠄⠄⠄⢰⣧⣼⣯⠄⣸⣠⣶⣶⣦⣾⠄⠄⠄⠄⡀⠄⢀⣿⣿⠄⠄⠄⢸⡇⠄⠄");
        LoggerUtils.getLogger().info("⠄⠄⠄⣾⣿⠿⠿⠶⠿⢿⣿⣿⣿⣿⣦⣤⣄⢀⡅⢠⣾⣛⡉⠄⠄⠄⠸⢀⣿⠄");
        LoggerUtils.getLogger().info("⠄⠄⢀⡋⣡⣴⣶⣶⡀⠄⠄⠙⢿⣿⣿⣿⣿⣿⣴⣿⣿⣿⢃⣤⣄⣀⣥⣿⣿⠄");
        LoggerUtils.getLogger().info("⠄⠄⢸⣇⠻⣿⣿⣿⣧⣀⢀⣠⡌⢻⣿⣿⣿⣿⣿⣿⣿⣿⣿⠿⠿⠿⣿⣿⣿⠄");
        LoggerUtils.getLogger().info("⠄⢀⢸⣿⣷⣤⣤⣤⣬⣙⣛⢿⣿⣿⣿⣿⣿⣿⡿⣿⣿⡍⠄⠄⢀⣤⣄⠉⠋⣰");
        LoggerUtils.getLogger().info("⠄⣼⣖⣿⣿⣿⣿⣿⣿⣿⣿⣿⢿⣿⣿⣿⣿⣿⢇⣿⣿⡷⠶⠶⢿⣿⣿⠇⢀⣤");
        LoggerUtils.getLogger().info("⠘⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣽⣿⣿⣿⡇⣿⣿⣿⣿⣿⣿⣷⣶⣥⣴⣿⡗");
        LoggerUtils.getLogger().info("⢀⠈⢿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⡟⠄");
        LoggerUtils.getLogger().info("⢸⣿⣦⣌⣛⣻⣿⣿⣧⠙⠛⠛⡭⠅⠒⠦⠭⣭⡻⣿⣿⣿⣿⣿⣿⣿⣿⡿⠃⠄");
        LoggerUtils.getLogger().info("⠘⣿⣿⣿⣿⣿⣿⣿⣿⡆⠄⠄⠄⠄⠄⠄⠄⠄⠹⠈⢋⣽⣿⣿⣿⣿⣵⣾⠃⠄");
        LoggerUtils.getLogger().info("⠄⠘⣿⣿⣿⣿⣿⣿⣿⣿⠄⣴⣿⣶⣄⠄⣴⣶⠄⢀⣾⣿⣿⣿⣿⣿⣿⠃⠄⠄");
        LoggerUtils.getLogger().info("⠄⠄⠈⠻⣿⣿⣿⣿⣿⣿⡄⢻⣿⣿⣿⠄⣿⣿⡀⣾⣿⣿⣿⣿⣛⠛⠁⠄⠄⠄");
        LoggerUtils.getLogger().info("⠄⠄⠄⠄⠈⠛⢿⣿⣿⣿⠁⠞⢿⣿⣿⡄⢿⣿⡇⣸⣿⣿⠿⠛⠁⠄⠄⠄⠄⠄");
        LoggerUtils.getLogger().info("⠄⠄⠄⠄⠄⠄⠄⠉⠻⣿⣿⣾⣦⡙⠻⣷⣾⣿⠃⠿⠋⠁⠄⠄⠄⠄⠄⢀⣠⣴");
        LoggerUtils.getLogger().info("⣿⣿⣿⣶⣶⣮⣥⣒⠲⢮⣝⡿⣿⣿⡆⣿⡿⠃⠄⠄⠄⠄⠄⠄⠄⣠⣴⣿⣿⣿");
    }

    public static void main(String[] args) throws Exception {
        String corePoolSizeString = YamlManager.getConfig().getString("THREAD-POOL.CORE-POOL-SIZE");
        int corePoolSize = corePoolSizeString.equalsIgnoreCase("max") ? Integer.MAX_VALUE : Integer.parseInt(corePoolSizeString);

        String maxPoolSizeString = YamlManager.getConfig().getString("THREAD-POOL.MAX-POOL-SIZE");
        int maxPoolSize = maxPoolSizeString.equalsIgnoreCase("max") ? Integer.MAX_VALUE : Integer.parseInt(maxPoolSizeString);

        String queueCapacityConfig = YamlManager.getConfig().getString("THREAD-POOL.QUEUE-CAPACITY");
        int queueCapacity = queueCapacityConfig.equalsIgnoreCase("max") ? Integer.MAX_VALUE : Integer.parseInt(queueCapacityConfig);

        VerifierThreadPool.setVerifierThreadPool(new VerifierThreadPool(
                corePoolSize,
                maxPoolSize,
                YamlManager.getConfig().getLong("THREAD-POOL.KEEP-ALIVE-TIME"),
                queueCapacity
        ));

        try {
            ConfigManager.init();
        } catch (Exception exception) {
            LoggerUtils.getLogger().error("初始化配置时出错! 请反馈给开发者!", exception);
            return;
        }

        if (!PersistentDatabasesManager.initPersistentDatabases()) {
            return;
        }

        if (!NonPersistentDatabasesManager.initNonPersistentDatabases()) {
            return;
        }

        try {
            Verifier.initVerifierServer();
        } catch (Exception exception) {
            LoggerUtils.getLogger().error("初始化验证模块异常! 请反馈给开发者!", exception);
            return;
        }

        CommandHandler.getCommandHandler().start();

        JarLoader.loadJarsFromDirectory("plugins/");

        EventManager.getEventManager().callEvent(new InitializationCompleteEvent());

        // LOL
        // LoggerUtils.getLogger().warn("[FUCK MESSAGE] FUCK U YXY AND WANG WEN JIE");
    }
}