package dev.posadskiy.skillrepeat.controller.impl;

import dev.posadskiy.skillrepeat.controller.ResetPasswordController;
import dev.posadskiy.skillrepeat.db.ResetPasswordRepository;
import dev.posadskiy.skillrepeat.db.model.DbResetPassword;
import dev.posadskiy.skillrepeat.exception.ResetPasswordDoesNotExistException;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;

public class ResetPasswordControllerImpl implements ResetPasswordController {

	@Autowired
	ResetPasswordRepository repository;

	public DbResetPassword create(String userId) {
		DbResetPassword dbConfirmEmail = new DbResetPassword();

		dbConfirmEmail.setUserId(userId);
		dbConfirmEmail.setHash(RandomStringUtils.randomAlphabetic(10));
		dbConfirmEmail.setTime(System.currentTimeMillis());

		return repository.save(dbConfirmEmail);
	}

	@Override
	public DbResetPassword getByHash(String hash) {
		DbResetPassword foundResetPassword = repository.findByHash(hash);
		if (foundResetPassword == null) {
			throw new ResetPasswordDoesNotExistException();
		}

		return foundResetPassword;
	}

	@Override
	public void delete(DbResetPassword resetPassword) {
		repository.delete(resetPassword);
	}
}
