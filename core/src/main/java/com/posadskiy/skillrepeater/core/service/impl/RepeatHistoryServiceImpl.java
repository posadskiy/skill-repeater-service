package com.posadskiy.skillrepeater.core.service.impl;

import com.posadskiy.skillrepeater.core.mapper.entity.RepeatHistoryMapper;
import com.posadskiy.skillrepeater.core.model.RepeatHistory;
import com.posadskiy.skillrepeater.core.service.RepeatHistoryService;
import com.posadskiy.skillrepeater.core.storage.db.RepeatHistoryRepository;
import jakarta.inject.Singleton;
import lombok.NoArgsConstructor;

@Singleton
@NoArgsConstructor
public class RepeatHistoryServiceImpl implements RepeatHistoryService {
    private RepeatHistoryRepository repeatHistoryRepository;
    private RepeatHistoryMapper repeatHistoryMapper;

    public RepeatHistoryServiceImpl(RepeatHistoryRepository repeatHistoryRepository, RepeatHistoryMapper repeatHistoryMapper) {
        this.repeatHistoryRepository = repeatHistoryRepository;
        this.repeatHistoryMapper = repeatHistoryMapper;
    }

    @Override
    public void repeatSkill(String id) {
        var repeatHistory = new RepeatHistory();
        repeatHistory.setSkillId(id);

        repeatHistoryRepository.save(
            repeatHistoryMapper.mapToEntity(repeatHistory)
        );
    }
}
