package com.posadskiy.skillrepeater.core.mapper.dto;

import com.posadskiy.skillrepeater.api.dto.RepeatHistoryDto;
import com.posadskiy.skillrepeater.core.model.RepeatHistory;
import io.micronaut.context.annotation.Mapper;
import jakarta.inject.Singleton;

@Singleton
public interface RepeatHistoryDtoMapper {
    @Mapper.Mapping(from = "skillId", to = "skillId")
    @Mapper.Mapping(from = "userId", to = "userId")
    @Mapper.Mapping(from = "comment", to = "comment")
    @Mapper.Mapping(from = "repeatedAt", to = "repeatedAt")
    RepeatHistoryDto mapToDto(RepeatHistory history);

    @Mapper.Mapping(from = "skillId", to = "skillId")
    @Mapper.Mapping(from = "userId", to = "userId")
    @Mapper.Mapping(from = "comment", to = "comment")
    @Mapper.Mapping(from = "repeatedAt", to = "repeatedAt")
    RepeatHistory mapFromDto(RepeatHistoryDto history);
} 
