package dev.posadskiy.skillrepeat.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
public class MailService {

	@Autowired
	private JavaMailSender mailSender;

	public void sendMessage(String to, String subject, String text) {
		SimpleMailMessage message = new SimpleMailMessage();
		message.setTo(to);
		message.setSubject(subject);
		message.setText(text);

		mailSender.send(message);
	}

	public void sendResetPasswordMessage(String to, String hash) {
		SimpleMailMessage message = new SimpleMailMessage();
		message.setTo(to);
		message.setSubject("Reset password for Skill Repeater");
		message.setText("Forgot your password?\n\n" +
			"No worries – it happens! \n" +
			"Simply click on the link below to get a new one. It's as easy as that.\n\n" +
			"https://server.posadskiy.space/user/resetPass/" + hash);

		mailSender.send(message);
	}
}