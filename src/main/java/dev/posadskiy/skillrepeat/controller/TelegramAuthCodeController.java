package dev.posadskiy.skillrepeat.controller;

import dev.posadskiy.skillrepeat.db.model.DbTelegramAuthCode;

public interface TelegramAuthCodeController {

	DbTelegramAuthCode create(String userId);

	DbTelegramAuthCode getByHash(String hash);

	void delete(DbTelegramAuthCode resetPassword);

}