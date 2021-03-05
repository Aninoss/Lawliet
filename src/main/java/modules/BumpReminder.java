package modules;

import constants.AssetIds;
import core.ShardManager;
import core.MainLogger;
import core.schedule.MainScheduler;
import core.utils.TimeUtil;
import mysql.modules.bump.DBBump;
import org.javacord.api.util.logging.ExceptionLogger;

import java.time.Instant;

public class BumpReminder {

    private static final BumpReminder ourInstance = new BumpReminder();

    public static BumpReminder getInstance() {
        return ourInstance;
    }

    private BumpReminder() {
    }

    private boolean started = false;
    private boolean countdownRunning = false;

    public void start() {
        if (started) return;
        started = true;

        try {
            Instant nextBump = DBBump.getNextBump();
            long milis = TimeUtil.getMillisBetweenInstants(Instant.now(), nextBump);
            startCountdown(milis);
        } catch (Throwable e) {
            MainLogger.get().error("Exception on bump reminder init");
        }
    }

    public void startCountdown(long milis) {
        if (countdownRunning) return;
        countdownRunning = true;

        final long ANINOSS_SERVER_ID = AssetIds.ANICORD_SERVER_ID;
        final long BUMP_CHANNEL_ID = 713849992611102781L;

        MainScheduler.getInstance().schedule(milis, "anicord_bump", () -> {
            ShardManager.getInstance().getLocalGuildById(ANINOSS_SERVER_ID)
                    .flatMap(server -> server.getTextChannelById(BUMP_CHANNEL_ID))
                    .ifPresent(channel -> {
                        channel.sendMessage("<@&755828541886693398> Der Server ist wieder bereit fürs Bumpen! Schreibt `!d bump`").exceptionally(ExceptionLogger.get());
                        countdownRunning = false;
                    });
        });
    }

}
