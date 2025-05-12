package com.posadskiy.skillrepeater.core.mapper.dto;

import com.posadskiy.skillrepeater.api.dto.SkillDto;
import com.posadskiy.skillrepeater.core.model.Skill;
import io.micronaut.context.annotation.Mapper;
import jakarta.inject.Singleton;

@Singleton
public interface SkillDtoMapper {

    @Mapper.Mapping(from = "name", to = "name")
    Skill mapFromDto(SkillDto user);

    @Mapper.Mapping(from = "name", to = "name")
    SkillDto mapToDto(Skill user);
}
