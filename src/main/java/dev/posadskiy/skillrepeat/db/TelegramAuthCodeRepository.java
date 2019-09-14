package dev.posadskiy.skillrepeat.db;

import dev.posadskiy.skillrepeat.db.model.DbTelegramAuthCode;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface TelegramAuthCodeRepository extends MongoRepository<DbTelegramAuthCode, String> {
	DbTelegramAuthCode findByHash(String hash);
}
