package dev.posadskiy.skillrepeat.db;

import dev.posadskiy.skillrepeat.db.model.DbUser;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<DbUser, String> {

    DbUser findByEmail(String email);

}
