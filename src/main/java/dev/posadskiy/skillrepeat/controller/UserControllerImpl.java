package dev.posadskiy.skillrepeat.controller;

import dev.posadskiy.skillrepeat.annotation.Security;
import dev.posadskiy.skillrepeat.db.UserRepository;
import dev.posadskiy.skillrepeat.db.model.DbSkill;
import dev.posadskiy.skillrepeat.db.model.DbUser;
import dev.posadskiy.skillrepeat.dto.Skill;
import dev.posadskiy.skillrepeat.dto.User;
import dev.posadskiy.skillrepeat.mapper.UserMapper;
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

	@Security
	@Override
	public List<User> getAll(String sessionId) {
		List<DbUser> users = userRepository.findAll();
		return users.parallelStream().map(userMapper::mapToDto).collect(Collectors.toList());
	}

	@Security
	@Override
	public User getUserById(String id, String sessionId) {
		Optional<DbUser> optionalUser = userRepository.findById(id);

		if (!optionalUser.isPresent()) return null;

		DbUser user = optionalUser.get();

		return userMapper.mapToDto(user);
	}

	@Security
	@Override
	public User findByName(String name, String sessionId) {
		DbUser byName = userRepository.findByName(name);

		if (byName == null) return null;

		return userMapper.mapToDto(byName);
	}

	@Security
	@Override
	public User addUser(User user, String sessionId) {
		return userMapper.mapToDto(
			userRepository.save(
				userMapper.mapFromDto(user)));
	}

	@Security
	@Override
	public User updateUser(User user, String sessionId) {
		return userMapper.mapToDto(
			userRepository.save(
				userMapper.mapFromDto(user)));
	}

	@Security
	@Override
	public User addSkill(String userId, List<Skill> skills, String sessionId) {
		Optional<DbUser> optionalDbUser = userRepository.findById(userId);
		if (!optionalDbUser.isPresent()) return null;

		DbUser dbUser = optionalDbUser.get();
		List<DbSkill> dbSkills = skills.stream().map(userMapper::map).collect(Collectors.toList());

		if (CollectionUtils.isEmpty(dbUser.getSkills())) {
			dbUser.setSkills(new ArrayList<>());
		}
		dbUser.getSkills().addAll(dbSkills);

		return userMapper.mapToDto(userRepository.save(dbUser));
	}

	@Security
	@Override
	public void deleteUser(String id, String sessionId) {
		userRepository.deleteById(id);
	}

	@Security
	@Override
	public User repeatSkill(String userId, String skillId, String sessionId) {
		Optional<DbUser> optionalUser = userRepository.findById(userId);

		if (!optionalUser.isPresent()) return null;

		DbUser user = optionalUser.get();
		DbSkill skill = CollectionUtils.find(user.getSkills(), item -> item.getId().equals(skillId));

		skill.setLastRepeat(new Date());
		skill.setLevel(skill.getLevel() + 1);

		DbUser savedDbUser = userRepository.save(user);
		return userMapper.mapToDto(savedDbUser);
	}
}