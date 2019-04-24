package dev.posadskiy.skillrepeat.service;

import dev.posadskiy.skillrepeat.mail.UserNotificationTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Timer;

@Component
public class UserService {

	@Autowired
	UserNotificationTask task;

	public void startUserNotificationService() {
		Timer timer = new Timer(true);
		timer.scheduleAtFixedRate(task, 0, 24 * 60 * 60 * 1000);
	}
}