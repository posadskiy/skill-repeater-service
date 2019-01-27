package dev.posadskiy.skillrepeat.rest;

import dev.posadskiy.skillrepeat.db.UserRepository;
import dev.posadskiy.skillrepeat.db.model.DbSkill;
import dev.posadskiy.skillrepeat.db.model.DbUser;
import dev.posadskiy.skillrepeat.dto.Auth;
import dev.posadskiy.skillrepeat.dto.Skill;
import dev.posadskiy.skillrepeat.dto.User;
import dev.posadskiy.skillrepeat.mapper.AuthMapper;
import dev.posadskiy.skillrepeat.mapper.UserMapper;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.security.auth.message.AuthException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/user")
@CrossOrigin(origins = "http://192.168.1.3:3000")
public class UserEndpoint {

    @Autowired
    private UserRepository repository;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private AuthMapper authMapper;

    @RequestMapping("/all")
    public List<DbUser> getAll() {
        return repository.findAll();
    }

    @RequestMapping("/id/{id}")
    public User getUserById(@PathVariable(value = "id") final String id) {
        Optional<DbUser> optionalUser = repository.findById(id);

        if (!optionalUser.isPresent()) return null;

        DbUser user = optionalUser.get();

        return userMapper.mapToDto(user);
    }

    @RequestMapping("/name/{name}")
    public DbUser getUserByName(@PathVariable(value = "name") final String name) {
        return repository.findByName(name);
    }

    @RequestMapping(value = "/", method = RequestMethod.PUT)
    public User addUser(@RequestBody final User user) {
        return userMapper.mapToDto(
            repository.save(
                userMapper.mapFromDto(user)));
    }

    @RequestMapping(value = "/", method = RequestMethod.POST)
    public User updateUser(@RequestBody final User user) {
        return userMapper.mapToDto(
            repository.save(
                userMapper.mapFromDto(user)));
    }

    @RequestMapping(value = "/{userId}/skill/add", method = RequestMethod.POST)
    public User addSkill(@PathVariable(value = "userId") final String userId,
                         @RequestBody final List<Skill> skills) {
        Optional<DbUser> optionalDbUser = repository.findById(userId);
        if (!optionalDbUser.isPresent()) return null;

        DbUser dbUser = optionalDbUser.get();
        List<DbSkill> dbSkills = skills.stream()
            .map(skill -> userMapper.map(skill)).collect(Collectors.toList());

        if (CollectionUtils.isEmpty(dbUser.getSkills())) {
            dbUser.setSkills(new ArrayList<>());
        }
        dbUser.getSkills().addAll(dbSkills);

        return userMapper.mapToDto(repository.save(dbUser));
    }

    @RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE)
    public void deleteUser(@PathVariable(value = "id") final String id) {
        repository.deleteById(id);
    }

    @RequestMapping(value = "/{userId}/skill/repeat/{skillId}", method = RequestMethod.POST)
    public User repeatSkill(@PathVariable(value = "userId") final String userId,
                            @PathVariable(value = "skillId") final Integer skillId) {
        Optional<DbUser> optionalUser = repository.findById(userId);

        if (!optionalUser.isPresent()) return null;

        DbUser user = optionalUser.get();
        DbSkill skill = user.getSkills().get(skillId);

        skill.setLastRepeat(new Date());
        skill.setLevel(skill.getLevel() + 1);

        DbUser savedDbUser = repository.save(user);
        return userMapper.mapToDto(savedDbUser);
    }

    @RequestMapping(value = "/auth", method = RequestMethod.POST)
    public User auth(@RequestBody final Auth auth) throws AuthException {
        DbUser foundUser = repository.findByName(auth.getLogin());
        if (foundUser == null) {
            throw new AuthException("User not found");
        }

        if (auth.getPassword().equals(foundUser.getPassword())) {
            return userMapper.mapToDto(foundUser);
        }

        throw new AuthException("Auth is not correct");
    }

    @RequestMapping(value = "/reg", method = RequestMethod.POST)
    public User registration(@RequestBody final Auth auth) {
        return userMapper.mapToDto(
            repository.save(
                authMapper.mapFromDto(auth)));
    }
}
