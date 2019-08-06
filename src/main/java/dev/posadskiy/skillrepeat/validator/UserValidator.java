package dev.posadskiy.skillrepeat.validator;

import dev.posadskiy.skillrepeat.dto.User;
import dev.posadskiy.skillrepeat.exception.UserValidationException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

public class UserValidator {

	public void userValidate(User user) {
		userNameValidate(user.getName());
		emailValidate(user.getEmail());
	}

	public void userAccountUpdateValidate(User user) {
		userNameValidate(user.getName());
	}

	public void passwordValidate(String password) {
		if (StringUtils.isBlank(password)) {
			throw new UserValidationException("Password is empty or blank");
		}
	}

	private void userNameValidate(String name) {
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
