package dev.posadskiy.skillrepeat.validator;

import dev.posadskiy.skillrepeat.dto.Auth;
import dev.posadskiy.skillrepeat.exception.UserValidationException;
import org.apache.commons.lang3.StringUtils;

public class AuthValidator {
	public void authValidate(Auth auth) {
		loginValidate(auth.getLogin());
		passwordValidate(auth.getPassword());
		emailValidate(auth.getEmail());
	}

	private void passwordValidate(String password) {
		if (StringUtils.isBlank(password)) {
			throw new UserValidationException("Password is empty or blank");
		}
	}

	private void loginValidate(String login) {
		if (StringUtils.isBlank(login)) {
			throw new UserValidationException("Name is empty or blank");
		}

		if (login.length() < 3) {
			throw new UserValidationException("Name is too short");
		}
	}

	private void emailValidate(String email) {
		if (StringUtils.isBlank(email)) {
			throw new UserValidationException("Email is empty or blank");
		}

		if (!email.contains("@")) {
			throw new UserValidationException("Email is not correct");
		}
	}
}
