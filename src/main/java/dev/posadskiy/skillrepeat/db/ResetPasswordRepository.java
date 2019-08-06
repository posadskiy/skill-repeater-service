package dev.posadskiy.skillrepeat.db;

import dev.posadskiy.skillrepeat.db.model.DbResetPassword;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ResetPasswordRepository extends MongoRepository<DbResetPassword, String> {
	DbResetPassword findByHash(String hash);
}
