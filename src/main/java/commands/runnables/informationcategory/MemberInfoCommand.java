package commands.runnables.informationcategory;

import java.util.Locale;
import commands.listeners.CommandProperties;
import commands.runnables.MemberAccountAbstract;
import core.EmbedFactory;
import core.ShardManager;
import core.utils.StringUtil;
import core.utils.TimeUtil;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

@CommandProperties(
        trigger = "userinfo",
        emoji = "\uD83D\uDC81",
        executableWithoutArgs = true,
        aliases = {"userinfos", "userstat", "userstats", "accountinfo", "whois"}
)
public class MemberInfoCommand extends MemberAccountAbstract {

    public MemberInfoCommand(Locale locale, String prefix) {
        super(locale, prefix);
    }

    @Override
    protected EmbedBuilder processMember(GuildMessageReceivedEvent event, Member member, boolean memberIsAuthor, String args) {
        String[] type = getString("type").split("\n");
        int typeN = 0;
        if (!member.getUser().isBot()) {
            typeN = 1;
            if (event.getGuild().getOwnerIdLong() == member.getIdLong()) typeN = 2;
            if (member.getIdLong() == ShardManager.getInstance().getOwnerId()) typeN = 3;
        }

        String[] argsArray = {
                type[typeN],
                StringUtil.escapeMarkdown(member.getUser().getName()),
                member.getNickname() != null ? member.getNickname() : "-",
                member.getUser().getDiscriminator(),
                member.getId(),
                member.getUser().getEffectiveAvatarUrl() + "?size=1024",
                member.hasTimeJoined() ? TimeUtil.getInstantString(getLocale(), member.getTimeJoined().toInstant(), true) : "-",
                TimeUtil.getInstantString(getLocale(), member.getTimeCreated().toInstant(), true)
        };

        return EmbedFactory.getEmbedDefault(this, getString("template", argsArray)).
                setThumbnail(member.getUser().getEffectiveAvatarUrl());
    }

}