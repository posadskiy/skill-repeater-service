package dev.posadskiy.skillrepeat.telegram;

import dev.posadskiy.skillrepeat.controller.TelegramAuthCodeController;
import dev.posadskiy.skillrepeat.controller.UserController;
import dev.posadskiy.skillrepeat.db.model.DbTelegramAuthCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;

public class TelegramBot extends TelegramLongPollingBot {

	@Value("${telegram.bot.name}")
	private String telegramBotName;

	@Value("${telegram.bot.key}")
	private String telegramBotKey;

	@Autowired
	private TelegramAuthCodeController telegramAuthCodeController;

	@Autowired
	private UserController userController;

	@Override
	public void onUpdateReceived(Update update) {
		String text = update.getMessage().getText();
		if (text.contains("/start")) {
			String[] parts = text.split(" ");
			if (parts.length > 1) {
				String hash = parts[1];
				Long chatId = update.getMessage().getChatId();
				DbTelegramAuthCode foundTelegramAuthCode = telegramAuthCodeController.getByHash(hash);

				userController.appendChatIdToUser(foundTelegramAuthCode.getUserId(), chatId);

				telegramAuthCodeController.delete(foundTelegramAuthCode);

				SendMessage sendMessage = new SendMessage().setChatId(chatId);
				sendMessage.setText("Confirmed! Now you will get notifications about your skill via Telegram!");

				try {
					execute(sendMessage);
				} catch (TelegramApiException e) {
					e.printStackTrace();
				}
			}
		}
	}

	@Override
	public void onUpdatesReceived(List<Update> updates) {
		for (Update update : updates) {
			onUpdateReceived(update);
		}
	}

	@Override
	public String getBotUsername() {
		return "@" + telegramBotName;
	}

	@Override
	public String getBotToken() {
		return telegramBotKey;
	}
}
