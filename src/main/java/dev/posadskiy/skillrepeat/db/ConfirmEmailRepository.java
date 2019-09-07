package dev.posadskiy.skillrepeat.db;

import dev.posadskiy.skillrepeat.db.model.DbConfirmEmail;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ConfirmEmailRepository extends MongoRepository<DbConfirmEmail, String> {
	DbConfirmEmail findByHash(String hash);
}
