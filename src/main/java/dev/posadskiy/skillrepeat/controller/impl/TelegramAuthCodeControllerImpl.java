package dev.posadskiy.skillrepeat.controller.impl;

import dev.posadskiy.skillrepeat.controller.TelegramAuthCodeController;
import dev.posadskiy.skillrepeat.db.TelegramAuthCodeRepository;
import dev.posadskiy.skillrepeat.db.model.DbTelegramAuthCode;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;

public class TelegramAuthCodeControllerImpl implements TelegramAuthCodeController {

	@Autowired
	private TelegramAuthCodeRepository repository;

	@Override
	public DbTelegramAuthCode create(String userId) {
		DbTelegramAuthCode dbTelegramAuthCode = new DbTelegramAuthCode();

		dbTelegramAuthCode.setUserId(userId);
		dbTelegramAuthCode.setHash(RandomStringUtils.randomAlphabetic(10));
		dbTelegramAuthCode.setTime(System.currentTimeMillis());

		return repository.save(dbTelegramAuthCode);
	}

	@Override
	public DbTelegramAuthCode getByHash(String hash) {
		return repository.findByHash(hash);
	}

	@Override
	public void delete(DbTelegramAuthCode dbTelegramAuthCode) {
		repository.delete(dbTelegramAuthCode);
	}
}
