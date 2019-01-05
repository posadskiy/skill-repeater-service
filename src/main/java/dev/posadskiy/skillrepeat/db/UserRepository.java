package dev.posadskiy.skillrepeat.db;

import dev.posadskiy.skillrepeat.db.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<User, String> {

    public User findByName(String name);
}
