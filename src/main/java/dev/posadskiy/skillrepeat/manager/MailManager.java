package dev.posadskiy.skillrepeat.manager;

import dev.posadskiy.skillrepeat.service.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MailManager {

	@Autowired
	private MailService mailService;

	public void sendWelcomeMessage(String email, String hash) {
		mailService.sendWelcomeMessage(email, hash);
	}

	public void sendForgotPasswordMessage(String email, String hash) {
		mailService.sendResetPasswordMessage(email, hash);
	}

}
