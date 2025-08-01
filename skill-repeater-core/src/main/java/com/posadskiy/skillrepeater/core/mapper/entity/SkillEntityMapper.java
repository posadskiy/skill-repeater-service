package com.posadskiy.skillrepeater.core.mapper.entity;

import com.posadskiy.skillrepeater.core.model.Skill;
import com.posadskiy.skillrepeater.core.storage.db.entity.SkillEntity;
import io.micronaut.context.annotation.Mapper;
import jakarta.inject.Singleton;

@Singleton
public interface SkillEntityMapper {

    @Mapper.Mapping(from = "userId", to = "userId")
    @Mapper.Mapping(from = "name", to = "name")
    @Mapper.Mapping(from = "description", to = "description")
    Skill mapFromEntity(SkillEntity user);

    @Mapper.Mapping(from = "name", to = "name")
    @Mapper.Mapping(from = "level", to = "level", defaultValue = "0")
    @Mapper.Mapping(from = "lastRepeated", to = "lastRepeated")
    SkillEntity mapToEntity(Skill user);
}
