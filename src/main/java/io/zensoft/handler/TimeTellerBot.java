package io.zensoft.handler;

import io.zensoft.command.HelpCommand;
import io.zensoft.command.StartCommand;
import io.zensoft.service.Emoji;
import io.zensoft.util.TimeTellerBotUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.bots.TelegramLongPollingCommandBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;

import java.util.Arrays;

@Component
public class TimeTellerBot extends TelegramLongPollingCommandBot {

    private final HelpCommand helpCommand;
    private final StartCommand startCommand;

    @Autowired
    public TimeTellerBot(HelpCommand helpCommand, StartCommand startCommand) {
        this.helpCommand = helpCommand;
        this.startCommand = startCommand;
        helpCommand.setiCommandRegistry(this);
        register(helpCommand);
        register(startCommand);
    }

    @Override
    public String getBotUsername() {
        return TimeTellerBotUtil.USER_NAME;
    }

    @Override
    public String getBotToken() {
        return TimeTellerBotUtil.TOKEN;
    }

    @Override
    public void processNonCommandUpdate(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            SendMessage message = handleMessage(update.getMessage());
           try {
                sendMessage(message);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
    }

    private SendMessage handleMessage(Message message) {
        return new SendMessage()
                .setChatId(message.getChatId())
                .setText(message.getText() + " zzzz from me")
                .setReplyMarkup(getReply());
    }

    private ReplyKeyboard getReply() {
        KeyboardRow keyboardFirstRow = new KeyboardRow();
        keyboardFirstRow.add(getStartCommand());
        keyboardFirstRow.add(getHelpCommand());
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboad(true);
        replyKeyboardMarkup.setKeyboard(Arrays.asList(keyboardFirstRow));

        return replyKeyboardMarkup;
    }

    private String getStartCommand() {
        return  Emoji.BLACK_RIGHT_POINTING_TRIANGLE.toString() + " start";
    }

    private String getHelpCommand() {
        return Emoji.AMBULANCE.toString() + " help";
    }
}
