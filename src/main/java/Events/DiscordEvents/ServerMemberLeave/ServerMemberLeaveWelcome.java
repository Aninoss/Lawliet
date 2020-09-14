package Events.DiscordEvents.ServerMemberLeave;

import Commands.ManagementCategory.WelcomeCommand;
import Constants.Permission;
import Core.PermissionCheckRuntime;
import Core.Utils.StringUtil;
import Events.DiscordEvents.DiscordEvent;
import Events.DiscordEvents.EventTypeAbstracts.ServerMemberLeaveAbstract;
import Modules.Welcome;
import MySQL.Modules.Server.DBServer;
import MySQL.Modules.WelcomeMessage.DBWelcomeMessage;
import MySQL.Modules.WelcomeMessage.WelcomeMessageBean;
import org.javacord.api.entity.server.Server;
import org.javacord.api.entity.user.User;
import org.javacord.api.event.server.member.ServerMemberLeaveEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Locale;
import java.util.concurrent.ExecutionException;

@DiscordEvent(allowBots = true)
public class ServerMemberLeaveWelcome extends ServerMemberLeaveAbstract {

    private final static Logger LOGGER = LoggerFactory.getLogger(ServerMemberLeaveWelcome.class);

    @Override
    public boolean onServerMemberLeave(ServerMemberLeaveEvent event) throws Throwable {
        Server server = event.getServer();
        Locale locale = DBServer.getInstance().getBean(server.getId()).getLocale();

        WelcomeMessageBean welcomeMessageBean = DBWelcomeMessage.getInstance().getBean(server.getId());
        if (welcomeMessageBean.isGoodbyeActive()) {
            welcomeMessageBean.getGoodbyeChannel().ifPresent(channel -> {
                if (PermissionCheckRuntime.getInstance().botHasPermission(locale, WelcomeCommand.class, channel, Permission.READ_MESSAGES | Permission.SEND_MESSAGES | Permission.EMBED_LINKS | Permission.ATTACH_FILES)) {
                    User user = event.getUser();
                    try {
                        channel.sendMessage(
                                StringUtil.defuseMassPing(
                                        Welcome.resolveVariables(
                                                welcomeMessageBean.getGoodbyeText(),
                                                StringUtil.escapeMarkdown(server.getName()),
                                                user.getMentionTag(),
                                                StringUtil.escapeMarkdown(user.getName()),
                                                StringUtil.escapeMarkdown(user.getDiscriminatedName()),
                                                StringUtil.numToString(locale, server.getMemberCount())
                                        )
                                )
                        ).get();
                    } catch (InterruptedException | ExecutionException e) {
                        LOGGER.error("Could not send message", e);
                    }
                }
            });
        }

        return true;
    }

}