package events.discordevents.eventtypeabstracts;

import events.discordevents.DiscordEventAbstract;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceJoinEvent;
import java.util.ArrayList;

public abstract class GuildVoiceJoinAbstract extends DiscordEventAbstract {

    public abstract boolean onGuildVoiceJoin(GuildVoiceJoinEvent event) throws Throwable;

    public static void onGuildVoiceJoinStatic(GuildVoiceJoinEvent event, ArrayList<DiscordEventAbstract> listenerList) {
        execute(listenerList, event.getMember().getUser(), event.getGuild().getIdLong(),
                listener -> ((GuildVoiceJoinAbstract) listener).onGuildVoiceJoin(event)
        );
    }

}
