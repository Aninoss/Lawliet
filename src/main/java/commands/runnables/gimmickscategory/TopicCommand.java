package commands.runnables.gimmickscategory;

import commands.listeners.CommandProperties;
import commands.listeners.OnTrackerRequestListener;
import commands.Command;
import constants.TrackerResult;
import core.EmbedFactory;
import core.FileManager;
import core.RandomPicker;
import core.TextManager;
import core.utils.StringUtil;
import mysql.modules.tracker.TrackerBeanSlot;
import org.javacord.api.entity.channel.ServerTextChannel;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.event.message.MessageCreateEvent;

import java.io.File;
import java.io.IOException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

@CommandProperties(
        trigger = "topic",
        emoji = "\uD83E\uDD60",
        executableWithoutArgs = true,
        aliases = {"topics"}
)
public class TopicCommand extends Command implements OnTrackerRequestListener {

    public TopicCommand(Locale locale, String prefix) {
        super(locale, prefix);
    }

    @Override
    public boolean onMessageReceived(MessageCreateEvent event, String followedString) throws Throwable {
        send(event.getServerTextChannel().get());
        return true;
    }

    private void send(ServerTextChannel channel) throws IOException, ExecutionException, InterruptedException {
        List<String> topicList = FileManager.readInList(new File("recourses/topics_" + getLocale().getDisplayName() + ".txt"));
        int n = RandomPicker.getInstance().pick(getTrigger(), channel.getServer().getId(), topicList.size());
        String topic = topicList.get(n);

        channel.sendMessage(EmbedFactory.getCommandEmbedStandard(this, topic)).get();
    }

    @Override
    public TrackerResult onTrackerRequest(TrackerBeanSlot slot) throws Throwable {
        final int MAX_MINUTES = 10080;
        String key = slot.getCommandKey();

        long minutes = StringUtil.filterLongFromString(key);
        if (minutes > MAX_MINUTES || minutes < 1) {
            EmbedBuilder eb = EmbedFactory.getCommandEmbedError(this,
                    TextManager.getString(getLocale(), TextManager.GENERAL, "number", "1", StringUtil.numToString(MAX_MINUTES)));
            EmbedFactory.addTrackerRemoveLog(eb, getLocale());

            slot.getChannel().get().sendMessage(eb).get();
            return TrackerResult.STOP_AND_DELETE;
        }

        send(slot.getChannel().get());
        slot.setNextRequest(Instant.now().plus(minutes, ChronoUnit.MINUTES));

        return TrackerResult.CONTINUE_AND_SAVE;
    }

    @Override
    public boolean trackerUsesKey() {
        return true;
    }

}