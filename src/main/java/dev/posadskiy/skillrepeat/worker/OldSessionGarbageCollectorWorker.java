package dev.posadskiy.skillrepeat.worker;

import dev.posadskiy.skillrepeat.db.SessionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;

import static dev.posadskiy.skillrepeat.controller.SessionControllerImpl.SESSION_LIFE_TIME;

public class OldSessionGarbageCollectorWorker {
	private static final String CRON_EVERY_DAY_AT_3_AM = "0 0 3 * * *";

	@Autowired
	private SessionRepository sessionRepository;

	@Scheduled(cron = CRON_EVERY_DAY_AT_3_AM)
	public void cleanOldSessions() {
		sessionRepository.findAll()
			.stream()
			.filter((s) -> s.getTime() < System.currentTimeMillis() - SESSION_LIFE_TIME)
			.forEach((s) -> sessionRepository.delete(s));
	}
}