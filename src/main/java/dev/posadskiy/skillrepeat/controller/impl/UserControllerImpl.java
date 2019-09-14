package dev.posadskiy.skillrepeat.controller.impl;

import dev.posadskiy.skillrepeat.annotation.Security;
import dev.posadskiy.skillrepeat.controller.UserController;
import dev.posadskiy.skillrepeat.db.UserRepository;
import dev.posadskiy.skillrepeat.db.model.DbSkill;
import dev.posadskiy.skillrepeat.db.model.DbUser;
import dev.posadskiy.skillrepeat.dto.Skill;
import dev.posadskiy.skillrepeat.dto.User;
import dev.posadskiy.skillrepeat.exception.TooMuchSkillsForUserException;
import dev.posadskiy.skillrepeat.exception.UserAlreadyExistException;
import dev.posadskiy.skillrepeat.exception.UserDoesNotExistException;
import dev.posadskiy.skillrepeat.exception.UserPasswordDoesNotMatchException;
import dev.posadskiy.skillrepeat.mapper.SkillMapper;
import dev.posadskiy.skillrepeat.mapper.UserMapper;
import dev.posadskiy.skillrepeat.rest.RequestWrapper;
import dev.posadskiy.skillrepeat.validator.UserValidator;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;
import java.util.stream.Collectors;

import static dev.posadskiy.skillrepeat.SystemSetting.MAX_SKILLS_NUMBER;

public class UserControllerImpl implements UserController {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private UserMapper userMapper;

	@Autowired
	private SkillMapper skillMapper;

	@Autowired
	private UserValidator userValidator;

	@Security
	@Override
	public User getUserById(RequestWrapper requestWrapper) {
		String userId = requestWrapper.getUserId();
		return this.getMappedUserById(userId);
	}

	@Security
	@Override
	public User updateUser(RequestWrapper requestWrapper) {
		User user = (User) requestWrapper.getData();

		userValidator.userValidate(user);

		DbUser foundUser = this.getUserById(user.getId());

		if (StringUtils.isNotBlank(user.getName()) && !user.getName().equals(foundUser.getName())) {
			foundUser.setName(user.getName());
		}
		if (user.getIsAgreeGetEmails() != null && user.getIsAgreeGetEmails() != foundUser.getIsAgreeGetEmails()) {
			foundUser.setIsAgreeGetEmails(user.getIsAgreeGetEmails());
		}

		return userMapper.mapToDto(
			userRepository.save(
				foundUser
			));
	}

	@Security
	@Override
	public User addSkill(RequestWrapper requestWrapper) {
		List<Skill> skills = (List<Skill>) requestWrapper.getData();
		String userId = requestWrapper.getUserId();

		DbUser dbUser = this.getUserById(userId);

		int dbUserSkillSize = CollectionUtils.isEmpty(dbUser.getSkills()) ? 0 : dbUser.getSkills().size();
		if (dbUserSkillSize + skills.size() > MAX_SKILLS_NUMBER) {
			throw new TooMuchSkillsForUserException();
		}

		List<DbSkill> dbSkills = skills.stream().map(skillMapper::map).collect(Collectors.toList());

		if (CollectionUtils.isEmpty(dbUser.getSkills())) {
			dbUser.setSkills(new ArrayList<>());
		}
		dbUser.getSkills().addAll(dbSkills);

		return userMapper.mapToDto(
			userRepository.save(
				dbUser
			));
	}

	@Security
	@Override
	public User editSkill(RequestWrapper requestWrapper) {
		Skill skill = (Skill) requestWrapper.getData();
		String userId = requestWrapper.getUserId();

		DbUser dbUser = this.getUserById(userId);

		DbSkill skillForChange = CollectionUtils.find(dbUser.getSkills(), (dbSkill) -> dbSkill.getId().equals(skill.getId()));
		if (skill.getName() != null && !skill.getName().equals(skillForChange.getName())) {
			skillForChange.setName(skill.getName());
		}

		if (skill.getPeriod() != null && !skill.getPeriod().equals(skillForChange.getPeriod())) {
			skillForChange.setPeriod(skill.getPeriod());
		}

		if (skill.getTime() != null && !skill.getTime().equals(skillForChange.getTime())) {
			skillForChange.setTime(skill.getTime());
		}

		return userMapper.mapToDto(
			userRepository.save(
				dbUser
			));
	}

	@Security
	@Override
	public User deleteSkill(RequestWrapper requestWrapper) {
		String skillId = (String) requestWrapper.getData();
		String userId = requestWrapper.getUserId();

		DbUser dbUser = this.getUserById(userId);
		List<DbSkill> notDeletedSkills = dbUser.getSkills().stream()
			.filter(skill -> !skill.getId().equals(skillId))
			.collect(Collectors.toList());

		dbUser.setSkills(notDeletedSkills);

		return userMapper.mapToDto(userRepository.save(dbUser));
	}

	@Security(roles = "ADMIN")
	@Override
	public void deleteUser(RequestWrapper requestWrapper) {
		String userId = requestWrapper.getUserId();
		userRepository.deleteById(userId);
	}

	@Security
	@Override
	public User repeatSkill(RequestWrapper requestWrapper) {
		String skillId = (String) requestWrapper.getData();
		String userId = requestWrapper.getUserId();

		DbUser user = this.getUserById(userId);
		DbSkill skill = CollectionUtils.find(user.getSkills(), item -> item.getId().equals(skillId));

		skill.setLastRepeat(new Date());
		skill.setLevel(skill.getLevel() + 1);

		return userMapper.mapToDto(
			userRepository.save(
				user
			));
	}

	@Security(roles = "ADMIN")
	@Override
	public void changeRoles(RequestWrapper requestWrapper) {
		List<String> roles = (List<String>) requestWrapper.getData();
		String userId = requestWrapper.getUserId();

		DbUser dbUser = this.getUserById(userId);

		dbUser.setRoles(roles);

		userRepository.save(dbUser);
	}

	@Security
	@Override
	public void isPasswordMatch(RequestWrapper requestWrapper) {
		String userId = requestWrapper.getUserId();
		User user = (User) requestWrapper.getData();

		DbUser dbUser = this.getUserById(userId);

		if (!dbUser.getPassword().equals(user.getPassword())) {
			throw new UserPasswordDoesNotMatchException();
		}
	}

	@Override
	public User changePassword(String userId, User user) {
		DbUser foundUser = this.getUserById(userId);

		if (StringUtils.isNotBlank(user.getPassword()) && !user.getPassword().equals(foundUser.getPassword())) {
			foundUser.setPassword(user.getPassword());
		}

		return userMapper.mapToDto(
			userRepository.save(
				foundUser
			));
	}

	@Override
	public User appendChatIdToUser(String userId, Long chatId) {
		DbUser dbUser = this.getUserById(userId);
		if (chatId != 0 && !chatId.equals(dbUser.getTelegramChatId())) {
			dbUser.setTelegramChatId(chatId);
			dbUser.setIsAgreeGetTelegram(true);
		}

		return userMapper.mapToDto(
			userRepository.save(
				dbUser
			)
		);
	}

	@Override
	public User changeEmail(RequestWrapper requestWrapper) {
		String userId = requestWrapper.getUserId();
		User user = (User) requestWrapper.getData();

		DbUser dbUser = this.getUserById(userId);

		if (StringUtils.isNotBlank(user.getEmail()) && !user.getEmail().equals(dbUser.getEmail())) {
			dbUser.setEmail(user.getEmail());
		}

		return userMapper.mapToDto(
			userRepository.save(
				dbUser
			));
	}

	@Override
	public User changeNotification(RequestWrapper requestWrapper) {
		String userId = requestWrapper.getUserId();
		User user = (User) requestWrapper.getData();

		DbUser dbUser = this.getUserById(userId);

		if (user.getPeriod() != null && user.getPeriod().compareTo(dbUser.getPeriod()) != 0) {
			dbUser.setPeriod(user.getPeriod());
		}

		if (StringUtils.isNotBlank(user.getTime()) && !user.getTime().equals(dbUser.getTime())) {
			dbUser.setTime(user.getTime());
		}

		return userMapper.mapToDto(
			userRepository.save(
				dbUser
			));
	}

	@Override
	public User auth(final User user) {
		DbUser foundUser = this.getUserByEmail(user.getEmail().toLowerCase());

		if (!foundUser.getPassword().equals(user.getPassword())) {
			throw new UserPasswordDoesNotMatchException();
		}

		return userMapper.mapToDto(foundUser);
	}

	@Override
	public User create(final User user) {
		DbUser foundUser = userRepository.findByEmail(user.getEmail().toLowerCase());
		if (foundUser != null) {
			throw new UserAlreadyExistException();
		}

		DbUser dbUser = userMapper.mapFromDto(user);
		dbUser.setRoles(Collections.singletonList("USER"));
		dbUser.setRegistrationDate(new Date());

		return userMapper.mapToDto(
			userRepository.save(
				dbUser
			));
	}

	@Override
	public User confirmEmail(String userId) {
		DbUser foundUser = this.getUserById(userId);

		foundUser.setIsConfirmedEmail(true);

		return userMapper.mapToDto(
			userRepository.save(
				foundUser
			));
	}

	@Override
	public DbUser getUserById(String userId) {
		Optional<DbUser> optionalFoundUser = userRepository.findById(userId);

		if (!optionalFoundUser.isPresent()) throw new UserDoesNotExistException();

		return optionalFoundUser.get();
	}

	@Override
	public User findByEmail(String email) {
		return this.getMappedUserByEmail(email);
	}

	private User getMappedUserById(String userId) {
		return userMapper.mapToDto(getUserById(userId));
	}

	private DbUser getUserByEmail(String email) {
		DbUser optionalFoundUser = userRepository.findByEmail(email);

		if (optionalFoundUser == null) {
			throw new UserDoesNotExistException();
		}

		return optionalFoundUser;
	}

	private User getMappedUserByEmail(String email) {
		return userMapper.mapToDto(this.getUserByEmail(email));
	}

}