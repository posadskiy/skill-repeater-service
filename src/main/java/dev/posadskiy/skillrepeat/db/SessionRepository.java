package dev.posadskiy.skillrepeat.db;

import dev.posadskiy.skillrepeat.db.model.DbSession;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface SessionRepository extends MongoRepository<DbSession, String> {
}