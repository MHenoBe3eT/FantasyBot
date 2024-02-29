package ru.artur.vlasov.LaligaFantasy.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.artur.vlasov.LaligaFantasy.config.*;
import ru.artur.vlasov.LaligaFantasy.model.Deadline;
import ru.artur.vlasov.LaligaFantasy.model.Player;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Component
public class TelegramBot extends TelegramLongPollingBot {
    final BotConfig config;

    public TelegramBot(BotConfig config) {
        this.config = config;
    }

    @Override
    public String getBotUsername() {
        return config.getBotName();
    }

    @Override
    public String getBotToken() {
        return config.getToken();
    }

    @Override
    public void onUpdateReceived(Update update) {

        Message message = update.getMessage();

        if (update.hasMessage() && update.getMessage().hasText()) {
            String messageText = update.getMessage().getText();
            long chatId = update.getMessage().getChatId();
            if (message.isUserMessage()) {
                if ((messageText.contains("htt")) && (messageText.contains("EPL"))) {
                    try {
                        startCommandReceived(chatId, extractLinkFromMessage(message.getText()),
                                "http://epl.soccerfantasy.ru/api/player?link=");
                    } catch (IOException e) {
                    }
                } else if ((messageText.contains("htt")) && (messageText.contains("LALIGA"))) {
                    try {
                        startCommandReceived(chatId, extractLinkFromMessage(message.getText()),
                                "http://laliga.soccerfantasy.ru/api/player?link=");
                    } catch (IOException e) {
                    }
                } else {
                    sendMessage(chatId, "Отправьте ссылку на sports.ru. Укажите в тексте сообщения EPL или LALIGA.");
                }
            } else if ((message.getText().startsWith("@" + getBotUsername()))) {
                if ((messageText.contains("htt")) && (chatId == -1001057666677L)) {
                    try {
                        startCommandReceived(chatId, extractLinkFromMessage(message.getText()),
                                "http://epl.soccerfantasy.ru/api/player?link=");
                    } catch (IOException e) {
                    }
                } else if ((messageText.contains("htt")) && (chatId == -1001148108113L)) {
                    try {
                        startCommandReceived(chatId, extractLinkFromMessage(message.getText()),
                                "http://laliga.soccerfantasy.ru/api/player?link=");
                    } catch (IOException e) {
                    }
                } else if ((messageText.contains("result")) && (chatId == -1001148108113L)) {
                    String text = message.getText();
                    int tourResult = Integer.parseInt(text.replaceAll("\\D", ""));
                    sendMessage(chatId, TourResult.getTourResult(tourResult));
                } else if ((messageText.contains("table")) && (chatId == -1001148108113L)) {
                    sendMessage(chatId, TournamentTable.getTournamentTable());
                } else {
                    sendMessage(chatId, "Отправьте ссылку sports.ru");
                }
            }
        }

    }


    private void startCommandReceived(long chatId, String message, String leagueUrl) throws IOException {
        Player player = new Player();
        String answer = PlayerInfo.getPlayersInfo(message, player, leagueUrl);

        sendMessage(chatId, answer);
    }

    private void sendMessage(long chatId, String textToSend) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(textToSend);

        try {
            execute(message);
        } catch (TelegramApiException e) {

        }


    }

    // обработка исключения (тут будут писаться логи)
    private void executeMessage(SendMessage message) {
        try {
            execute(message);
        } catch (TelegramApiException e) {

        }
    }

    // метод отправки сообщения для дедлайна
    private void prepareAndSendMessage(long chatId, String textToSend) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(textToSend);
        executeMessage(message);
    }


    @Scheduled(cron = "0 0 * * * *")
    private void eplDeadlineScheduler() throws IOException {
        long eplGroupChatId = -1001057666677L;
        String eplDeadlineURL = "https://epl.soccerfantasy.ru/api/deadline";
        String eplTeamsURL = "https://epl.soccerfantasy.ru/api/teams-without-squad";
        deadLineInfoSender(eplGroupChatId, eplDeadlineURL, eplTeamsURL);
    }

    @Scheduled(cron = "0 0 * * * *")
    private void laLigaDeadlineScheduler() throws IOException {
        long laligaGroupChatId = -1001148108113L;
        String laLigaDeadlineURL = "https://laliga.soccerfantasy.ru/api/deadline";
        String laLigaTeamsURL = "https://laliga.soccerfantasy.ru/api/teams-without-squad";
        deadLineInfoSender(laligaGroupChatId, laLigaDeadlineURL, laLigaTeamsURL);

    }

    public void deadLineInfoSender(long chatId, String urlTime, String urlTeams) throws IOException {
        Deadline deadline = new Deadline();
        DeadlineTime deadlineTime = new DeadlineTime();
        String answer = deadlineTime.getDeadlineTime(deadline, urlTime, urlTeams);
        if ((deadline.getSecondsBeforeTour() / 3600) == 24) {
            prepareAndSendMessage(chatId, answer);
        } else if ((deadline.getSecondsBeforeTour() / 3600) == 12) {
            prepareAndSendMessage(chatId, answer);
        } else if ((deadline.getSecondsBeforeTour() / 3600) == 6) {
            prepareAndSendMessage(chatId, answer);
        }
    }

    private String extractLinkFromMessage(String message) {
        Pattern pattern = Pattern.compile("htt\\S+");
        Matcher matcher = pattern.matcher(message);

        if (matcher.find()) {
            return matcher.group();
        }

        return "ссылка не верная (экстракт)"; // If no link found
    }
}
