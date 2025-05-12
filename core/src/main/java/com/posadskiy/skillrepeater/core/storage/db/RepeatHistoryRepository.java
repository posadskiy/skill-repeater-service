package com.posadskiy.skillrepeater.core.storage.db;

import com.posadskiy.skillrepeater.core.storage.db.entity.RepeatHistoryEntity;
import io.micronaut.data.jdbc.annotation.JdbcRepository;
import io.micronaut.data.repository.CrudRepository;

import static io.micronaut.data.model.query.builder.sql.Dialect.POSTGRES;

@JdbcRepository(dialect = POSTGRES)
public interface RepeatHistoryRepository extends CrudRepository<RepeatHistoryEntity, String> {
}
