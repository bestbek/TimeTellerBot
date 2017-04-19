package io.zensoft;

import io.zensoft.handler.TimeTellerBot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.TelegramBotsApi;
import org.telegram.telegrambots.exceptions.TelegramApiRequestException;

@SpringBootApplication
public class TimetellerApplication implements CommandLineRunner {

	@Autowired
	private TimeTellerBot timeTellerBot;

	static {
		ApiContextInitializer.init();
	}

	public static void main(String[] args) {
		SpringApplication.run(TimetellerApplication.class, args);
	}

	@Override
	public void run(String... strings) throws Exception {
		TelegramBotsApi botsApi = new TelegramBotsApi();
		try {
			botsApi.registerBot(timeTellerBot);
		} catch (TelegramApiRequestException e) {
			e.printStackTrace();
		}
	}
}
