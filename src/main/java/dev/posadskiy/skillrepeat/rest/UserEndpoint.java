package dev.posadskiy.skillrepeat.rest;

import dev.posadskiy.skillrepeat.db.UserRepository;
import dev.posadskiy.skillrepeat.db.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/user")
@CrossOrigin(origins = "http://192.168.1.2:3000")
public class UserEndpoint {

    @Autowired
    UserRepository repository;

    @RequestMapping("/all")
    public List<User> getAll() {
        return repository.findAll();
    }

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

    @RequestMapping(value = "/", method = RequestMethod.PUT)
    public User updateUser(@RequestBody final User user) {
        return repository.save(user);
    }

    @RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE)
    public void deleteUser(@PathVariable(value = "id") final String id) {
        repository.deleteById(id);
    }
}
