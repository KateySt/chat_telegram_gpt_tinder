package com.javarush.telegram;

import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import java.util.ArrayList;
import java.util.List;

public class TinderBoltApp extends MultiSessionTelegramBot {
    public static final String TELEGRAM_BOT_NAME = "MyBot";
    public static final String TELEGRAM_BOT_TOKEN = "";
    public static final String OPEN_AI_TOKEN = "";// token
    public DialogMode mode = DialogMode.MAIN;
    public ChatGPTService chatGPTService = new ChatGPTService(OPEN_AI_TOKEN);
    private List<String> chat;
    private UserInfo userInfo;
    private int questionNumber;

    public TinderBoltApp() {
        super(TELEGRAM_BOT_NAME, TELEGRAM_BOT_TOKEN);
    }

    @Override
    public void onUpdateEventReceived(Update update) {
        String message = getMessageText();

        switch (message) {
            case "/start" -> handleStart();
            case "/gpt" -> handleGpt();
            case "/date" -> handleDate();
            case "/message" -> handleMessage();
            case "/profile" -> handleProfile();
            default -> handleDefaultMessage(message);
        }
    }

    private void handleStart() {
        mode = DialogMode.MAIN;
        showMainMenu(
                "Main menu", "/start",
                "Chat gpt", "/gpt",
                "Generating a Tinder profile", "/profile",
                "Message for dating", "/opener",
                "List with your name", "/message",
                "Set the GPT chat name", "/date"
        );
        String menu = loadMessage("main");
        sendTextMessage(menu);
        sendPhotoMessage("main");
    }

    private void handleGpt() {
        mode = DialogMode.GPT;
        String gptMessage = loadMessage("gpt");
        sendTextMessage(gptMessage);
        sendPhotoMessage("gpt");
    }

    private void handleDate() {
        mode = DialogMode.DATE;
        String dateMessage = loadMessage("date");
        sendTextMessage(dateMessage);
        sendPhotoMessage("date");

        sendTextButtonsMessage(dateMessage,
                "Ariana Grande", "date_grande",
                "Margot Robbie", "date_robbie",
                "Zendaya", "date_zendaya",
                "Ryan Gosling", "date_gosling",
                "Tom Hardy", "date_hardy"
        );
    }

    private void handleProfile() {
        mode = DialogMode.PROFILE;
        sendPhotoMessage("profile");
        String profileMessage = loadMessage("profile");
        sendTextButtonsMessage(profileMessage);
        userInfo = new UserInfo();
        questionNumber = 1;
        sendTextMessage("What is your name?");
    }


    private void handleMessage() {
        mode = DialogMode.MESSAGE;
        sendPhotoMessage("message");
        String gptMessageHelper = loadMessage("message");
        sendTextButtonsMessage(gptMessageHelper,
                "Next message", "message_next",
                "Date", "message_date"
        );
        chat = new ArrayList<>();
    }

    private void handleDefaultMessage(String message) {
        if (mode == DialogMode.GPT) {
            handleGptResponse(message);
        } else if (mode == DialogMode.DATE) {
            handleDateResponse(message);
        } else if (mode == DialogMode.MESSAGE) {
            handleMessageResponse(message);
        } else if (mode == DialogMode.PROFILE) {
            handleProfileResponse(message);
        }
    }

    private void handleProfileResponse(String message) {

        if (questionNumber >= 1 && questionNumber <= 10) {
            switch (questionNumber) {
                case 1 -> userInfo.name = message;
                case 2 -> userInfo.sex = message;
                case 3 -> userInfo.age = message;
                case 4 -> userInfo.city = message;
                case 5 -> userInfo.occupation = message;
                case 6 -> userInfo.hobby = message;
                case 7 -> userInfo.handsome = message;
                case 8 -> userInfo.wealth = message;
                case 9 -> userInfo.annoys = message;
                case 10 -> userInfo.goals = message;
            }

            questionNumber++;
            if (questionNumber <= 10) {
                askNextQuestion();
            } else {
                finalizeProfile();
            }
        }
    }

    private void finalizeProfile() {
        questionNumber = 1;
        sendTextMessage("Profile completed! Here’s what you’ve shared: " + userInfo.toString());
        String prompt = loadPrompt("profile");
        String answer = chatGPTService.sendMessage(prompt, userInfo.toString());
        sendTextMessage(answer);
    }


    private void askNextQuestion() {
        String question;
        switch (questionNumber) {
            case 2 -> question = "What is your sex?";
            case 3 -> question = "How old are you?";
            case 4 -> question = "Which city do you live in?";
            case 5 -> question = "What is your occupation?";
            case 6 -> question = "What are your hobbies?";
            case 7 -> question = "On a scale from 1-10, how handsome do you consider yourself?";
            case 8 -> question = "On a scale from 1-10, how wealthy do you consider yourself?";
            case 9 -> question = "What annoys you the most?";
            case 10 -> question = "What are your goals?";
            default -> question = "I don't have any more questions for you.";
        }
        sendTextMessage(question);
    }

    private void handleGptResponse(String message) {
        String prompt = loadMessage("gpt");
        String answer = chatGPTService.sendMessage(prompt, message);
        sendTextMessage(answer);
    }

    private void handleDateResponse(String message) {
        String query = getCallbackQueryButtonKey();

        if (query.startsWith("date_")) {
            sendPhotoMessage(query);
            String prompt = loadMessage(query);
            chatGPTService.setPrompt(prompt);
        } else {
            String answer = chatGPTService.addMessage(message);
            sendTextMessage(answer);
        }
    }

    private void handleMessageResponse(String message) {
        String query = getCallbackQueryButtonKey();

        if (query.startsWith("message_")) {
            String prompt = loadMessage(query);
            String history = String.join("/n/n", chat);

            Message msg = sendTextMessage("...Loading");
            String answer = chatGPTService.sendMessage(prompt, history);
            updateTextMessage(msg, answer);
        }

        chat.add(message);
    }

    public static void main(String[] args) throws TelegramApiException {
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
        telegramBotsApi.registerBot(new TinderBoltApp());
    }
}
