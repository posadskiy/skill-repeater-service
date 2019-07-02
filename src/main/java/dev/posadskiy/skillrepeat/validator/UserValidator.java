package dev.posadskiy.skillrepeat.validator;

import dev.posadskiy.skillrepeat.dto.User;
import dev.posadskiy.skillrepeat.exception.UserValidationException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

public class UserValidator {

	public void userValidate(User user) {
		loginValidate(user.getLogin());
		userNameValidate(user.getName());
		emailValidate(user.getEmail());
	}

	public void passwordValidate(String password) {
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

		if (!StringUtils.isAsciiPrintable(login)) {
			throw new UserValidationException("Login consists unsupported letters");
		}
	}

	private void userNameValidate(String name) {
		if (StringUtils.isBlank(name)) {
			throw new UserValidationException("Name is empty or blank");
		}

		if (name.length() < 2) {
			throw new UserValidationException("Name is too short");
		}

		if (!StringUtils.isAsciiPrintable(name)) {
			throw new UserValidationException("Name consists unsupported letters");
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
