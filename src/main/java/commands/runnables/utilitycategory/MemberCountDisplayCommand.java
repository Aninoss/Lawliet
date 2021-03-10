package commands.runnables.utilitycategory;

import java.security.Permissions;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import commands.Command;
import commands.listeners.CommandProperties;
import constants.LogStatus;
import constants.Response;
import core.EmbedFactory;
import core.ListGen;
import core.ShardManager;
import core.TextManager;
import core.utils.BotPermissionUtil;
import core.utils.MentionUtil;
import core.utils.StringUtil;
import modules.MemberCountDisplay;
import mysql.modules.membercountdisplays.DBMemberCountDisplays;
import mysql.modules.membercountdisplays.MemberCountBean;
import mysql.modules.membercountdisplays.MemberCountDisplaySlot;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;

@CommandProperties(
        trigger = "mcdisplays",
        userPermissions = PermissionDeprecated.MANAGE_SERVER,
        emoji = "️🧮️",
        executableWithoutArgs = true,
        aliases = {"membercountdisplays", "memberscountdisplays", "memberdisplays", "mdisplays", "countdisplays", "displays", "mcdisplay" }
)
public class MemberCountDisplayCommand extends Command implements OnNavigationListenerOld {

    private MemberCountBean memberCountBean;
    private ServerVoiceChannel currentVC = null;
    private String currentName = null;

    public MemberCountDisplayCommand(Locale locale, String prefix) {
        super(locale, prefix);
    }

    @Override
    public boolean onTrigger(GuildMessageReceivedEvent event, String args) {
        memberCountBean = DBMemberCountDisplays.getInstance().retrieve(event.getGuild().getIdLong());
        memberCountBean.getMemberCountBeanSlots().trim(vcId -> event.getServer().get().getVoiceChannelById(vcId));
        return true;
    }

    @Override
    public Response controllerMessage(GuildMessageReceivedEvent event, String input, int state) {
        if (state == 1) {
            ArrayList<ServerVoiceChannel> vcList = MentionUtil.getVoiceChannels(event.getMessage(), inputString).getList();
            if (vcList.size() == 0) {
                String checkString = inputString.toLowerCase();
                if (!modules.MemberCountDisplay.replaceVariables(checkString, "", "", "", "").equals(checkString)) {
                    if (inputString.length() <= 50) {
                        currentName = inputString;
                        setLog(LogStatus.SUCCESS, getString("nameset"));
                        return Response.TRUE;
                    } else {
                        setLog(LogStatus.FAILURE, getString("nametoolarge", "50"));
                        return Response.FALSE;
                    }
                }

                setLog(LogStatus.FAILURE, TextManager.getNoResultsString(getLocale(), inputString));
                return Response.FALSE;
            } else {
                ServerVoiceChannel channel = vcList.get(0);

                ArrayList<Integer> missingPermissions = BotPermissionUtil.getMissingPermissions(channel.getServer(), channel, ShardManager.getInstance().getSelf(), PermissionDeprecated.MANAGE_CHANNEL | PermissionDeprecated.MANAGE_CHANNEL_PERMISSIONS | PermissionDeprecated.CONNECT);
                if (missingPermissions.size() > 0) {
                    String permissionsList = new ListGen<Integer>().getList(missingPermissions, ListGen.SLOT_TYPE_BULLET, n -> TextManager.getString(getLocale(), TextManager.PERMISSIONS, String.valueOf(n)));
                    setLog(LogStatus.FAILURE, getString("missing_perms", permissionsList));
                    return Response.FALSE;
                }

                if (memberCountBean.getMemberCountBeanSlots().containsKey(channel.getId())) {
                    setLog(LogStatus.FAILURE, getString("alreadyexists"));
                    return Response.FALSE;
                }

                currentVC = channel;

                setLog(LogStatus.SUCCESS, getString("vcset"));
                return Response.TRUE;
            }
        }

        return null;
    }

    @Override
    public boolean controllerReaction(GenericGuildMessageReactionEvent event, int i, int state) {
        switch (state) {
            case 0:
                switch (i) {
                    case -1:
                        removeNavigationWithMessage();
                        return false;

                    case 0:
                        if (memberCountBean.getMemberCountBeanSlots().size() < 5) {
                            setState(1);
                            currentVC = null;
                            currentName = null;
                            return true;
                        } else {
                            setLog(LogStatus.FAILURE, getString("toomanydisplays"));
                            return true;
                        }

                    case 1:
                        if (memberCountBean.getMemberCountBeanSlots().size() > 0) {
                            setState(2);
                            return true;
                        } else {
                            setLog(LogStatus.FAILURE, getString("nothingtoremove"));
                            return true;
                        }

                    default:
                        return false;
                }

            case 1:
                if (i == -1) {
                    setState(0);
                    return true;
                }

                if (i == 0 && currentName != null && currentVC != null) {
                    try {
                        ServerVoiceChannelUpdater updater = currentVC.createUpdater();
                        for (Long roleId : currentVC.getOverwrittenRolePermissions().keySet()) {
                            PermissionsBuilder permissions = currentVC.getOverwrittenPermissions().get(roleId).toBuilder();
                            permissions.setState(PermissionType.CONNECT, PermissionState.DENIED);
                            updater.addPermissionOverwrite(event.getServer().get().getRoleById(roleId).get(), permissions.build());
                        }
                        for (Long userId : currentVC.getOverwrittenUserPermissions().keySet()) {
                            PermissionsBuilder permissions = currentVC.getOverwrittenPermissions().get(userId).toBuilder();
                            permissions.setState(PermissionType.CONNECT, PermissionState.DENIED);
                            updater.addPermissionOverwrite(event.getServer().get().getMemberById(userId).get(), permissions.build());
                        }

                        Role everyoneRole = event.getServer().get().getEveryoneRole();
                        PermissionsBuilder permissions = currentVC.getOverwrittenPermissions(everyoneRole).toBuilder();
                        permissions.setState(PermissionType.CONNECT, PermissionState.DENIED);
                        updater.addPermissionOverwrite(everyoneRole, permissions.build());

                        User yourself = ShardManager.getInstance().getSelf();
                        Permissions ownPermissions = currentVC.getOverwrittenPermissions(yourself)
                                .toBuilder()
                                .setState(PermissionType.MANAGE_CHANNELS, PermissionState.ALLOWED)
                                .setState(PermissionType.MANAGE_ROLES, PermissionState.ALLOWED)
                                .setState(PermissionType.CONNECT, PermissionState.ALLOWED)
                                .build();
                        updater.addPermissionOverwrite(yourself, ownPermissions);

                        updater.update().get(10, TimeUnit.SECONDS);
                    } catch (ExecutionException | TimeoutException e) {
                        //Ignore
                        setLog(LogStatus.FAILURE, getString("nopermissions"));
                        return true;
                    }

                    memberCountBean.getMemberCountBeanSlots().put(currentVC.getId(), new MemberCountDisplaySlot(event.getGuild().getIdLong(), currentVC.getId(), currentName));
                    MemberCountDisplay.getInstance().manage(getLocale(), event.getServer().get());

                    setLog(LogStatus.SUCCESS, getString("displayadd"));
                    setState(0);

                    return true;
                }
                return false;

            case 2:
                if (i == -1) {
                    setState(0);
                    return true;
                } else if (i < memberCountBean.getMemberCountBeanSlots().size()) {
                    memberCountBean.getMemberCountBeanSlots().remove(new ArrayList<>(memberCountBean.getMemberCountBeanSlots().keySet()).get(i));

                    setLog(LogStatus.SUCCESS, getString("displayremove"));
                    setState(0);
                    return true;
                }

            default:
                return false;
        }
    }

    @Override
    public EmbedBuilder draw(int state) {
        String notSet = TextManager.getString(getLocale(), TextManager.GENERAL, "notset");

        switch (state) {
            case 0:
                setOptions(getString("state0_options").split("\n"));
                return EmbedFactory.getEmbedDefault(this, getString("state0_description"))
                        .addField(getString("state0_mdisplays"), highlightVariables(new ListGen<MemberCountDisplaySlot>()
                                .getList(memberCountBean.getMemberCountBeanSlots().values(), getLocale(), bean -> {
                                    if (bean.getVoiceChannel().isPresent()) {
                                        return getString("state0_displays", StringUtil.escapeMarkdown(bean.getVoiceChannel().get().getName()), StringUtil.escapeMarkdown(bean.getMask()));
                                    } else {
                                        return getString("state0_displays", "???", StringUtil.escapeMarkdown(bean.getMask()));
                                    }
                                })), false);

            case 1:
                if (currentName != null && currentVC != null) setOptions(new String[]{getString("state1_options")});
                return EmbedFactory.getEmbedDefault(this, getString("state1_description", StringUtil.escapeMarkdown(Optional.ofNullable(currentVC).map(Nameable::getName).orElse(notSet)), highlightVariables(StringUtil.escapeMarkdown(Optional.ofNullable(currentName).orElse(notSet)))), getString("state1_title"));

            case 2:
                ArrayList<MemberCountDisplaySlot> channelNames = new ArrayList<>(memberCountBean.getMemberCountBeanSlots().values());
                String[] roleStrings = new String[channelNames.size()];
                for(int i = 0; i < roleStrings.length; i++) {
                    roleStrings[i] = channelNames.get(i).getMask();
                }
                setOptions(roleStrings);
                return EmbedFactory.getEmbedDefault(this, getString("state2_description"), getString("state2_title"));

            default:
                return null;
        }
    }

    @Override
    public void onNavigationTimeOut(Message message) {}

    @Override
    public int getMaxReactionNumber() {
        return 5;
    }

    private String highlightVariables(String str) {
        return modules.MemberCountDisplay.replaceVariables(str, "`%MEMBERS`", "`%USERS`", "`%BOTS`", "`%BOOSTS`");
    }

}
