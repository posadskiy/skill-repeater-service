package com.posadskiy.skillrepeater.core.mapper.entity;

import com.posadskiy.skillrepeater.core.model.Skill;
import com.posadskiy.skillrepeater.core.storage.db.entity.SkillEntity;
import io.micronaut.context.annotation.Mapper;
import jakarta.inject.Singleton;

@Singleton
@Mapper
public interface SkillEntityMapper {

    Skill mapFromEntity(SkillEntity user);
    
    SkillEntity mapToEntity(Skill user);
}
