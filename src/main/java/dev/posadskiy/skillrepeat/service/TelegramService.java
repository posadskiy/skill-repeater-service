package dev.posadskiy.skillrepeat.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Component
public class TelegramService {

	@Value("${telegram.bot.key}")
	private String telegramKey;

	public void sendMessage(Long chatId, String joinedSkills) {
		new RestTemplate().getForObject("https://api.telegram.org/bot" + telegramKey + "/sendMessage?chat_id=" + chatId + "&text=" + createMessage(joinedSkills), String.class);
	}

	private String createMessage(String joinedSkills) {
		return ("Hello! It's time to repeat your skills: " + joinedSkills + "! Don't procrastinate!").replaceAll(" ", "+");
	}
}
