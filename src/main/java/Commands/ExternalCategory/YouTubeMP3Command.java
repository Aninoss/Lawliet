package Commands.ExternalCategory;

import CommandListeners.CommandProperties;
import CommandListeners.onRecievedListener;
import CommandSupporters.Command;
import Constants.Permission;
import General.*;
import org.javacord.api.entity.message.Message;
import org.javacord.api.event.message.MessageCreateEvent;

import java.io.File;
import java.util.Optional;

@CommandProperties(
        trigger = "ytmp3",
        withLoadingBar = true,
        emoji = "\uD83C\uDFB5",
        botPermissions = Permission.ATTACH_FILES_TO_TEXT_CHANNEL,
        userPermissions = Permission.ATTACH_FILES_TO_TEXT_CHANNEL,
        thumbnail = "http://icons.iconarchive.com/icons/martz90/circle/128/youtube-icon.png",
        executable = false,
        aliases = {"youtubemp3", "yt"}
)
public class YouTubeMP3Command extends Command implements onRecievedListener {

    public YouTubeMP3Command() {
        super();
    }

    @Override
    public boolean onReceived(MessageCreateEvent event, String followedString) throws Throwable {

        if (!followedString.isEmpty()) {
            Optional<String> videoIdOptional = YouTubeDownloader.getVideoID(followedString);

            if (videoIdOptional.isPresent()) {
                String videoId = videoIdOptional.get();
                if (videoId.equalsIgnoreCase("%toolong")) {
                    event.getChannel().sendMessage(EmbedFactory.getCommandEmbedError(this, getString("toolong_desc"), getString("toolong_title"))).get();
                    return false;
                }

                if (videoId.equalsIgnoreCase("%error")) {
                    event.getChannel().sendMessage(EmbedFactory.getCommandEmbedError(this, getString("error_desc"), getString("error_title"))).get();
                    return false;
                }

                String loadingEmoji = Tools.getLoadingReaction(event.getServerTextChannel().get());
                Message message = event.getChannel().sendMessage(EmbedFactory.getCommandEmbedStandard(this, getString("loading", loadingEmoji))).get();

                try {
                    File audioFile = YouTubeDownloader.downloadAudio(videoId);
                    event.getChannel().sendMessage(event.getMessage().getUserAuthor().get().getMentionTag(), EmbedFactory.getCommandEmbedStandard(this, getString("finished")), audioFile).get();
                    return true;
                } catch (Throwable e) {
                    throw e;
                } finally {
                    message.delete().get();
                }
            }

            event.getChannel().sendMessage(EmbedFactory.getCommandEmbedError(this, getString("invalid", followedString))).get();
            return false;
        } else {
            event.getChannel().sendMessage(EmbedFactory.getCommandEmbedError(this, getString("noargs"))).get();
            return false;
        }
    }
}
