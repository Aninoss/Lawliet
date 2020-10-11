package commands.runnables.fisherycategory;

import commands.listeners.CommandProperties;
import commands.listeners.OnReactionAddStaticListener;
import commands.listeners.OnTrackerRequestListener;
import commands.runnables.FisheryAbstract;
import constants.*;
import core.DiscordApiCollection;
import core.EmbedFactory;
import core.PermissionCheckRuntime;
import core.TextManager;
import core.utils.StringUtil;
import core.utils.TimeUtil;
import javafx.util.Pair;
import mysql.modules.survey.*;
import mysql.modules.tracker.TrackerBeanSlot;
import org.javacord.api.entity.channel.ServerTextChannel;
import org.javacord.api.entity.message.Message;
import org.javacord.api.entity.message.Reaction;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.entity.server.Server;
import org.javacord.api.entity.user.User;
import org.javacord.api.event.message.MessageCreateEvent;
import org.javacord.api.event.message.reaction.ReactionAddEvent;
import java.io.IOException;
import java.sql.SQLException;
import java.time.Instant;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

@CommandProperties(
    trigger = "survey",
    botPermissions = Permission.MANAGE_MESSAGES,
    emoji = "✅",
    executable = true
)
public class SurveyCommand extends FisheryAbstract implements OnReactionAddStaticListener, OnTrackerRequestListener {

    private static final String BELL_EMOJI = "🔔";

    public SurveyCommand(Locale locale, String prefix) {
        super(locale, prefix);
    }

    @Override
    public boolean onMessageReceivedSuccessful(MessageCreateEvent event, String followedString) throws Throwable {
        sendMessages(event.getServerTextChannel().get(), event.getMessageAuthor().asUser().get(), false, event.getMessage().getUserAuthor().get());
        return true;
    }

    @Override
    public void onReactionAddStatic(Message message, ReactionAddEvent event) throws Throwable {
        if (!PermissionCheckRuntime.getInstance().botHasPermission(getLocale(), getClass(), event.getServerTextChannel().get(), Permission.MANAGE_MESSAGES)) return;
        event.removeReaction().get();

        removeUserReactions(message);
        String emoji = event.getEmoji().asUnicodeEmoji().get();
        if (event.getEmoji().isUnicodeEmoji()) {
            for (byte i = 0; i < 2; i++) {
                int type = 0;
                if (emoji.equals(LetterEmojis.LETTERS[i])) type = 1;
                if (emoji.equals(LetterEmojis.RED_LETTERS[i])) type = 2;
                if (emoji.equals(BELL_EMOJI)) type = 3;

                if (type > 0) {
                    SurveyBean surveyBean = DBSurvey.getInstance().getCurrentSurvey();

                    if (message.getCreationTimestamp().isAfter(surveyBean.getStartDate().atStartOfDay(ZoneId.systemDefault()).toInstant())) {
                        if (!registerVote(event, surveyBean, type, i)) return;
                        EmbedBuilder eb = getVoteStatusEmbed(event, surveyBean);
                        event.getUser().sendMessage(eb);
                    }
                    break;
                }
            }
        }
    }

    private EmbedBuilder getVoteStatusEmbed(ReactionAddEvent event, SurveyBean surveyBean) throws IOException {
        SurveyQuestion surveyQuestion = surveyBean.getSurveyQuestionAndAnswers(getLocale());
        String[] voteStrings = new String[2];

        voteStrings[0] = "• " + surveyQuestion.getAnswers()[surveyBean.getFirstVotes().get(event.getUser().getId()).getVote()];

        List<SurveySecondVote> surveySecondVotes = surveyBean.getSurveySecondVotesForUserId(event.getUser().getId());

        if (surveySecondVotes.size() == 0) voteStrings[1] = TextManager.getString(getLocale(), TextManager.GENERAL, "notset");
        else voteStrings[1] = "";

        for (SurveySecondVote surveySecondVote: surveySecondVotes) {
            voteStrings[1] += "• " + surveyQuestion.getAnswers()[surveySecondVote.getVote()] + " (" + StringUtil.escapeMarkdown(DiscordApiCollection.getInstance().getServerById(surveySecondVote.getServerId()).get().getName()) + ")\n";
        }

        return EmbedFactory.getCommandEmbedStandard(this, getString("vote_description") + "\n" + Emojis.EMPTY_EMOJI)
                .addField(surveyQuestion.getQuestion(), voteStrings[0])
                .addField(getString("majority"), voteStrings[1])
                .addField(Emojis.EMPTY_EMOJI, getString("vote_notification", StringUtil.getOnOffForBoolean(getLocale(), surveyBean.hasNotificationUserId(event.getUser().getId()))));
    }

    private void removeUserReactions(Message message) throws ExecutionException, InterruptedException {
        for(Reaction reaction: message.getReactions()) {
            String emoji = reaction.getEmoji().asUnicodeEmoji().get();
            boolean correctEmoji = false;
            for (int i = 0; i < 2; i++) {
                if (reaction.getEmoji().isUnicodeEmoji() &&
                        (emoji.equals(LetterEmojis.LETTERS[i]) || emoji.equals(LetterEmojis.RED_LETTERS[i]) || emoji.equals(BELL_EMOJI))
                ) {
                    correctEmoji = true;
                    break;
                }
            }

            if (!correctEmoji) reaction.remove().get();
            else if (reaction.getCount() > 1) {
                for(User user: reaction.getUsers().get()) {
                    if (!user.isYourself()) reaction.removeUser(user).get();
                }
            }
        }
    }

    private boolean registerVote(ReactionAddEvent event, SurveyBean surveyBean, int type, byte i) {
        switch (type) {
            case 1:
                surveyBean.getFirstVotes().put(event.getUser().getId(), new SurveyFirstVote(event.getUser().getId(), i));
                return true;

            case 2:
                if (surveyBean.getFirstVotes().containsKey(event.getUser().getId())) {
                    surveyBean.getSecondVotes().put(
                            new Pair<>(event.getServer().get().getId(), event.getUser().getId()),
                            new SurveySecondVote(event.getServer().get().getId(), event.getUser().getId(), i)
                    );
                    return true;
                } else {
                    EmbedBuilder eb = EmbedFactory.getCommandEmbedError(this, getString("vote_error"), TextManager.getString(getLocale(), TextManager.GENERAL, "rejected"));
                    event.getUser().sendMessage(eb);
                    return false;
                }

            case 3:
                if (surveyBean.getFirstVotes().containsKey(event.getUser().getId())) {
                    surveyBean.toggleNotificationUserId(event.getUser().getId());
                    return true;
                } else {
                    EmbedBuilder eb = EmbedFactory.getCommandEmbedError(this, getString("vote_error"), TextManager.getString(getLocale(), TextManager.GENERAL, "rejected"));
                    event.getUser().sendMessage(eb);
                    return false;
                }

            default:
                return false;
        }
    }

    private Message sendMessages(ServerTextChannel channel, User userRequested, boolean tracker, User user) throws InterruptedException, IOException, SQLException, ExecutionException {
        SurveyBean currentSurvey = DBSurvey.getInstance().getCurrentSurvey();
        SurveyBean lastSurvey = DBSurvey.getInstance().getBean(currentSurvey.getSurveyId() - 1);

        //Results Message
        channel.sendMessage(getResultsEmbed(lastSurvey, channel.getServer(), user));

        //Survey Message
        EmbedBuilder eb = getSurveyEmbed(currentSurvey);
        if (!tracker) EmbedFactory.addTrackerNoteLog(getLocale(), channel.getServer(), userRequested, eb, getPrefix(), getTrigger());
        Message message = channel.sendMessage(eb).get();

        for(int i = 0; i < 2; i++) {
            for(int j = 0; j < 2; j++) {
                if (i == 0) message.addReaction(LetterEmojis.LETTERS[j]);
                else message.addReaction(LetterEmojis.RED_LETTERS[j]);
            }
        }
        message.addReaction(BELL_EMOJI);


        return message;
    }

    private EmbedBuilder getResultsEmbed(SurveyBean lastSurvey, Server server, User user) throws IOException {
        SurveyQuestion surveyQuestion = lastSurvey.getSurveyQuestionAndAnswers(getLocale());

        EmbedBuilder eb = EmbedFactory.getCommandEmbedStandard(this, "", getString("results_title"));
        eb.addField(getString("results_question"), surveyQuestion.getQuestion(), false);

        StringBuilder answerString = new StringBuilder();
        for(int i = 0; i < 2; i++) answerString.append(LetterEmojis.LETTERS[i]).append(" | ").append(surveyQuestion.getAnswers()[i]).append("\n");
        eb.addField(getString("results_answers"), answerString.toString(), false);

        long firstVotesTotal = lastSurvey.getFirstVoteNumber();
        long[] firstVotes = new long[2];
        for(byte i = 0; i < 2; i++) firstVotes[i] = lastSurvey.getFirstVoteNumbers(i);
        double[] firstVotesRelative = new double[2];
        for(byte i = 0; i < 2; i++) firstVotesRelative[i] = firstVotes[i] / (double)firstVotesTotal;

        StringBuilder resultString = new StringBuilder();
        for(int i = 0; i < 2; i++) {
            resultString.append(
                    getString("results_template",
                            LetterEmojis.LETTERS[i],
                            StringUtil.getBar(firstVotesRelative[i], 12),
                            StringUtil.numToString(getLocale(), firstVotes[i]),
                            StringUtil.numToString(getLocale(), Math.round(firstVotesRelative[i] * 100))
                    )
            ).append("\n");
        }

        eb.addField(getString("results_results", firstVotesTotal != 1, StringUtil.numToString(getLocale(), firstVotesTotal)), resultString.toString(), false);

        boolean individual = false;
        if (server != null && user != null && lastSurvey.getWon() != 2) {
            SurveySecondVote secondVote = lastSurvey.getSecondVotes().get(new Pair<>(server.getId(), user.getId()));
            if (secondVote != null) {
                individual = true;
                boolean won = lastSurvey.getWon() == 2 || lastSurvey.getWon() == secondVote.getVote();
                EmbedFactory.addLog(eb, won ? LogStatus.WIN : LogStatus.LOSE, getString("results_status", won));
            }
        }

        if (!individual) {
            EmbedFactory.addLog(eb, null, getString("results_won", lastSurvey.getWon(), surveyQuestion.getAnswers()[0], surveyQuestion.getAnswers()[1]));
        }

        return eb;
    }

    private EmbedBuilder getSurveyEmbed(SurveyBean surveyBean) throws IOException {
        SurveyQuestion surveyQuestion = surveyBean.getSurveyQuestionAndAnswers(getLocale());
        EmbedBuilder eb = EmbedFactory.getCommandEmbedStandard(this, getString("sdescription", BELL_EMOJI), getString("title") + Emojis.EMPTY_EMOJI);

        StringBuilder personalString = new StringBuilder();
        StringBuilder majorityString = new StringBuilder();
        for(int i = 0; i < 2; i++) {
            personalString.append(LetterEmojis.LETTERS[i]).append(" | ").append(surveyQuestion.getAnswers()[i]).append("\n");
            majorityString.append(LetterEmojis.RED_LETTERS[i]).append(" | ").append(surveyQuestion.getAnswers()[i]).append("\n");
        }
        eb.addField(surveyQuestion.getQuestion(), personalString.toString(), false);
        eb.addField(getString("majority"), majorityString.toString(), false);
        EmbedFactory.addLog(eb, LogStatus.TIME, getString("nextdate", TimeUtil.getRemainingTimeString(getLocale(), Instant.now(), TimeUtil.localDateToInstant(surveyBean.getNextDate()), false)));

        return eb;
    }

    @Override
    public String getTitleStartIndicator() {
        return getEmoji();
    }

    @Override
    public TrackerResult onTrackerRequest(TrackerBeanSlot slot) throws Throwable {
        SurveyBean currentSurvey = DBSurvey.getInstance().getCurrentSurvey();
        if(slot.getArgs().isPresent() && currentSurvey.getSurveyId() <= Integer.parseInt(slot.getArgs().get()))
            return TrackerResult.CONTINUE;

        ServerTextChannel channel = slot.getChannel().get();
        if (!PermissionCheckRuntime.getInstance().botHasPermission(getLocale(), getClass(), channel, Permission.ADD_REACTIONS))
            return TrackerResult.CONTINUE;

        slot.getMessage().ifPresent(Message::delete);

        slot.setMessageId(sendMessages(channel, null, true, null).getId());
        slot.setNextRequest(getNextSurveyInstant(Instant.now()));
        slot.setArgs(String.valueOf(currentSurvey.getSurveyId()));

        return TrackerResult.CONTINUE_AND_SAVE;
    }

    private Instant getNextSurveyInstant(Instant start) {
        do {
            start = TimeUtil.setInstantToNextDay(start);
        } while(!TimeUtil.instantHasWeekday(start, Calendar.MONDAY) && !TimeUtil.instantHasWeekday(start, Calendar.THURSDAY));
        return start;
    }

    @Override
    public boolean trackerUsesKey() {
        return false;
    }

}
