package dev.posadskiy.skillrepeat.controller;

import dev.posadskiy.skillrepeat.annotation.Security;
import dev.posadskiy.skillrepeat.db.UserRepository;
import dev.posadskiy.skillrepeat.db.model.DbSkill;
import dev.posadskiy.skillrepeat.db.model.DbUser;
import dev.posadskiy.skillrepeat.dto.Auth;
import dev.posadskiy.skillrepeat.dto.Skill;
import dev.posadskiy.skillrepeat.dto.User;
import dev.posadskiy.skillrepeat.exception.UserDoesNotExistException;
import dev.posadskiy.skillrepeat.exception.UserPasswordDoesNotMatchException;
import dev.posadskiy.skillrepeat.mapper.UserMapper;
import dev.posadskiy.skillrepeat.rest.RequestWrapper;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class UserControllerImpl implements UserController {
	@Autowired
	private UserRepository userRepository;

	@Autowired
	private UserMapper userMapper;

	@Security(roles = "ADMIN")
	@Override
	public List<User> getAll(RequestWrapper requestWrapper) {
		List<DbUser> users = userRepository.findAll();
		return users.parallelStream().map(userMapper::mapToDto).collect(Collectors.toList());
	}

	@Security
	@Override
	public User getUserById(RequestWrapper requestWrapper) {
		Optional<DbUser> optionalUser = userRepository.findById(requestWrapper.getUserId());

		if (!optionalUser.isPresent()) throw new UserDoesNotExistException();

		DbUser user = optionalUser.get();

		return userMapper.mapToDto(user);
	}

	@Security
	@Override
	public User findByName(RequestWrapper requestWrapper) {
		String name = (String) requestWrapper.getData();
		DbUser byName = userRepository.findByName(name);

		if (byName == null) throw new UserDoesNotExistException();

		return userMapper.mapToDto(byName);
	}

	@Security(roles = "ADMIN")
	@Override
	public User addUser(RequestWrapper requestWrapper) {
		User user = (User) requestWrapper.getData();
		return userMapper.mapToDto(
			userRepository.save(
				userMapper.mapFromDto(user)));
	}

	@Security(roles = "ADMIN")
	@Override
	public User updateUser(RequestWrapper requestWrapper) {
		User user = (User) requestWrapper.getData();
		return userMapper.mapToDto(
			userRepository.save(
				userMapper.mapFromDto(user)));
	}

	@Security
	@Override
	public User addSkill(RequestWrapper requestWrapper) {
		List<Skill> skills = (List<Skill>) requestWrapper.getData();
		String userId = requestWrapper.getUserId();

		Optional<DbUser> optionalDbUser = userRepository.findById(userId);
		if (!optionalDbUser.isPresent()) throw new UserDoesNotExistException();

		DbUser dbUser = optionalDbUser.get();
		List<DbSkill> dbSkills = skills.stream().map(userMapper::map).collect(Collectors.toList());

		if (CollectionUtils.isEmpty(dbUser.getSkills())) {
			dbUser.setSkills(new ArrayList<>());
		}
		dbUser.getSkills().addAll(dbSkills);

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

		Optional<DbUser> optionalUser = userRepository.findById(userId);

		if (!optionalUser.isPresent()) throw new UserDoesNotExistException();

		DbUser user = optionalUser.get();
		DbSkill skill = CollectionUtils.find(user.getSkills(), item -> item.getId().equals(skillId));

		skill.setLastRepeat(new Date());
		skill.setLevel(skill.getLevel() + 1);

		DbUser savedDbUser = userRepository.save(user);
		return userMapper.mapToDto(savedDbUser);
	}

	@Security(roles = "ADMIN")
	@Override
	public void changeRoles(RequestWrapper requestWrapper) {
		List<String> roles = (List<String>) requestWrapper.getData();
		String userId = requestWrapper.getUserId();

		Optional<DbUser> byId = userRepository.findById(userId);
		if (!byId.isPresent()) throw new UserDoesNotExistException();

		DbUser dbUser = byId.get();

		dbUser.setRoles(roles);
		userRepository.save(dbUser);
	}

	@Security
	@Override
	public void changePassword(RequestWrapper requestWrapper) {
		String userId = requestWrapper.getUserId();
		Auth auth = (Auth) requestWrapper.getData();

		Optional<DbUser> optionalUser = userRepository.findById(userId);

		if (!optionalUser.isPresent()) throw new UserDoesNotExistException();

		DbUser user = optionalUser.get();

		if (!user.getPassword().equals(auth.getOldPassword())) {
			throw new UserPasswordDoesNotMatchException();
		}

		user.setPassword(auth.getPassword());

		userRepository.save(user);
	}
}