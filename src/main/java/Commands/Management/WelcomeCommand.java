package Commands.Management;

import CommandListeners.CommandProperties;
import CommandListeners.onNavigationListener;
import CommandSupporters.Command;
import Constants.LogStatus;
import Constants.Permission;
import Constants.Response;
import General.*;
import General.Mention.MentionFinder;
import MySQL.DBMain;
import MySQL.DBServer;
import org.apache.commons.io.FileUtils;
import org.javacord.api.DiscordApi;
import org.javacord.api.entity.channel.ServerTextChannel;
import org.javacord.api.entity.message.Message;
import org.javacord.api.entity.message.MessageAttachment;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.entity.server.Server;
import org.javacord.api.entity.user.User;
import org.javacord.api.event.message.MessageCreateEvent;
import org.javacord.api.event.message.reaction.SingleReactionEvent;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@CommandProperties(
    trigger = "welcome",
    botPermissions = Permission.ATTACH_FILES_TO_TEXT_CHANNEL,
    userPermissions = Permission.MANAGE_SERVER,
    emoji = "\uD83D\uDE4B",
    thumbnail = "http://icons.iconarchive.com/icons/graphicloads/flat-finance/128/person-icon.png",
    executable = true
)
public class WelcomeCommand extends Command implements onNavigationListener {
    
    private WelcomeMessageSetting welcomeMessageSetting;
    private User author;

    public WelcomeCommand() {
        super();
    }

    @Override
    public Response controllerMessage(MessageCreateEvent event, String inputString, int state, boolean firstTime) throws Throwable {
        if (firstTime) {
            welcomeMessageSetting = DBServer.getWelcomeMessageSettingFromServer(getLocale(), event.getServer().get());
            author = event.getMessage().getUserAuthor().get();
            checkChannelWithLog(welcomeMessageSetting.getWelcomeChannel());
            checkChannelWithLog(welcomeMessageSetting.getFarewellChannel());
            return Response.TRUE;
        }

        switch (state) {
            case 1:
                if (inputString.length() > 0) {
                    if (inputString.length() <= 20) {
                        welcomeMessageSetting.setTitle(inputString);
                        DBServer.saveWelcomeMessageSetting(welcomeMessageSetting);
                        setLog(LogStatus.SUCCESS, getString("titleset"));
                        setState(0);
                        return Response.TRUE;
                    } else {
                        setLog(LogStatus.FAILURE, getString("titletoolarge", "20"));
                        return Response.FALSE;
                    }
                }
                return Response.FALSE;

            case 2:
                inputString = DBMain.encryptEmojis(inputString);

                if (inputString.length() > 0) {
                    if (inputString.length() <= 500) {
                        welcomeMessageSetting.setDescription(inputString);
                        DBServer.saveWelcomeMessageSetting(welcomeMessageSetting);
                        setLog(LogStatus.SUCCESS, getString("descriptionset"));
                        setState(0);
                        return Response.TRUE;
                    } else {
                        setLog(LogStatus.FAILURE, getString("descriptiontoolarge", "500"));
                        return Response.FALSE;
                    }
                }
                return Response.FALSE;

            case 3:
                ArrayList<ServerTextChannel> channelList = MentionFinder.getTextChannels(event.getMessage(), inputString).getList();
                if (channelList.size() == 0) {
                    setLog(LogStatus.FAILURE, TextManager.getString(getLocale(), TextManager.GENERAL, "no_results_description", inputString));
                    return Response.FALSE;
                } else {
                    if (checkChannelWithLog(channelList.get(0))) {
                        welcomeMessageSetting.setWelcomeChannel(channelList.get(0));
                        DBServer.saveWelcomeMessageSetting(welcomeMessageSetting);
                        setLog(LogStatus.SUCCESS, getString("channelset"));
                        setState(0);
                        return Response.TRUE;
                    } else return Response.FALSE;
                }

            case 4:
                List<MessageAttachment> attachmentList = event.getMessage().getAttachments();
                if (attachmentList.size() > 0 && attachmentList.get(0).isImage()) {
                    MessageAttachment messageAttachment = attachmentList.get(0);

                    long size = messageAttachment.getSize();
                    if (size >= 8000000) {
                        setLog(LogStatus.FAILURE, TextManager.getString(getLocale(), TextManager.GENERAL, "file_too_large"));
                        return Response.FALSE;
                    }

                    BufferedImage bi = messageAttachment.downloadAsImage().get();
                    ImageIO.write(bi, "png", new File("data/welcome_backgrounds/" + event.getServer().get().getIdAsString() + ".png"));

                    setLog(LogStatus.SUCCESS, getString("backgroundset"));
                    setState(0);
                    return Response.TRUE;
                }

                ArrayList<URL> imageList = MentionFinder.getImages(inputString).getList();
                if (imageList.size() > 0) {
                    URL url = imageList.get(0);

                    try {
                        long size = Tools.getURLFileSize(event.getApi(), url);
                        if (size >= 8000000) {
                            setLog(LogStatus.FAILURE, TextManager.getString(getLocale(), TextManager.GENERAL, "file_too_large"));
                            return Response.FALSE;
                        }
                        FileUtils.copyURLToFile(url, new File("data/welcome_backgrounds/" + event.getServer().get().getIdAsString() + ".png"));

                        setLog(LogStatus.SUCCESS, getString("backgroundset"));
                        setState(0);
                        return Response.TRUE;
                    } catch (IOException e) {
                        if (e.toString().contains("403") && url.toString().startsWith("https://cdn.discordapp.com/attachments/")) {
                            setLog(LogStatus.FAILURE, getString("image_no_permission"));
                        } else {
                            setLog(LogStatus.FAILURE, getString("404"));
                        }
                        return Response.FALSE;
                    }
                }

                setLog(LogStatus.FAILURE, getString("imagenotfound"));
                return Response.FALSE;

            case 6:
                inputString = DBMain.encryptEmojis(inputString);

                if (inputString.length() > 0) {
                    if (inputString.length() <= 500) {
                        welcomeMessageSetting.setGoodbyeText(inputString);
                        DBServer.saveWelcomeMessageSetting(welcomeMessageSetting);
                        setLog(LogStatus.SUCCESS, getString("goodbyetextset"));
                        setState(0);
                        return Response.TRUE;
                    } else {
                        setLog(LogStatus.FAILURE, getString("goodbyetoolarge", "500"));
                        return Response.FALSE;
                    }
                }
                return Response.FALSE;

            case 7:
                channelList = MentionFinder.getTextChannels(event.getMessage(), inputString).getList();
                if (channelList.size() == 0) {
                    setLog(LogStatus.FAILURE, TextManager.getString(getLocale(), TextManager.GENERAL, "no_results_description", inputString));
                    return Response.FALSE;
                } else {
                    if (checkChannelWithLog(channelList.get(0))) {
                        welcomeMessageSetting.setFarewellChannel(channelList.get(0));
                        DBServer.saveWelcomeMessageSetting(welcomeMessageSetting);
                        setLog(LogStatus.SUCCESS, getString("farechannelset"));
                        setState(0);
                        return Response.TRUE;
                    } else return Response.FALSE;
                }
        }

        return null;
    }

    @Override
    public boolean controllerReaction(SingleReactionEvent event, int i, int state) throws Throwable {

        switch (state) {
            case 0:
                switch (i) {
                    case -1:
                        deleteNavigationMessage();
                        return false;

                    case 0:
                        welcomeMessageSetting.setActivated(!welcomeMessageSetting.isActivated());
                        DBServer.saveWelcomeMessageSetting(welcomeMessageSetting);
                        setLog(LogStatus.SUCCESS, getString("activateset", !welcomeMessageSetting.isActivated()));
                        return true;

                    case 1:
                        setState(1);
                        return true;

                    case 2:
                        setState(2);
                        return true;

                    case 3:
                        setState(3);
                        return true;

                    case 4:
                        setState(4);
                        return true;

                    case 5:
                        welcomeMessageSetting.setGoodbye(!welcomeMessageSetting.isGoodbye());
                        DBServer.saveWelcomeMessageSetting(welcomeMessageSetting);
                        setLog(LogStatus.SUCCESS, getString("goodbyeset", !welcomeMessageSetting.isGoodbye()));
                        return true;

                    case 6:
                        setState(6);
                        return true;

                    case 7:
                        setState(7);
                        return true;

                    case 8:
                        setState(5);
                        return true;
                }
                return false;

            default:
                if (i == -1) {
                    setState(0);
                    return true;
                } return false;
        }
    }

    @Override
    public EmbedBuilder draw(DiscordApi api, int state) throws Throwable {
        switch (state) {
            case 0:
                setOptions(getString("state0_options").split("\n"));
                return EmbedFactory.getCommandEmbedStandard(this, getString("state0_description"))
                        .addField(Tools.getEmptyCharacter(), Tools.getEmptyCharacter(), false)
                        .addField(getString("state0_menabled"), Tools.getOnOffForBoolean(getLocale(), welcomeMessageSetting.isActivated()), true)
                       .addField(getString("state0_mtitle"), welcomeMessageSetting.getTitle(), true)
                       .addField(getString("state0_mdescription"),
                               replaceVariables(welcomeMessageSetting.getDescription(),
                                       "`%SERVER`",
                                       "`%USER`",
                                      "`%MEMBERS`"),
                               true)
                       .addField(getString("state0_mchannel"),welcomeMessageSetting.getWelcomeChannel().getMentionTag(), true)
                        .addField(Tools.getEmptyCharacter(), Tools.getEmptyCharacter(), false)
                        .addField(getString("state0_mgoodbye"), Tools.getOnOffForBoolean(getLocale(), welcomeMessageSetting.isGoodbye()), true)
                       .addField(getString("state0_mgoodbyeText"),
                               replaceVariables(welcomeMessageSetting.getGoodbyeText(),
                                       "`%SERVER`",
                                       "`%USER`",
                                       "`%MEMBERS`").replace("``", "` `"),
                                true)
                       .addField(getString("state0_mfarewellchannel"), welcomeMessageSetting.getFarewellChannel().getMentionTag(), true);

            default:
                if (state == 5) {
                    return getWelcomeMessageTest(author);
                }
                return EmbedFactory.getCommandEmbedStandard(this, getString("state"+state+"_description"), getString("state"+state+"_title"));
        }
    }

    @Override
    public void onNavigationTimeOut(Message message) throws Throwable {
    }

    @Override
    public int getMaxReactionNumber() {
        return 9;
    }

    public EmbedBuilder getWelcomeMessageTest(User user) throws ExecutionException, InterruptedException {
        Server server = welcomeMessageSetting.getServer();
        EmbedBuilder eb = EmbedFactory.getEmbed()
                .setDescription(replaceVariables(welcomeMessageSetting.getDescription(),
                        server.getName(),
                        user.getMentionTag(),
                        Tools.numToString(getLocale(), server.getMembers().size())));

        eb.setImage(Tools.getURLFromInputStream(user.getApi(), ImageCreator.createImageWelcome(user, server, welcomeMessageSetting.getTitle())).toString());
        return eb;
    }

    public static String replaceVariables(String string, String arg1, String arg2, String arg3) {
        return string.replaceAll("(?i)" + Pattern.quote("%server"), Matcher.quoteReplacement(arg1))
                .replaceAll("(?i)" + Pattern.quote("%user"), Matcher.quoteReplacement(arg2))
                .replaceAll("(?i)" + Pattern.quote("%members"), Matcher.quoteReplacement(arg3));
    }
}