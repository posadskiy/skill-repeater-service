package dev.posadskiy.skillrepeat.db;

import dev.posadskiy.skillrepeat.db.model.DbUser;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<DbUser, String> {

    public DbUser findByName(String name);
    public DbUser findByEmail(String email);
}
