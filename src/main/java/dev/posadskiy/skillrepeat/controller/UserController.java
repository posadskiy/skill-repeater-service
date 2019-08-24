package dev.posadskiy.skillrepeat.controller;

import dev.posadskiy.skillrepeat.dto.User;
import dev.posadskiy.skillrepeat.rest.RequestWrapper;

import java.util.List;

public interface UserController {
	List<User> getAll(RequestWrapper requestWrapper);

	User getUserById(RequestWrapper requestWrapper);

	User findByName(RequestWrapper requestWrapper);

	User updateUser(RequestWrapper requestWrapper);

	User addSkill(RequestWrapper requestWrapper);

	User editSkill(RequestWrapper requestWrapper);

	User deleteSkill(RequestWrapper requestWrapper);

	void deleteUser(RequestWrapper requestWrapper);

	User repeatSkill(RequestWrapper requestWrapper);

	void changeRoles(RequestWrapper requestWrapper);

	void changePassword(RequestWrapper requestWrapper);

	User changeEmail(RequestWrapper requestWrapper);

	User changeNotification(RequestWrapper requestWrapper);
}