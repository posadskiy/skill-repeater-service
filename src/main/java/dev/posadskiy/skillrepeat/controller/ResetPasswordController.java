package dev.posadskiy.skillrepeat.controller;

import dev.posadskiy.skillrepeat.db.model.DbResetPassword;

public interface ResetPasswordController {

	DbResetPassword create(String userId);

	DbResetPassword getByHash(String hash);

	void delete(DbResetPassword resetPassword);

}
