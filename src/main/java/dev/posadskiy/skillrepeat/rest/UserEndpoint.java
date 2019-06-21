package dev.posadskiy.skillrepeat.rest;

import dev.posadskiy.skillrepeat.controller.UserController;
import dev.posadskiy.skillrepeat.db.SessionRepository;
import dev.posadskiy.skillrepeat.db.UserRepository;
import dev.posadskiy.skillrepeat.db.model.DbSession;
import dev.posadskiy.skillrepeat.db.model.DbUser;
import dev.posadskiy.skillrepeat.dto.Auth;
import dev.posadskiy.skillrepeat.dto.Skill;
import dev.posadskiy.skillrepeat.dto.User;
import dev.posadskiy.skillrepeat.mapper.AuthMapper;
import dev.posadskiy.skillrepeat.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.security.auth.message.AuthException;
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

	@Autowired
	public UserEndpoint(UserRepository userRepository, SessionRepository sessionRepository, UserMapper userMapper, AuthMapper authMapper, UserController controller) {
		this.userRepository = userRepository;
		this.sessionRepository = sessionRepository;
		this.userMapper = userMapper;
		this.authMapper = authMapper;
		this.controller = controller;
	}

	@RequestMapping("/all")
	public List<User> getAll(@CookieValue(SESSION_COOKIE_NAME) final String sessionId) {
		return controller.getAll(sessionId);
	}

	@GetMapping("/id/{id}")
	public User getUserById(@PathVariable("id") final String id, @CookieValue(SESSION_COOKIE_NAME) final String sessionId) {
		return controller.getUserById(id, sessionId);
	}

	@GetMapping("/name/{name}")
	public User getUserByName(@PathVariable("name") final String name, @CookieValue(SESSION_COOKIE_NAME) final String sessionId) {
		return controller.findByName(name, sessionId);
	}

	@PutMapping("/")
	public User addUser(@RequestBody final User user, @CookieValue(SESSION_COOKIE_NAME) final String sessionId) {
		return controller.addUser(user, sessionId);
	}

	@PostMapping("/")
	public User updateUser(@RequestBody final User user, @CookieValue(SESSION_COOKIE_NAME) final String sessionId) {
		return controller.updateUser(user, sessionId);
	}

	@PostMapping("/{userId}/skill/add")
	public User addSkill(@PathVariable("userId") final String userId, @RequestBody final List<Skill> skills,
						 @CookieValue(SESSION_COOKIE_NAME) final String sessionId) {
		return controller.addSkill(userId, skills, sessionId);
	}

	@DeleteMapping("/delete/{id}")
	public void deleteUser(@PathVariable(value = "id") final String id, @CookieValue(SESSION_COOKIE_NAME) final String sessionId) {
		controller.deleteUser(id, sessionId);
	}

	@PostMapping("/{userId}/skill/repeat/{skillId}")
	public User repeatSkill(@PathVariable(value = "userId") final String userId,
							@PathVariable(value = "skillId") final String skillId,
							@CookieValue(SESSION_COOKIE_NAME) final String sessionId) {
		return controller.repeatSkill(userId, skillId, sessionId);
	}

	@PostMapping("/auth")
	public User auth(@RequestBody final Auth auth, final HttpServletResponse response) throws AuthException {
		DbUser foundUser = userRepository.findByName(auth.getLogin());
		if (foundUser == null) {
			throw new AuthException("User not found");
		}

		if (auth.getPassword().equals(foundUser.getPassword())) {
			User user = userMapper.mapToDto(foundUser);
			DbSession session = sessionRepository.save(new DbSession(System.currentTimeMillis() + 100_000));
			Cookie cookie = new Cookie(SESSION_COOKIE_NAME, session.getId());
			cookie.setPath("/");
			response.addCookie(cookie);
			return user;
		}

		throw new AuthException("Auth is not correct");
	}

	@PostMapping("/reg")
	public User registration(@RequestBody final Auth auth, final HttpServletResponse response) {
		User user = userMapper.mapToDto(
			userRepository.save(
				authMapper.mapFromDto(auth)));
		DbSession session = sessionRepository.save(new DbSession(System.currentTimeMillis() + 100_000));
		Cookie cookie = new Cookie(SESSION_COOKIE_NAME, session.getId());
		cookie.setPath("/");
		response.addCookie(cookie);
		return user;
	}
}