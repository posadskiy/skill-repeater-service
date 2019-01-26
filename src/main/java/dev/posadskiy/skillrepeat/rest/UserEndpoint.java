package dev.posadskiy.skillrepeat.rest;

import dev.posadskiy.skillrepeat.db.UserRepository;
import dev.posadskiy.skillrepeat.db.model.Skill;
import dev.posadskiy.skillrepeat.db.model.User;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.security.auth.message.AuthException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/user")
@CrossOrigin(origins = "http://192.168.1.3:3000")
public class UserEndpoint {

    @Autowired
    UserRepository repository;

    @RequestMapping("/all")
    public List<User> getAll() {
        return repository.findAll();
    }

    @RequestMapping("/id/{id}")
    public User getUserById(@PathVariable(value = "id") final String id) {
        Optional<User> optionalUser = repository.findById(id);

        User user = null;
        if (optionalUser.isPresent()) {
            user = optionalUser.get();
        }

        if (user != null && CollectionUtils.isNotEmpty(user.getSkills())) {
            for (Skill skill : user.getSkills()) {
                if (skill.getLastRepeat() != null) {
                    Calendar calendar = Calendar.getInstance();
                    calendar.add(Calendar.DATE, -14);
                    skill.setIsNeedRepeat(skill.getLastRepeat().before(calendar.getTime()));
                }
            }
        }

        return user;
    }

    @RequestMapping("/name/{name}")
    public User getUserByName(@PathVariable(value = "name") final String name) {
        return repository.findByName(name);
    }

    @RequestMapping(value = "/", method = RequestMethod.PUT)
    public User addUser(@RequestBody final User user) {
        return repository.save(user);
    }

    @RequestMapping(value = "/", method = RequestMethod.POST)
    public User updateUser(@RequestBody final User user) {
        for (Skill skill : user.getSkills()) {
            if (skill.getLevel() == null) {
                skill.setLevel(1);
            }
            if (skill.getTermRepeat() != null) {
                Calendar calendar = Calendar.getInstance();
                switch (skill.getTermRepeat()) {
                    case "0": break;
                    case "1": calendar.add(Calendar.DATE, -1); break;
                    case "2": calendar.add(Calendar.DATE, -7); break;
                    case "3": calendar.add(Calendar.DATE, -14); break;
                    case "4": calendar.add(Calendar.DATE, -30); break;
                    default: break;
                }

                skill.setLastRepeat(calendar.getTime());
            } else {
                Calendar calendar = Calendar.getInstance();
                calendar.add(Calendar.YEAR, -1);
                skill.setLastRepeat(calendar.getTime());
            }
        }
        return repository.save(user);
    }

    @RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE)
    public void deleteUser(@PathVariable(value = "id") final String id) {
        repository.deleteById(id);
    }

    @RequestMapping(value = "/{userId}/skill/repeat/{skillId}", method = RequestMethod.POST)
    public User repeatSkill(@PathVariable(value = "userId") final String userId,
                            @PathVariable(value = "skillId") final Integer skillId) {
        Optional<User> optionalUser = repository.findById(userId);

        if (!optionalUser.isPresent()) return null;

        User user = optionalUser.get();
        Skill skill = user.getSkills().get(skillId);

        skill.setLastRepeat(new Date());
        skill.setLevel(skill.getLevel() + 1);

        repository.save(user);
        return user;
    }

    @RequestMapping(value = "/auth", method = RequestMethod.POST)
    public User auth(@RequestBody final User user) throws AuthException {
        User foundUser = repository.findByName(user.getName());
        if (foundUser == null) {
            throw new AuthException("User not found");
        }

        if (user.getPassword().equals(foundUser.getPassword())) {
            return foundUser;
        }

        throw new AuthException("Auth is not correct");
    }
}
