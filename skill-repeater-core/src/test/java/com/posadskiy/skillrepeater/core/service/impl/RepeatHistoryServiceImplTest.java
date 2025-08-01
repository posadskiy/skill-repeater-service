package com.posadskiy.skillrepeater.core.service.impl;

import com.posadskiy.skillrepeater.core.mapper.entity.RepeatHistoryMapper;
import com.posadskiy.skillrepeater.core.model.RepeatHistory;
import com.posadskiy.skillrepeater.core.model.Skill;
import com.posadskiy.skillrepeater.core.service.SkillService;
import com.posadskiy.skillrepeater.core.storage.db.RepeatHistoryRepository;
import com.posadskiy.skillrepeater.core.storage.db.entity.RepeatHistoryEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class RepeatHistoryServiceImplTest {

    @Mock
    private RepeatHistoryRepository repeatHistoryRepository;

    @Mock
    private RepeatHistoryMapper repeatHistoryMapper;

    @Mock
    private SkillService skillService;

    private RepeatHistoryServiceImpl repeatHistoryService;

    private String testSkillId;
    private String testUserId;
    private Skill testSkill;
    private RepeatHistory testRepeatHistory;
    private RepeatHistoryEntity testRepeatHistoryEntity;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        repeatHistoryService = new RepeatHistoryServiceImpl(repeatHistoryRepository, repeatHistoryMapper, skillService);

        // Setup test data
        testSkillId = UUID.randomUUID().toString();
        testUserId = "test-user";
        
        testSkill = new Skill();
        testSkill.setId(testSkillId);
        testSkill.setUserId(testUserId);
        testSkill.setName("Java");
        testSkill.setDescription("Java programming language");
        testSkill.setPeriod(ChronoUnit.DAYS);
        testSkill.setNumber(1);
        testSkill.setLevel(1);
        testSkill.setLastRepeated(LocalDateTime.now());
        testSkill.setNextRepeated(LocalDateTime.now());

        testRepeatHistory = new RepeatHistory();
        testRepeatHistory.setId(UUID.randomUUID().toString());
        testRepeatHistory.setSkillId(testSkillId);
        testRepeatHistory.setUserId(testUserId);
        testRepeatHistory.setRepeatedAt(LocalDateTime.now());

        testRepeatHistoryEntity = new RepeatHistoryEntity();
        testRepeatHistoryEntity.setId(testRepeatHistory.getId());
        testRepeatHistoryEntity.setSkillId(testRepeatHistory.getSkillId());
        testRepeatHistoryEntity.setUserId(testRepeatHistory.getUserId());
        testRepeatHistoryEntity.setRepeatedAt(testRepeatHistory.getRepeatedAt());

        // Setup mock behavior
        when(skillService.get(testSkillId)).thenReturn(testSkill);
        when(repeatHistoryMapper.mapToEntity(any(RepeatHistory.class))).thenReturn(testRepeatHistoryEntity);
        when(repeatHistoryMapper.mapFromEntity(any(RepeatHistoryEntity.class))).thenReturn(testRepeatHistory);
    }

    @Test
    void repeatSkill_ShouldCreateRepeatHistory() {
        // Arrange
        when(repeatHistoryRepository.save(any(RepeatHistoryEntity.class))).thenReturn(testRepeatHistoryEntity);

        // Act
        repeatHistoryService.repeatSkill(testSkillId);

        // Assert
        verify(skillService).get(testSkillId);
        verify(repeatHistoryRepository).save(any(RepeatHistoryEntity.class));
        verify(repeatHistoryMapper).mapToEntity(any(RepeatHistory.class));
    }

    @Test
    void getBySkillId_ShouldReturnListOfRepeatHistory() {
        // Arrange
        when(repeatHistoryRepository.findBySkillId(testSkillId)).thenReturn(List.of(testRepeatHistoryEntity));

        // Act
        List<RepeatHistory> results = repeatHistoryService.getBySkillId(testSkillId);

        // Assert
        assertNotNull(results);
        assertEquals(1, results.size());
        assertEquals(testRepeatHistory.getId(), results.get(0).getId());
        assertEquals(testRepeatHistory.getSkillId(), results.get(0).getSkillId());
        verify(repeatHistoryRepository).findBySkillId(testSkillId);
        verify(repeatHistoryMapper).mapFromEntity(testRepeatHistoryEntity);
    }

    @Test
    void getByUserId_ShouldReturnListOfRepeatHistory() {
        // Arrange
        when(repeatHistoryRepository.findByUserId(testUserId)).thenReturn(List.of(testRepeatHistoryEntity));

        // Act
        List<RepeatHistory> results = repeatHistoryService.getByUserId(testUserId);

        // Assert
        assertNotNull(results);
        assertEquals(1, results.size());
        assertEquals(testRepeatHistory.getId(), results.get(0).getId());
        assertEquals(testRepeatHistory.getUserId(), results.get(0).getUserId());
        verify(repeatHistoryRepository).findByUserId(testUserId);
        verify(repeatHistoryMapper).mapFromEntity(testRepeatHistoryEntity);
    }
} 
