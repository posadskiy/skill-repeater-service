package dev.posadskiy.skillrepeat.mail;

import dev.posadskiy.skillrepeat.db.UserRepository;
import dev.posadskiy.skillrepeat.db.model.DbSkill;
import dev.posadskiy.skillrepeat.db.model.DbUser;
import dev.posadskiy.skillrepeat.service.MailService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.TimerTask;
import java.util.stream.Collectors;

@Component
public class UserNotificationTask extends TimerTask {

	@Autowired
	private UserRepository repository;

	@Autowired
	private MailService mailService;

	@Override
	public void run() {
		List<DbUser> users = repository.findAll();
		users.forEach((user) -> {
			if (StringUtils.isEmpty(user.getEmail()) || !user.getEmail().contains("@") || CollectionUtils.isEmpty(user.getSkills())) return;

			List<DbSkill> skills = user.getSkills().stream().filter((skill) -> {
				LocalDateTime time = Instant.ofEpochMilli(skill.getLastRepeat().getTime()).atZone(ZoneId.systemDefault()).toLocalDateTime();
				LocalDateTime plusedTime = time.plusDays(14);
				return plusedTime.isBefore(LocalDateTime.now());

			}).collect(Collectors.toList());

			if (CollectionUtils.isEmpty(skills)) return;

			List<String> skillNames = skills.stream().map(DbSkill::getName).collect(Collectors.toList());
			String joinedSkills = StringUtils.join(skillNames, ", ");

			mailService.sendMessage(user.getEmail(), "Time to train skills", "Hello, " + user.getName() + "!\n\n" +
				"You haven't trained skill for 2 weeks. Time to repeat them!\n" +
				"Your forgettable skills: " + joinedSkills + ".\n\n" +
				"Skill Repeater Team.\n\n" +
				"You can unsubscribe on personal page.");
		});
	}
}
