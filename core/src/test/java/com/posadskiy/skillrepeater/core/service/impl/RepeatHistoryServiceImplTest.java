package com.posadskiy.skillrepeater.core.service.impl;

import com.posadskiy.skillrepeater.core.mapper.entity.RepeatHistoryMapper;
import com.posadskiy.skillrepeater.core.model.RepeatHistory;
import com.posadskiy.skillrepeater.core.storage.db.RepeatHistoryRepository;
import com.posadskiy.skillrepeater.core.storage.db.entity.RepeatHistoryEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class RepeatHistoryServiceImplTest {

    @Mock
    private RepeatHistoryRepository repeatHistoryRepository;

    @Mock
    private RepeatHistoryMapper repeatHistoryMapper;

    private RepeatHistoryServiceImpl repeatHistoryService;

    private String testSkillId;
    private RepeatHistory testRepeatHistory;
    private RepeatHistoryEntity testRepeatHistoryEntity;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        repeatHistoryService = new RepeatHistoryServiceImpl(repeatHistoryRepository, repeatHistoryMapper);

        // Setup test data
        testSkillId = UUID.randomUUID().toString();
        testRepeatHistory = new RepeatHistory();
        testRepeatHistory.setSkillId(testSkillId);

        testRepeatHistoryEntity = new RepeatHistoryEntity();
        testRepeatHistoryEntity.setSkillId(testSkillId);

        // Setup mock behavior
        when(repeatHistoryMapper.mapToEntity(any(RepeatHistory.class))).thenReturn(testRepeatHistoryEntity);
    }

    @Test
    void repeatSkill_ShouldCreateRepeatHistory() {
        // Act
        repeatHistoryService.repeatSkill(testSkillId);

        // Assert
        verify(repeatHistoryRepository).save(any(RepeatHistoryEntity.class));
    }
} 