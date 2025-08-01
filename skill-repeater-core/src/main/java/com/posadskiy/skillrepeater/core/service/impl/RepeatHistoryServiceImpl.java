package com.posadskiy.skillrepeater.core.service.impl;

import com.posadskiy.skillrepeater.core.mapper.entity.RepeatHistoryMapper;
import com.posadskiy.skillrepeater.core.model.RepeatHistory;
import com.posadskiy.skillrepeater.core.service.RepeatHistoryService;
import com.posadskiy.skillrepeater.core.service.SkillService;
import com.posadskiy.skillrepeater.core.storage.db.RepeatHistoryRepository;
import jakarta.inject.Singleton;
import lombok.NoArgsConstructor;

import java.util.List;

@Singleton
@NoArgsConstructor
public class RepeatHistoryServiceImpl implements RepeatHistoryService {
    private RepeatHistoryRepository repeatHistoryRepository;
    private RepeatHistoryMapper repeatHistoryMapper;
    private SkillService skillService;

    public RepeatHistoryServiceImpl(RepeatHistoryRepository repeatHistoryRepository, 
                                  RepeatHistoryMapper repeatHistoryMapper,
                                  SkillService skillService) {
        this.repeatHistoryRepository = repeatHistoryRepository;
        this.repeatHistoryMapper = repeatHistoryMapper;
        this.skillService = skillService;
    }

    @Override
    public void repeatSkill(String id) {
        var skill = skillService.get(id);
        var repeatHistory = new RepeatHistory();
        repeatHistory.setSkillId(id);
        repeatHistory.setUserId(skill.getUserId());

        repeatHistoryRepository.save(
            repeatHistoryMapper.mapToEntity(repeatHistory)
        );
    }

    @Override
    public List<RepeatHistory> getBySkillId(String skillId) {
        return repeatHistoryRepository.findBySkillId(skillId)
            .stream()
            .map(repeatHistoryMapper::mapFromEntity)
            .toList();
    }

    @Override
    public List<RepeatHistory> getByUserId(String userId) {
        return repeatHistoryRepository.findByUserId(userId)
            .stream()
            .map(repeatHistoryMapper::mapFromEntity)
            .toList();
    }
}
