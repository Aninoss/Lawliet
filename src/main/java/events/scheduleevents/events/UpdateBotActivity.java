package events.scheduleevents.events;

import core.DiscordConnector;
import core.ShardManager;
import core.schedule.ScheduleInterface;
import events.scheduleevents.ScheduleEventFixedRate;

import java.time.temporal.ChronoUnit;

@ScheduleEventFixedRate(rateValue = 10, rateUnit = ChronoUnit.MINUTES)
public class UpdateBotActivity implements ScheduleInterface {

    @Override
    public void run() throws Throwable {
        ShardManager.getInstance().getConnectedLocalJDAs().forEach(api -> DiscordConnector.getInstance().updateActivity(api));
    }

}