package com.posadskiy.skillrepeater.core.service;

import com.posadskiy.skillrepeater.core.model.RepeatHistory;

import java.util.List;

public interface RepeatHistoryService {
    void repeatSkill(String id);

    List<RepeatHistory> getBySkillId(String skillId);

    List<RepeatHistory> getByUserId(String userId);
}
