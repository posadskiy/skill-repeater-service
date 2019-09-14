package dev.posadskiy.skillrepeat.manager;

import dev.posadskiy.skillrepeat.controller.TelegramAuthCodeController;
import dev.posadskiy.skillrepeat.db.model.DbTelegramAuthCode;
import org.springframework.beans.factory.annotation.Autowired;

public class TelegramManager {

	@Autowired
	private TelegramAuthCodeController controller;

	public DbTelegramAuthCode createTelegramLink(String userId) {
		return controller.create(userId);
	}

}