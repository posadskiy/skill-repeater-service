package dev.posadskiy.skillrepeat.controller;

import dev.posadskiy.skillrepeat.db.model.DbConfirmEmail;

public interface ConfirmEmailController {

	DbConfirmEmail create(String userId);

	DbConfirmEmail getByHash(String hash);

	void delete(DbConfirmEmail confirmEmail);
}
