package Commands.NSFWCategory;

import CommandListeners.CommandProperties;
import CommandListeners.OnTrackerRequestListener;
import Commands.GelbooruAbstract;

import java.util.Locale;

@CommandProperties(
        trigger = "yuri",
        executable = true,
        emoji = "\uD83D\uDD1E",
        nsfw = true,
        requiresEmbeds = false,
        withLoadingBar = true
)
public class YuriCommand extends GelbooruAbstract implements OnTrackerRequestListener {

    public YuriCommand(Locale locale, String prefix) {
        super(locale, prefix);
    }

    @Override
    protected String getSearchKey() {
        return "animated yuri";
    }

    @Override
    protected boolean isAnimatedOnly() {
        return true;
    }

}