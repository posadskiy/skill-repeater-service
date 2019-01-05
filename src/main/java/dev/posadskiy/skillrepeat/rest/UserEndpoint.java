package dev.posadskiy.skillrepeat.rest;

import dev.posadskiy.skillrepeat.db.UserRepository;
import dev.posadskiy.skillrepeat.db.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/user")
public class UserEndpoint {

    @Autowired
    UserRepository repository;

    @RequestMapping("/id/{id}")
    public Optional<User> getUserById(@PathVariable(value = "id") final String id) {
        return repository.findById(id);
    }

    @RequestMapping("/name/{name}")
    public User getUserByName(@PathVariable(value = "name") final String name) {
        return repository.findByName(name);
    }

    @RequestMapping(value = "/", method = RequestMethod.POST)
    public User addUser(@RequestBody final User user) {
        return repository.save(user);
    }
}
