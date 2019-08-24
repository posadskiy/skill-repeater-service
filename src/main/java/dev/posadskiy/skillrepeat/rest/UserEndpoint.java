package dev.posadskiy.skillrepeat.rest;

import dev.posadskiy.skillrepeat.controller.UserController;
import dev.posadskiy.skillrepeat.db.ResetPasswordRepository;
import dev.posadskiy.skillrepeat.db.SessionRepository;
import dev.posadskiy.skillrepeat.db.UserRepository;
import dev.posadskiy.skillrepeat.db.model.DbResetPassword;
import dev.posadskiy.skillrepeat.db.model.DbSession;
import dev.posadskiy.skillrepeat.db.model.DbUser;
import dev.posadskiy.skillrepeat.dto.Auth;
import dev.posadskiy.skillrepeat.dto.Skill;
import dev.posadskiy.skillrepeat.dto.User;
import dev.posadskiy.skillrepeat.exception.UserAlreadyExistException;
import dev.posadskiy.skillrepeat.exception.UserDoesNotExistException;
import dev.posadskiy.skillrepeat.exception.UserPasswordDoesNotMatchException;
import dev.posadskiy.skillrepeat.mapper.AuthMapper;
import dev.posadskiy.skillrepeat.mapper.UserMapper;
import dev.posadskiy.skillrepeat.service.CryptoService;
import dev.posadskiy.skillrepeat.service.MailService;
import dev.posadskiy.skillrepeat.validator.AuthValidator;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static dev.posadskiy.skillrepeat.controller.SessionControllerImpl.SESSION_LIFE_TIME_MS;
import static dev.posadskiy.skillrepeat.controller.SessionControllerImpl.SESSION_LIFE_TIME_S;

@RestController
@RequestMapping("/user")
public class UserEndpoint {
	private static final String SESSION_COOKIE_NAME = "SESSION_ID";

	private final UserRepository userRepository;
	private final SessionRepository sessionRepository;
	private final ResetPasswordRepository resetPasswordRepository;
	private final UserMapper userMapper;
	private final AuthMapper authMapper;
	private final UserController controller;
	private final AuthValidator authValidator;
	private final MailService mailService;

	@Autowired
	public UserEndpoint(UserRepository userRepository, SessionRepository sessionRepository, ResetPasswordRepository resetPasswordRepository, UserMapper userMapper, AuthMapper authMapper, UserController controller, AuthValidator authValidator, MailService mailService) {
		this.userRepository = userRepository;
		this.sessionRepository = sessionRepository;
		this.resetPasswordRepository = resetPasswordRepository;
		this.userMapper = userMapper;
		this.authMapper = authMapper;
		this.controller = controller;
		this.authValidator = authValidator;
		this.mailService = mailService;
	}

	@RequestMapping("/all")
	public List<User> getAll(@CookieValue(SESSION_COOKIE_NAME) final String sessionId) {
		return controller.getAll(new RequestWrapper().sessionId(sessionId));
	}

	@GetMapping("/id/{id}")
	public User getUserById(@PathVariable("id") final String id, @CookieValue(SESSION_COOKIE_NAME) final String sessionId) {
		return controller.getUserById(new RequestWrapper().userId(id).sessionId(sessionId));
	}

	@GetMapping("/name/{name}")
	public User getUserByName(@PathVariable("name") final String name, @CookieValue(SESSION_COOKIE_NAME) final String sessionId) {
		return controller.findByName(new RequestWrapper().data(name).sessionId(sessionId));
	}

	@PostMapping("/")
	public User updateUser(@RequestBody final User user, @CookieValue(SESSION_COOKIE_NAME) final String sessionId) {
		return controller.updateUser(new RequestWrapper().data(user).userId(user.getId()).sessionId(sessionId));
	}

	@PostMapping("/{userId}/skill/add")
	public User addSkill(@PathVariable("userId") final String userId, @RequestBody final List<Skill> skills,
						 @CookieValue(SESSION_COOKIE_NAME) final String sessionId) {
		return controller.addSkill(new RequestWrapper().data(skills).userId(userId).sessionId(sessionId));
	}

	@DeleteMapping("/{userId}/skill/{skillId}")
	public User deleteSkill(@PathVariable("userId") final String userId, @PathVariable final String skillId,
							@CookieValue(SESSION_COOKIE_NAME) final String sessionId) {
		return controller.deleteSkill(new RequestWrapper().data(skillId).userId(userId).sessionId(sessionId));
	}

	@DeleteMapping("/{id}")
	public void deleteUser(@PathVariable(value = "id") final String userId, @CookieValue(SESSION_COOKIE_NAME) final String sessionId) {
		controller.deleteUser(new RequestWrapper().userId(userId).sessionId(sessionId));
	}

	@GetMapping("/{userId}/skill/repeat/{skillId}")
	public User repeatSkill(@PathVariable(value = "userId") final String userId,
							@PathVariable(value = "skillId") final String skillId,
							@CookieValue(SESSION_COOKIE_NAME) final String sessionId) {
		return controller.repeatSkill(new RequestWrapper().data(skillId).userId(userId).sessionId(sessionId));
	}

	@PostMapping("/auth")
	public User auth(@RequestBody final Auth auth, final HttpServletRequest request, final HttpServletResponse response) {
		authValidator.authValidate(auth);

		DbUser foundUser = userRepository.findByEmail(auth.getEmail());
		if (foundUser == null) {
			throw new UserDoesNotExistException();
		}

		if (!auth.getPassword().equals(foundUser.getPassword())) {
			throw new UserPasswordDoesNotMatchException();
		}

		User user = userMapper.mapToDto(foundUser);
		DbSession session = sessionRepository.save(new DbSession(request.getSession().getId(), user.getId(), System.currentTimeMillis() + SESSION_LIFE_TIME_MS));
		Cookie cookie = new Cookie(SESSION_COOKIE_NAME, session.getId());
		cookie.setPath("/");
		cookie.setDomain("localhost");
		cookie.setMaxAge(SESSION_LIFE_TIME_S);
		response.addCookie(cookie);
		return user;
	}

	@PostMapping("/reg")
	public User registration(@RequestBody final Auth auth, final HttpServletRequest request, final HttpServletResponse response) {
		authValidator.regValidate(auth);

		DbUser foundUser = userRepository.findByEmail(auth.getEmail());
		if (foundUser != null) {
			throw new UserAlreadyExistException();
		}

		DbUser dbUser = authMapper.mapFromDto(auth);
		dbUser.setRoles(Collections.singletonList("USER"));
		User user = userMapper.mapToDto(
			userRepository.save(
				dbUser));

		DbSession session = sessionRepository.save(new DbSession(request.getSession().getId(), user.getId(), System.currentTimeMillis() + SESSION_LIFE_TIME_MS));
		Cookie cookie = new Cookie(SESSION_COOKIE_NAME, session.getId());
		cookie.setPath("/");
		cookie.setDomain("localhost");
		cookie.setMaxAge(SESSION_LIFE_TIME_S);
		response.addCookie(cookie);
		return user;
	}

	@PostMapping("/regWithSkills")
	public User registrationWithSkills(@RequestBody final User user, final HttpServletRequest request, final HttpServletResponse response) {
		//authValidator.regValidate(auth);

		DbUser foundUser = userRepository.findByEmail(user.getEmail());
		if (foundUser != null) {
			throw new UserAlreadyExistException();
		}

		DbUser dbUser = userMapper.mapFromDto(user);
		dbUser.setRoles(Collections.singletonList("USER"));
		User createdUser = userMapper.mapToDto(
			userRepository.save(
				dbUser));

		DbSession session = sessionRepository.save(new DbSession(request.getSession().getId(), createdUser.getId(), System.currentTimeMillis() + SESSION_LIFE_TIME_MS));
		Cookie cookie = new Cookie(SESSION_COOKIE_NAME, session.getId());
		cookie.setPath("/");
		cookie.setDomain("localhost");
		cookie.setMaxAge(SESSION_LIFE_TIME_S);
		response.addCookie(cookie);
		return createdUser;
	}

	@PostMapping("/{userId}/changePassword")
	public void changePassword(@PathVariable(value = "userId") final String userId,
							   @RequestBody final Auth auth,
							   @CookieValue(SESSION_COOKIE_NAME) final String sessionId) {
		controller.changePassword(new RequestWrapper().data(auth).userId(userId).sessionId(sessionId));
	}

	@PostMapping("/{userId}/changeEmail")
	public User changeEmail(@PathVariable(value = "userId") final String userId,
							   @RequestBody final Auth auth,
							   @CookieValue(SESSION_COOKIE_NAME) final String sessionId) {
		return controller.changeEmail(new RequestWrapper().data(auth).userId(userId).sessionId(sessionId));
	}

	@PostMapping("/{userId}/changeNotification")
	public User changeNotification(@PathVariable(value = "userId") final String userId,
							   @RequestBody final User user,
							   @CookieValue(SESSION_COOKIE_NAME) final String sessionId) {
		return controller.changeNotification(new RequestWrapper().data(user).userId(userId).sessionId(sessionId));
	}

	@PostMapping("/forgotPass")
	public void forgotPassword(@RequestBody final Auth auth) {
		DbUser foundUser = userRepository.findByEmail(auth.getEmail());
		if (foundUser == null) {
			throw new UserDoesNotExistException();
		}

		String hash = RandomStringUtils.randomAscii(10);
		DbResetPassword dbResetPassword = new DbResetPassword();
		dbResetPassword.setUserId(foundUser.getId());
		dbResetPassword.setHash(hash);
		dbResetPassword.setTime(System.currentTimeMillis());
		resetPasswordRepository.save(dbResetPassword);

		mailService.sendResetPasswordMessage(foundUser.getEmail(), hash);
	}

	@GetMapping("/resetPass/{hash}")
	public void resetPassword(@PathVariable(value = "hash") final String hash) {
		DbResetPassword foundResetPassword = resetPasswordRepository.findByHash(hash);
		if (foundResetPassword == null) {
			throw new UserDoesNotExistException();
		}

		Optional<DbUser> foundUserById = userRepository.findById(foundResetPassword.getUserId());
		if (!foundUserById.isPresent()) {
			throw new UserDoesNotExistException();
		}

		DbUser foundUser = foundUserById.get();
		String newNakedPassword = RandomStringUtils.randomAscii(10);
		String newPassword = CryptoService.createNewPassword(newNakedPassword);
		foundUser.setPassword(newPassword);

		userRepository.save(foundUser);
		resetPasswordRepository.delete(foundResetPassword);

		mailService.sendMessage(foundUser.getEmail(), "Your new password for Skill Repeater", "Hello!\n" +
			"Your new password is " + newNakedPassword);
	}

	@PostMapping("/{userId}/changeRoles")
	public void changeRoles(@PathVariable("userId") final String userId, @RequestBody final List<String> roles,
							@CookieValue(SESSION_COOKIE_NAME) final String sessionId) {
		controller.changeRoles(new RequestWrapper().data(roles).userId(userId).sessionId(sessionId));
	}
}