package Commands.NSFWCategory;

import CommandListeners.CommandProperties;
import Commands.GelbooruAbstract;

import java.util.Locale;

@CommandProperties(
        trigger = "neko",
        executable = true,
        emoji = "\uD83D\uDD1E",
        nsfw = true,
        requiresEmbeds = false,
        withLoadingBar = true
)
public class NekoCommand extends GelbooruAbstract {

    public NekoCommand(Locale locale, String prefix) {
        super(locale, prefix);
    }

    @Override
    protected String getSearchKey() {
        return "cat_girl -futa";
    }

    @Override
    protected boolean isAnimatedOnly() {
        return false;
    }

}