package dev.posadskiy.skillrepeat.controller.impl;

import dev.posadskiy.skillrepeat.controller.ConfirmEmailController;
import dev.posadskiy.skillrepeat.db.ConfirmEmailRepository;
import dev.posadskiy.skillrepeat.db.model.DbConfirmEmail;
import dev.posadskiy.skillrepeat.exception.ConfirmEmailDoesNotExistException;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;

public class ConfirmEmailControllerImpl implements ConfirmEmailController {

	@Autowired
	ConfirmEmailRepository repository;

	public DbConfirmEmail create(String userId) {
		DbConfirmEmail dbConfirmEmail = new DbConfirmEmail();

		dbConfirmEmail.setUserId(userId);
		dbConfirmEmail.setHash(RandomStringUtils.randomAlphabetic(10));
		dbConfirmEmail.setTime(System.currentTimeMillis());

		return repository.save(dbConfirmEmail);
	}

	@Override
	public DbConfirmEmail getByHash(String hash) {
		DbConfirmEmail foundConfirmEmail = repository.findByHash(hash);
		if (foundConfirmEmail == null) {
			throw new ConfirmEmailDoesNotExistException();
		}

		return foundConfirmEmail;
	}

	@Override
	public void delete(DbConfirmEmail confirmEmail) {
		repository.delete(confirmEmail);
	}
}
