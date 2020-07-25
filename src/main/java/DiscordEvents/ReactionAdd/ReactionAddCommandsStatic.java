package DiscordEvents.ReactionAdd;

import CommandListeners.OnReactionAddStaticListener;
import CommandSupporters.Command;
import CommandSupporters.CommandContainer;
import CommandSupporters.CommandManager;
import Constants.Settings;
import DiscordEvents.DiscordEventAnnotation;
import DiscordEvents.EventTypeAbstracts.ReactionAddAbstract;
import MySQL.Modules.Server.DBServer;
import MySQL.Modules.Server.ServerBean;
import org.javacord.api.entity.message.Message;
import org.javacord.api.entity.message.embed.Embed;
import org.javacord.api.event.message.reaction.ReactionAddEvent;

import java.util.concurrent.ExecutionException;

@DiscordEventAnnotation()
public class ReactionAddCommandsStatic extends ReactionAddAbstract {

    @Override
    public boolean onReactionAdd(ReactionAddEvent event) throws Throwable {
        Message message;
        try {
            if (event.getMessage().isPresent()) message = event.getMessage().get();
            else message = event.getChannel().getMessageById(event.getMessageId()).get();
        } catch (InterruptedException | ExecutionException e) {
            //Ignore
            return true;
        }

        if (event.getServer().isPresent() &&
                message.getAuthor().isYourself() &&
                message.getEmbeds().size() > 0
        ) {
            ServerBean serverBean = DBServer.getInstance().getBean(event.getServer().get().getId());
            Embed embed = message.getEmbeds().get(0);
            if (embed.getTitle().isPresent() && !embed.getAuthor().isPresent()) {
                String title = embed.getTitle().get();
                for (Class<? extends OnReactionAddStaticListener> clazz : CommandContainer.getInstance().getStaticReactionAddCommands()) {
                    Command command = CommandManager.createCommandByClass((Class<? extends Command>) clazz, serverBean.getLocale(), serverBean.getPrefix());
                    if (title.toLowerCase().startsWith(((OnReactionAddStaticListener)command).getTitleStartIndicator().toLowerCase()) && title.endsWith(Settings.EMPTY_EMOJI)) {
                        ((OnReactionAddStaticListener)command).onReactionAddStatic(message, event);
                        return false;
                    }
                }
            }
        }

        return true;
    }

}
