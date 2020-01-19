package Commands.FisheryCategory;

import CommandListeners.CommandProperties;
import CommandListeners.onRecievedListener;
import CommandSupporters.Command;
import Constants.*;
import General.*;
import MySQL.DBServer;
import MySQL.DBUser;
import General.DailyState;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.event.message.MessageCreateEvent;

@CommandProperties(
    trigger = "daily",
    botPermissions = Permission.USE_EXTERNAL_EMOJIS_IN_TEXT_CHANNEL,
    thumbnail = "http://icons.iconarchive.com/icons/fps.hu/free-christmas-flat-circle/128/calendar-icon.png",
    emoji = "\uD83D\uDDD3",
    withLoadingBar = true,
    executable = true
)
public class DailyCommand extends Command implements onRecievedListener {

    public DailyCommand() {
        super();
    }

    @Override
    public boolean onRecieved(MessageCreateEvent event, String followedString) throws Throwable {
        PowerPlantStatus status = DBServer.getPowerPlantStatusFromServer(event.getServer().get());
        if (status == PowerPlantStatus.ACTIVE) {
            DailyState dailyState = DBUser.daily(event.getServer().get(), event.getMessage().getUserAuthor().get());
            if (dailyState != null && dailyState.isClaimed()) {
                long fishes = DBUser.getFishingProfile(event.getServer().get(), event.getMessage().getUserAuthor().get(), false).getEffect(FishingCategoryInterface.PER_DAY);

                int bonusCombo = 0;
                int bonusDonation = 0;
                int dailyBefore = -1;
                if (dailyState.isStreakBroken()) {
                    dailyBefore = dailyState.getStreak();
                } else {
                    dailyBefore = dailyState.getStreak() - 1;
                    if (dailyState.getStreak() >= 5) {
                        bonusCombo = (int) Math.round(fishes * 0.25);
                    }
                }

                if (DBUser.hasDonated(event.getMessage().getUserAuthor().get())) {
                    bonusDonation = (int) Math.round((fishes + bonusCombo) * 0.5);
                }

                StringBuilder sb = new StringBuilder(getString("point_default", Tools.numToString(getLocale(), fishes)));
                if (bonusCombo > 0) sb.append("\n").append(getString("point_combo", Tools.numToString(getLocale(), bonusCombo)));
                if (bonusDonation > 0) sb.append("\n").append(getString("point_donation", Tools.numToString(getLocale(), bonusDonation)));

                EmbedBuilder eb = EmbedFactory.getCommandEmbedSuccess(this, getString("codeblock", sb.toString()));
                eb.addField(getString("didyouknow_title"), getString("didyouknow_desc", Settings.UPVOTE_URL), false);
                if (dailyState.isStreakBroken()) EmbedFactory.addLog(eb, LogStatus.LOSE, getString("combobreak"));

                event.getChannel().sendMessage(eb).get();
                event.getChannel().sendMessage(DBUser.addFishingValues(getLocale(), event.getServer().get(), event.getMessage().getUserAuthor().get(), fishes + bonusCombo + bonusDonation, 0L, dailyBefore, false)).get();

                return true;
            } else {
                EmbedBuilder eb = EmbedFactory.getCommandEmbedError(this, getString("claimed_desription"), getString("claimed_title"));
                event.getChannel().sendMessage(eb).get();
                return false;
            }
        } else {
            event.getChannel().sendMessage(EmbedFactory.getCommandEmbedError(this, TextManager.getString(getLocale(), TextManager.GENERAL, "fishing_notactive_description").replace("%PREFIX", getPrefix()), TextManager.getString(getLocale(), TextManager.GENERAL, "fishing_notactive_title")));
            return false;
        }
    }
}
