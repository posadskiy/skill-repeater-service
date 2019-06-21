package dev.posadskiy.skillrepeat.controller;

import dev.posadskiy.skillrepeat.dto.Skill;
import dev.posadskiy.skillrepeat.dto.User;

import java.util.List;

public interface UserController {
	List<User> getAll(String sessionId);

	User getUserById(String id, String sessionId);

	User findByName(String name, String sessionId);

	User addUser(User user, String sessionId);

	User updateUser(User user, String sessionId);

	User addSkill(String id, List<Skill> skills, String sessionId);

	void deleteUser(String id, String sessionId);

	User repeatSkill(String userId, String skillId, String sessionId);
}