package dev.posadskiy.skillrepeat.rest;

import dev.posadskiy.skillrepeat.controller.UserController;
import dev.posadskiy.skillrepeat.db.SessionRepository;
import dev.posadskiy.skillrepeat.db.UserRepository;
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
import dev.posadskiy.skillrepeat.validator.AuthValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
@RequestMapping("/user")
@CrossOrigin(origins = "http://192.168.100.7:3000")
public class UserEndpoint {
	private static final String SESSION_COOKIE_NAME = "SESSION_ID";

	private final UserRepository userRepository;
	private final SessionRepository sessionRepository;
	private final UserMapper userMapper;
	private final AuthMapper authMapper;
	private final UserController controller;
	private final AuthValidator authValidator;

	@Autowired
	public UserEndpoint(UserRepository userRepository, SessionRepository sessionRepository, UserMapper userMapper, AuthMapper authMapper, UserController controller, AuthValidator authValidator) {
		this.userRepository = userRepository;
		this.sessionRepository = sessionRepository;
		this.userMapper = userMapper;
		this.authMapper = authMapper;
		this.controller = controller;
		this.authValidator = authValidator;
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

	@DeleteMapping("/{id}")
	public void deleteUser(@PathVariable(value = "id") final String userId, @CookieValue(SESSION_COOKIE_NAME) final String sessionId) {
		controller.deleteUser(new RequestWrapper().userId(userId).sessionId(sessionId));
	}

	@PostMapping("/{userId}/skill/repeat/{skillId}")
	public User repeatSkill(@PathVariable(value = "userId") final String userId,
							@PathVariable(value = "skillId") final String skillId,
							@CookieValue(SESSION_COOKIE_NAME) final String sessionId) {
		return controller.repeatSkill(new RequestWrapper().data(skillId).userId(userId).sessionId(sessionId));
	}

	@PostMapping("/auth")
	public User auth(@RequestBody final Auth auth, final HttpServletResponse response) {
		authValidator.authValidate(auth);

		DbUser foundUser = userRepository.findByName(auth.getLogin());
		if (foundUser == null) {
			throw new UserDoesNotExistException();
		}

		if (!auth.getPassword().equals(foundUser.getPassword())) {
			throw new UserPasswordDoesNotMatchException();
		}

		User user = userMapper.mapToDto(foundUser);
		DbSession session = sessionRepository.save(new DbSession(user.getId(), System.currentTimeMillis() + 100_000));
		Cookie cookie = new Cookie(SESSION_COOKIE_NAME, session.getId());
		cookie.setPath("/");
		response.addCookie(cookie);
		return user;
	}

	@PostMapping("/reg")
	public User registration(@RequestBody final Auth auth, final HttpServletResponse response) {
		authValidator.authValidate(auth);

		DbUser foundUser = userRepository.findByName(auth.getLogin());
		if (foundUser != null) {
			throw new UserAlreadyExistException();
		}

		User user = userMapper.mapToDto(
			userRepository.save(
				authMapper.mapFromDto(auth)));

		DbSession session = sessionRepository.save(new DbSession(user.getId(), System.currentTimeMillis() + 100_000));
		Cookie cookie = new Cookie(SESSION_COOKIE_NAME, session.getId());
		cookie.setPath("/");
		response.addCookie(cookie);
		return user;
	}

	@PostMapping("/{userId}/changePass")
	public void changePassword(@PathVariable(value = "userId") final String userId,
							   @RequestBody final Auth auth,
							   @CookieValue(SESSION_COOKIE_NAME) final String sessionId) {
		controller.changePassword(new RequestWrapper().data(auth).userId(userId).sessionId(sessionId));
	}

	@PostMapping("/{userId}/changeRoles")
	public void changeRoles(@PathVariable("userId") final String userId, @RequestBody final List<String> roles,
							@CookieValue(SESSION_COOKIE_NAME) final String sessionId) {
		controller.changeRoles(new RequestWrapper().data(roles).userId(userId).sessionId(sessionId));
	}
}