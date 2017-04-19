package io.zensoft.command;

import io.zensoft.repository.DatabaseManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Chat;
import org.telegram.telegrambots.api.objects.User;
import org.telegram.telegrambots.bots.AbsSender;
import org.telegram.telegrambots.bots.commands.BotCommand;
import org.telegram.telegrambots.bots.commands.ICommandRegistry;
import org.telegram.telegrambots.exceptions.TelegramApiException;

@Component
@Slf4j
public class HelpCommand extends BotCommand {

    public void setiCommandRegistry(ICommandRegistry iCommandRegistry) {
        this.iCommandRegistry = iCommandRegistry;
    }

    private ICommandRegistry iCommandRegistry;
    private final DatabaseManager databaseManager;

    @Autowired
    public HelpCommand(DatabaseManager databaseManager) {
        super("help", "List bot`s commands");
        this.databaseManager = databaseManager;
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] arguments) {
        StringBuilder stringBuilder = new StringBuilder();
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chat.getId().toString());
        sendMessage.enableHtml(true);
        if(databaseManager.isUserExist(user.getId().longValue())) {
            stringBuilder.append("These are the registered commands for this Bot:\n\n");
            for (BotCommand botCommand : iCommandRegistry.getRegisteredCommands()) {
                stringBuilder.append(botCommand.toString()).append("\n");
            }
        } else {
            stringBuilder.append("unauthorized user:\n\n");
        }
        sendMessage.setText(stringBuilder.toString());
        try {
            absSender.sendMessage(sendMessage);
        } catch (TelegramApiException e) {
            log.error("could not send message error:\n" + e.getMessage());
        }
    }
}
