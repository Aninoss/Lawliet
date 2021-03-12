package core;

import java.io.File;
import java.time.Instant;
import core.emoji.EmojiTable;
import core.utils.BotUtil;
import mysql.DBMain;
import mysql.modules.version.DBVersion;
import mysql.modules.version.VersionBean;
import mysql.modules.version.VersionBeanSlot;
import websockets.syncserver.SyncManager;

public class Main {

    public static void main(String[] args) {
        try {
            Bot.init();
            createTempDir();

            Console.getInstance().start();
            FontContainer.getInstance().init();
            DBMain.getInstance().connect();
            EmojiTable.getInstance().load();
            if (Bot.isPublicVersion()) {
                initializeUpdate();
            }

            MainLogger.get().info("Waiting for sync server");
            SyncManager.getInstance().start();

            if (!Bot.isProductionMode()) {
                DiscordConnector.getInstance().connect(0, 0, 1);
            } else {
                Runtime.getRuntime().addShutdownHook(new CustomThread(Bot::onStop, "shutdown_botstop"));
            }
        } catch (Throwable e) {
            MainLogger.get().error("EXIT - Error on startup", e);
            System.exit(1);
        }
    }

    private static void createTempDir() {
        File tempDir = ResourceHandler.getFileResource("temp");
        if (!tempDir.exists() && !tempDir.mkdir()) {
            throw new RuntimeException("Could not create temp dir");
        }
    }

    private static void initializeUpdate() {
        VersionBean versionBean = DBVersion.getInstance().retrieve();

        String currentVersionDB = versionBean.getCurrentVersion().getVersion();
        if (!BotUtil.getCurrentVersion().equals(currentVersionDB)) {
            versionBean.getSlots().add(new VersionBeanSlot(BotUtil.getCurrentVersion(), Instant.now()));
        }
    }

}
