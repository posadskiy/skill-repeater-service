package dev.posadskiy.skillrepeat.controller;

import dev.posadskiy.skillrepeat.db.model.DbUser;
import dev.posadskiy.skillrepeat.dto.User;
import dev.posadskiy.skillrepeat.rest.RequestWrapper;

public interface UserController {
	User getUserById(RequestWrapper requestWrapper);

	DbUser getUserById(String userId);

	User findByEmail(String email);

	User updateUser(RequestWrapper requestWrapper);

	User addSkill(RequestWrapper requestWrapper);

	User editSkill(RequestWrapper requestWrapper);

	User deleteSkill(RequestWrapper requestWrapper);

	void deleteUser(RequestWrapper requestWrapper);

	User repeatSkill(RequestWrapper requestWrapper);

	void changeRoles(RequestWrapper requestWrapper);

	void isPasswordMatch(RequestWrapper requestWrapper);

	User changeEmail(RequestWrapper requestWrapper);

	User changeNotification(RequestWrapper requestWrapper);

	User auth(final User user);

	User create(final User user);

	User confirmEmail(final String userId);

	User changePassword(String userId, User user);

	User appendChatIdToUser(String userId, Long chatId);

}