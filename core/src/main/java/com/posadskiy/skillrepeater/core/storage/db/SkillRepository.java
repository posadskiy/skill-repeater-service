package com.posadskiy.skillrepeater.core.storage.db;

import com.posadskiy.skillrepeater.core.storage.db.entity.SkillEntity;
import io.micronaut.data.jdbc.annotation.JdbcRepository;
import io.micronaut.data.repository.CrudRepository;

import java.time.LocalDateTime;
import java.util.List;

import static io.micronaut.data.model.query.builder.sql.Dialect.POSTGRES;

@JdbcRepository(dialect = POSTGRES)
public interface SkillRepository extends CrudRepository<SkillEntity, String> {

    List<SkillEntity> findByNextRepeatedBetween(LocalDateTime start, LocalDateTime end);

    List<SkillEntity> findByUserId(String userId);
}
