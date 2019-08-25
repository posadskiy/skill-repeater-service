package dev.posadskiy.skillrepeat.service;

import dev.posadskiy.skillrepeat.mail.MailTemplateEngine;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Slf4j
@Component
public class MailService {

	@Autowired
	private JavaMailSender mailSender;

	@Autowired
	private MailTemplateEngine mailTemplateEngine;

	public void sendMessage(String to, String subject, String text) {
		SimpleMailMessage message = new SimpleMailMessage();
		message.setTo(to);
		message.setSubject(subject);
		message.setText(text);

		mailSender.send(message);
	}

	public void sendRepeatMessage(String to, String subject, String skills, String date, String time) {
		MimeMessage mimeMessage = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(mimeMessage);
		try {
			helper.setTo(to);
			helper.setSubject(subject);
			helper.setText(mailTemplateEngine.generateRepeatEmailHtml(skills, date, time), true);
			mailSender.send(mimeMessage);
		} catch (MessagingException e) {
			log.info("Error when sending email message", e);
		}
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