package dev.posadskiy.skillrepeat.mail;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

@Component
public class MailTemplateEngine {

	@Autowired
	private TemplateEngine templateEngine;

	public String generateRepeatEmailHtml(String skills, String date, String time) {
		Map<String, Object> variables = new HashMap<>();
		variables.put("skills", skills);
		variables.put("date", date);
		variables.put("time", time);

		final String templateFileName = "repeat";

		return this.templateEngine.process(templateFileName, new Context(Locale.ROOT, variables));
	}

	public String generateRepeatPasswordEmailHtml(String hash) {
		Map<String, Object> variables = new HashMap<>();
		variables.put("hash", hash);

		final String templateFileName = "reset";

		return this.templateEngine.process(templateFileName, new Context(Locale.ROOT, variables));
	}

	public String generateWelcomeEmailHtml(String hash) {
		Map<String, Object> variables = new HashMap<>();
		variables.put("hash", hash);

		final String templateFileName = "welcome";

		return this.templateEngine.process(templateFileName, new Context(Locale.ROOT, variables));
	}
}
