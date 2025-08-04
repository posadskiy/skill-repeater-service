package com.posadskiy.skillrepeater.core.mapper.dto;

import com.posadskiy.skillrepeater.api.dto.SkillDto;
import com.posadskiy.skillrepeater.core.model.Skill;
import io.micronaut.context.annotation.Mapper;
import io.micronaut.core.annotation.Introspected;
import jakarta.inject.Singleton;

@Singleton
@Mapper
public interface SkillDtoMapper {
    
    Skill mapFromDto(SkillDto user);
    
    SkillDto mapToDto(Skill user);
}
