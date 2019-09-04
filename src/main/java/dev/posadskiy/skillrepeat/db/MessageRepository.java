package dev.posadskiy.skillrepeat.db;

import dev.posadskiy.skillrepeat.db.model.DbMessage;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface MessageRepository extends MongoRepository<DbMessage, String> { }