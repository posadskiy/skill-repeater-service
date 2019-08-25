package dev.posadskiy.skillrepeat.worker;

import dev.posadskiy.skillrepeat.SystemSetting;
import dev.posadskiy.skillrepeat.db.UserRepository;
import dev.posadskiy.skillrepeat.db.model.DbSkill;
import dev.posadskiy.skillrepeat.db.model.DbUser;
import dev.posadskiy.skillrepeat.service.MailService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static dev.posadskiy.skillrepeat.SystemSetting.DAYS_BETWEEN_NOTIFICATIONS;

@Slf4j
public class EmailNotificationWorker {
	@Autowired
	private UserRepository userRepository;

	@Autowired
	private MailService mailService;

	@Scheduled(initialDelay = 6000000, fixedDelay = 900000)
	public void sendEmailNotification() {
		log.info("Email notification worker started");
		List<DbUser> users = userRepository.findAll();
		users.forEach((user) -> {
			if (user.getIsAgreeGetEmails() == null
				|| !user.getIsAgreeGetEmails()
				|| StringUtils.isEmpty(user.getEmail())
				|| !user.getEmail().contains("@")
				|| CollectionUtils.isEmpty(user.getSkills())) return;

			List<DbSkill> skills = user.getSkills().stream().filter((skill) -> {
				int notificationPeriod = Math.min(skill.getPeriod(), DAYS_BETWEEN_NOTIFICATIONS);
				if (skill.getLastNotification() != null && Instant.ofEpochMilli(skill.getLastNotification().getTime()).atZone(ZoneId.systemDefault()).toLocalDateTime().plusDays(notificationPeriod).isAfter(LocalDateTime.now())) {
					return false;
				}

				Integer period = SystemSetting.DEFAULT_PERIOD;
				if (skill.getPeriod() != null) {
					period = skill.getPeriod();
				} else if (user.getPeriod() != null) {
					period = user.getPeriod();
				}

				LocalDate lastRepeatTime = Instant.ofEpochMilli(skill.getLastRepeat().getTime()).atZone(ZoneId.systemDefault()).toLocalDate();
				LocalDate addedTime = lastRepeatTime.plusDays(period);
				LocalDate nowDate = LocalDate.now();
				if (!addedTime.isBefore(nowDate)) {
					return false;
				}

				String time = SystemSetting.DEFAULT_TIME;
				if (skill.getTime() != null) {
					time = skill.getTime();
				} else if (user.getTime() != null) {
					time = user.getTime();
				}

				String[] timeParts = time.split(":");

				LocalDateTime nowTime = LocalDateTime.now();
				LocalDateTime repeatTime = nowTime.withHour(Integer.parseInt(timeParts[0])).withMinute(Integer.parseInt(timeParts[1]));
				return repeatTime.isBefore(nowTime);
			})
				.peek((skill) -> skill.setLastNotification(new Date()))
				.collect(Collectors.toList());

			if (CollectionUtils.isEmpty(skills)) return;

			List<String> skillNames = skills.stream().map(DbSkill::getName).collect(Collectors.toList());
			String joinedSkills = StringUtils.join(skillNames, ", ");

			mailService.sendRepeatMessage(user.getEmail(), "Time to train skills", joinedSkills, LocalDate.now().format(DateTimeFormatter.ofPattern("d MMM")), skills.get(0).getTime());
		});
		log.info("Email notification worker finished");
	}
}
