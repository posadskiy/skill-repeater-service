package dev.posadskiy.skillrepeat.rest;

import dev.posadskiy.skillrepeat.controller.*;
import dev.posadskiy.skillrepeat.db.model.DbConfirmEmail;
import dev.posadskiy.skillrepeat.db.model.DbResetPassword;
import dev.posadskiy.skillrepeat.db.model.DbSession;
import dev.posadskiy.skillrepeat.db.model.DbTelegramAuthCode;
import dev.posadskiy.skillrepeat.dto.Message;
import dev.posadskiy.skillrepeat.dto.Skill;
import dev.posadskiy.skillrepeat.dto.User;
import dev.posadskiy.skillrepeat.manager.CookieManager;
import dev.posadskiy.skillrepeat.manager.MailManager;
import dev.posadskiy.skillrepeat.manager.TelegramManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

import static dev.posadskiy.skillrepeat.manager.CookieManager.SESSION_COOKIE_NAME;

@RestController
@RequestMapping("/user")
public class UserEndpoint {
	private final UserController userController;
	private final SessionController sessionController;
	private final ConfirmEmailController confirmEmailController;
	private final ResetPasswordController resetPasswordController;
	private final MessageController messageController;
	private final MailManager mailManager;
	private final CookieManager cookieManager;
	private final TelegramManager telegramManager;

	@Autowired
	public UserEndpoint(UserController userController, SessionController sessionController, ConfirmEmailController confirmEmailController, ResetPasswordController resetPasswordController, MessageController messageController, MailManager mailManager, CookieManager cookieManager, TelegramManager telegramManager) {
		this.userController = userController;
		this.sessionController = sessionController;
		this.confirmEmailController = confirmEmailController;
		this.resetPasswordController = resetPasswordController;
		this.messageController = messageController;
		this.mailManager = mailManager;
		this.cookieManager = cookieManager;
		this.telegramManager = telegramManager;
	}

	@GetMapping("/id/{id}")
	public User getUserById(@PathVariable("id") final String id,
							@CookieValue(SESSION_COOKIE_NAME) final String sessionId) {
		return userController.getUserById(new RequestWrapper().userId(id).sessionId(sessionId));
	}

	@PostMapping("/")
	public User updateUser(@RequestBody final User user,
						   @CookieValue(SESSION_COOKIE_NAME) final String sessionId) {
		return userController.updateUser(new RequestWrapper().data(user).userId(user.getId()).sessionId(sessionId));
	}

	@PostMapping("/{userId}/skill/add")
	public User addSkill(@PathVariable("userId") final String userId,
						 @RequestBody final List<Skill> skills,
						 @CookieValue(SESSION_COOKIE_NAME) final String sessionId) {
		return userController.addSkill(new RequestWrapper().data(skills).userId(userId).sessionId(sessionId));
	}

	@PostMapping("/{userId}/skill/edit")
	public User editSkill(@PathVariable("userId") final String userId,
						  @RequestBody final Skill skill,
						  @CookieValue(SESSION_COOKIE_NAME) final String sessionId) {
		return userController.editSkill(new RequestWrapper().data(skill).userId(userId).sessionId(sessionId));
	}

	@DeleteMapping("/{userId}/skill/{skillId}")
	public User deleteSkill(@PathVariable("userId") final String userId,
							@PathVariable final String skillId,
							@CookieValue(SESSION_COOKIE_NAME) final String sessionId) {
		return userController.deleteSkill(new RequestWrapper().data(skillId).userId(userId).sessionId(sessionId));
	}

	@DeleteMapping("/{id}")
	public void deleteUser(@PathVariable(value = "id") final String userId,
						   @CookieValue(SESSION_COOKIE_NAME) final String sessionId) {
		userController.deleteUser(new RequestWrapper().userId(userId).sessionId(sessionId));
	}

	@GetMapping("/{userId}/skill/repeat/{skillId}")
	public User repeatSkill(@PathVariable(value = "userId") final String userId,
							@PathVariable(value = "skillId") final String skillId,
							@CookieValue(SESSION_COOKIE_NAME) final String sessionId) {
		return userController.repeatSkill(new RequestWrapper().data(skillId).userId(userId).sessionId(sessionId));
	}

	@PostMapping("/auth")
	public User auth(@RequestBody final User user,
					 final HttpServletRequest request,
					 final HttpServletResponse response) {
		User foundUser = userController.auth(user);
		DbSession dbSession = sessionController.create(request.getSession().getId(), foundUser.getId());
		response.addCookie(cookieManager.createCookie(dbSession.getId()));

		return foundUser;
	}

	@PostMapping("/reg")
	public User registration(@RequestBody final User user,
							 final HttpServletRequest request,
							 final HttpServletResponse response) {

		User createdUser = userController.create(user);
		DbConfirmEmail conformEmail = confirmEmailController.create(createdUser.getId());
		mailManager.sendWelcomeMessage(createdUser.getEmail().toLowerCase(), conformEmail.getHash());

		DbSession createdSession = sessionController.create(request.getSession().getId(), createdUser.getId());
		response.addCookie(cookieManager.createCookie(createdSession.getId()));

		return createdUser;
	}

	@GetMapping("/confirmEmail/{hash}")
	public User confirmEmail(@PathVariable(value = "hash") final String hash) {
		DbConfirmEmail foundConfirmEmail = confirmEmailController.getByHash(hash);

		User user = userController.confirmEmail(foundConfirmEmail.getUserId());
		confirmEmailController.delete(foundConfirmEmail);

		return user;
	}

	@PostMapping("/{userId}/checkPasswordMatch")
	public DbResetPassword checkPasswordMatch(@PathVariable(value = "userId") final String userId,
											  @RequestBody final User user,
											  @CookieValue(SESSION_COOKIE_NAME) final String sessionId) {
		userController.isPasswordMatch(new RequestWrapper().data(user).userId(userId).sessionId(sessionId));

		return resetPasswordController.create(user.getId());
	}

	@PostMapping("/changePassword/{hash}")
	public User changePassword(@PathVariable(value = "hash") final String hash,
							   @RequestBody final User user) {
		DbResetPassword foundResetPassword = resetPasswordController.getByHash(hash);

		User changedUser = userController.changePassword(foundResetPassword.getUserId(), user);

		resetPasswordController.delete(foundResetPassword);

		return changedUser;
	}

	@PostMapping("/{userId}/changeEmail")
	public User changeEmail(@PathVariable(value = "userId") final String userId,
							@RequestBody final User user,
							@CookieValue(SESSION_COOKIE_NAME) final String sessionId) {
		return userController.changeEmail(new RequestWrapper().data(user).userId(userId).sessionId(sessionId));
	}

	@PostMapping("/{userId}/changeNotification")
	public User changeNotification(@PathVariable(value = "userId") final String userId,
								   @RequestBody final User user,
								   @CookieValue(SESSION_COOKIE_NAME) final String sessionId) {
		return userController.changeNotification(new RequestWrapper().data(user).userId(userId).sessionId(sessionId));
	}

	@PostMapping("/forgotPass")
	public void forgotPassword(@RequestBody final User user) {
		User foundUser = userController.findByEmail(user.getEmail().toLowerCase());

		DbResetPassword createdResetPassword = resetPasswordController.create(foundUser.getId());

		mailManager.sendForgotPasswordMessage(foundUser.getEmail(), createdResetPassword.getHash());
	}

	@PostMapping("/{userId}/changeRoles")
	public void changeRoles(@PathVariable("userId") final String userId,
							@RequestBody final List<String> roles,
							@CookieValue(SESSION_COOKIE_NAME) final String sessionId) {
		userController.changeRoles(new RequestWrapper().data(roles).userId(userId).sessionId(sessionId));
	}

	@PostMapping("/{userId}/sendMessage")
	public void sendMessage(@PathVariable("userId") final String userId,
							@RequestBody final Message message) {
		messageController.create(message, userId);
	}

	@GetMapping("/{userId}/getTelegramLink")
	public DbTelegramAuthCode getTelegramLink(@PathVariable("userId") final String userId) {
		return telegramManager.createTelegramLink(userId);
	}
}