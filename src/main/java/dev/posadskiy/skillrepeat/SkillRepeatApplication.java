package dev.posadskiy.skillrepeat;

import dev.posadskiy.skillrepeat.db.UserRepository;
import dev.posadskiy.skillrepeat.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SkillRepeatApplication implements CommandLineRunner {

	@Autowired
	UserRepository userRepository;

	@Autowired
	UserService userService;

	public static void main(String[] args) {
		SpringApplication.run(SkillRepeatApplication.class, args);
	}

	@Override
	public void run(String... args) {
		//userRepository.deleteAll();
		userService.startUserNotificationService();
	}

}

