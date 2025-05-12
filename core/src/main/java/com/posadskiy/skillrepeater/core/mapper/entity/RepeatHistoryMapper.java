package com.posadskiy.skillrepeater.core.mapper.entity;

import com.posadskiy.skillrepeater.core.model.RepeatHistory;
import com.posadskiy.skillrepeater.core.storage.db.entity.RepeatHistoryEntity;
import io.micronaut.context.annotation.Mapper;
import jakarta.inject.Singleton;

@Singleton
public interface RepeatHistoryMapper {
    @Mapper.Mapping(from = "skillId", to = "skillId")
    RepeatHistory mapFromEntity(RepeatHistoryEntity user);

    @Mapper.Mapping(from = "skillId", to = "skillId")
    RepeatHistoryEntity mapToEntity(RepeatHistory user);
}
