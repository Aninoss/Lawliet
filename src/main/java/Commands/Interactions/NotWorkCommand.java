package Commands.Interactions;
import CommandListeners.onRecievedListener;
import org.javacord.api.event.message.MessageCreateEvent;

import java.util.ArrayList;

public class NotWorkCommand extends InteractionCommand implements onRecievedListener {
    private static ArrayList<Integer> picked = new ArrayList<>();

    public NotWorkCommand() {
        super();
        trigger = "notwork";
        emoji = "❌";
        nsfw = false;
        gifs = new String[]{
                "https://cdn.discordapp.com/attachments/499629904380297226/621051161952256050/notwork.png",
                "https://media1.tenor.com/images/471e6c2ca597921c3f26ea7713555feb/tenor.gif?itemid=11299432"
        };
    }

    @Override
    public boolean onRecieved(MessageCreateEvent event, String followedString) throws Throwable {
        return onInteractionRecieved(event, followedString, picked);
    }
}
