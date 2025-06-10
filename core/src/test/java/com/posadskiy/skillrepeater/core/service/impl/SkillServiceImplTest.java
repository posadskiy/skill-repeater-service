package com.posadskiy.skillrepeater.core.service.impl;

import com.posadskiy.skillrepeater.core.mapper.entity.SkillEntityMapper;
import com.posadskiy.skillrepeater.core.model.Skill;
import com.posadskiy.skillrepeater.core.storage.db.SkillRepository;
import com.posadskiy.skillrepeater.core.storage.db.entity.SkillEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class SkillServiceImplTest {

    @Mock
    private SkillRepository skillRepository;

    @Mock
    private SkillEntityMapper skillEntityMapper;

    private SkillServiceImpl skillService;

    private String testUserId;
    private Skill testSkill;
    private SkillEntity testSkillEntity;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        skillService = new SkillServiceImpl(skillRepository, skillEntityMapper);

        // Setup test data
        testUserId = "test-user";
        testSkill = new Skill();
        testSkill.setId(UUID.randomUUID().toString());
        testSkill.setUserId(testUserId);
        testSkill.setName("Java");
        testSkill.setDescription("Java programming language");
        testSkill.setPeriod(ChronoUnit.DAYS);
        testSkill.setNumber(1);
        testSkill.setLevel(1);
        testSkill.setLastRepeated(LocalDateTime.now());
        testSkill.setNextRepeated(LocalDateTime.now());

        testSkillEntity = new SkillEntity();
        testSkillEntity.setId(testSkill.getId());
        testSkillEntity.setUserId(testSkill.getUserId());
        testSkillEntity.setName(testSkill.getName());
        testSkillEntity.setDescription(testSkill.getDescription());
        testSkillEntity.setPeriod(testSkill.getPeriod());
        testSkillEntity.setNumber(testSkill.getNumber());
        testSkillEntity.setLevel(testSkill.getLevel());
        testSkillEntity.setLastRepeated(testSkill.getLastRepeated());
        testSkillEntity.setNextRepeated(testSkill.getNextRepeated());

        // Setup mock behavior
        when(skillEntityMapper.mapToEntity(any(Skill.class))).thenReturn(testSkillEntity);
        when(skillEntityMapper.mapFromEntity(any(SkillEntity.class))).thenReturn(testSkill);
    }

    @Test
    void addSkill_ShouldSaveAndReturnSkill() {
        // Arrange
        when(skillRepository.save(any(SkillEntity.class))).thenReturn(testSkillEntity);

        // Act
        Skill result = skillService.addSkill(testSkill);

        // Assert
        assertNotNull(result);
        assertEquals(testSkill.getId(), result.getId());
        assertEquals(testSkill.getName(), result.getName());
        verify(skillRepository).save(any(SkillEntity.class));
    }

    @Test
    void editSkill_ShouldUpdateAndReturnSkill() {
        // Arrange
        when(skillRepository.update(any(SkillEntity.class))).thenReturn(testSkillEntity);

        // Act
        Skill result = skillService.editSkill(testSkill);

        // Assert
        assertNotNull(result);
        assertEquals(testSkill.getId(), result.getId());
        assertEquals(testSkill.getName(), result.getName());
        verify(skillRepository).update(any(SkillEntity.class));
    }

    @Test
    void deleteSkill_ShouldDeleteSkill() {
        // Act
        skillService.deleteSkill(testSkill);

        // Assert
        verify(skillRepository).delete(any(SkillEntity.class));
    }

    @Test
    void get_ShouldReturnSkill() {
        // Arrange
        when(skillRepository.findById(testSkill.getId())).thenReturn(Optional.of(testSkillEntity));

        // Act
        Skill result = skillService.get(testSkill.getId());

        // Assert
        assertNotNull(result);
        assertEquals(testSkill.getId(), result.getId());
        assertEquals(testSkill.getName(), result.getName());
        verify(skillRepository).findById(testSkill.getId());
    }

    @Test
    void getAllByUser_ShouldReturnListOfSkills() {
        // Arrange
        when(skillRepository.findByUserId(testUserId)).thenReturn(List.of(testSkillEntity));

        // Act
        List<Skill> results = skillService.getAllByUser(testUserId);

        // Assert
        assertNotNull(results);
        assertEquals(1, results.size());
        assertEquals(testSkill.getId(), results.get(0).getId());
        assertEquals(testSkill.getName(), results.get(0).getName());
        verify(skillRepository).findByUserId(testUserId);
    }
} 
