package dev.posadskiy.skillrepeat;

import dev.posadskiy.skillrepeat.db.UserRepository;
import dev.posadskiy.skillrepeat.db.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.ArrayList;

@SpringBootApplication
public class SkillRepeatApplication implements CommandLineRunner {

	@Autowired
	UserRepository userRepository;

	public static void main(String[] args) {
		SpringApplication.run(SkillRepeatApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		userRepository.deleteAll();

		ArrayList<String> skillsOfJohn = new ArrayList<>();
		skillsOfJohn.add("java");
		skillsOfJohn.add("javascript");

		ArrayList<String> skillsOfMary = new ArrayList<>();
		skillsOfMary.add("python");


		userRepository.save(new User("1","John", skillsOfJohn));
		userRepository.save(new User("2","Mary", skillsOfMary));

		for (User user : userRepository.findAll()) {
			System.out.println(user.getName());
		}

	}

}

