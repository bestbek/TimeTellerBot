package io.zensoft.command;

import io.zensoft.model.TelegramUser;
import io.zensoft.repository.DatabaseManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Chat;
import org.telegram.telegrambots.api.objects.User;
import org.telegram.telegrambots.bots.AbsSender;
import org.telegram.telegrambots.bots.commands.BotCommand;
import org.telegram.telegrambots.exceptions.TelegramApiException;

@Component
@Slf4j
public class StartCommand extends BotCommand {

    private final DatabaseManager databaseManager;

    @Autowired
    public StartCommand(DatabaseManager databaseManager) {
        super("start", "This command starts your bot");
        this.databaseManager = databaseManager;
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] arguments) {
       String message;
        if(databaseManager.isUserExist(user.getId().longValue())) {
            message = String.format("Hello Mr. %s \nnice to see you again \nHow can I help you?", user.getLastName());
        } else {
            TelegramUser telegramUser = TelegramUser.builder()
                    .userName(user.getUserName())
                    .firstName(user.getFirstName())
                    .lastName(user.getLastName())
                    .id(user.getId().longValue())
                    .build();
            databaseManager.saveTelegramUser(telegramUser);
            message = String.format("Welcome Mr. %s ", user.getLastName());
        }
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chat.getId());
        sendMessage.setText(message + "\nrun /help command for more information");
        sendMessage.enableHtml(true);
        try {
            absSender.sendMessage(sendMessage);
        } catch (TelegramApiException e) {
            log.error("could not send message error:\n" + e.getMessage());
        }
    }
}
