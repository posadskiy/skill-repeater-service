package dev.posadskiy.skillrepeat.validator;

import dev.posadskiy.skillrepeat.dto.User;
import dev.posadskiy.skillrepeat.exception.UserValidationException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

public class UserValidator {

	public void userValidate(User user) {
		passwordValidate(user.getPassword());
		emailValidate(user.getEmail());
	}

	private void passwordValidate(String password) {
		if (StringUtils.isBlank(password)) {
			throw new UserValidationException("Password is empty or blank");
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
