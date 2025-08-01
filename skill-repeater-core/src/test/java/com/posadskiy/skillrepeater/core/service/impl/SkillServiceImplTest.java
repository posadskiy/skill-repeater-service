package com.posadskiy.skillrepeater.core.service.impl;

import com.posadskiy.skillrepeater.core.mapper.entity.SkillEntityMapper;
import com.posadskiy.skillrepeater.core.model.Skill;
import com.posadskiy.skillrepeater.core.storage.db.SkillRepository;
import com.posadskiy.skillrepeater.core.storage.db.entity.SkillEntity;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.exceptions.HttpStatusException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

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
        assertEquals(testSkill.getDescription(), result.getDescription());
        assertEquals(testSkill.getPeriod(), result.getPeriod());
        assertEquals(testSkill.getNumber(), result.getNumber());
        assertEquals(testSkill.getLevel(), result.getLevel());
        verify(skillRepository).save(any(SkillEntity.class));
        verify(skillEntityMapper).mapToEntity(testSkill);
        verify(skillEntityMapper).mapFromEntity(testSkillEntity);
    }

    @Test
    void editSkill_ShouldUpdateAndReturnSkill() {
        // Arrange
        when(skillRepository.findById(testSkill.getId())).thenReturn(Optional.of(testSkillEntity));
        when(skillRepository.update(any(SkillEntity.class))).thenReturn(testSkillEntity);

        // Act
        Skill result = skillService.editSkill(testSkill);

        // Assert
        assertNotNull(result);
        assertEquals(testSkill.getId(), result.getId());
        assertEquals(testSkill.getName(), result.getName());
        verify(skillRepository).findById(testSkill.getId());
        verify(skillRepository).update(any(SkillEntity.class));
        verify(skillEntityMapper, times(1)).mapToEntity(any(Skill.class));
        verify(skillEntityMapper, times(2)).mapFromEntity(testSkillEntity);
    }

    @Test
    void deleteSkill_ShouldDeleteSkill() {
        // Act
        skillService.deleteSkill(testSkill);

        // Assert
        verify(skillRepository).delete(any(SkillEntity.class));
        verify(skillEntityMapper).mapToEntity(testSkill);
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
        verify(skillEntityMapper).mapFromEntity(testSkillEntity);
    }

    @Test
    void get_WithNonExistentId_ShouldThrowHttpStatusException() {
        // Arrange
        String nonExistentId = "non-existent-id";
        when(skillRepository.findById(nonExistentId)).thenReturn(Optional.empty());

        // Act & Assert
        HttpStatusException exception = assertThrows(HttpStatusException.class, () -> {
            skillService.get(nonExistentId);
        });

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
        assertTrue(exception.getMessage().contains("Skill not found with id: " + nonExistentId));
        verify(skillRepository).findById(nonExistentId);
        verify(skillEntityMapper, never()).mapFromEntity(any());
    }

    @Test
    void get_WithNullId_ShouldThrowHttpStatusException() {
        // Arrange
        when(skillRepository.findById(null)).thenReturn(Optional.empty());

        // Act & Assert
        HttpStatusException exception = assertThrows(HttpStatusException.class, () -> {
            skillService.get(null);
        });

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
        assertTrue(exception.getMessage().contains("Skill not found with id: null"));
        verify(skillRepository).findById(null);
        verify(skillEntityMapper, never()).mapFromEntity(any());
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
        verify(skillEntityMapper).mapFromEntity(testSkillEntity);
    }

    @Test
    void getAllByUser_WithEmptyResult_ShouldReturnEmptyList() {
        // Arrange
        when(skillRepository.findByUserId(testUserId)).thenReturn(List.of());

        // Act
        List<Skill> results = skillService.getAllByUser(testUserId);

        // Assert
        assertNotNull(results);
        assertTrue(results.isEmpty());
        verify(skillRepository).findByUserId(testUserId);
        verify(skillEntityMapper, never()).mapFromEntity(any());
    }

    @Test
    void getAllByUser_WithMultipleSkills_ShouldReturnAllSkills() {
        // Arrange
        SkillEntity skillEntity2 = new SkillEntity();
        skillEntity2.setId(UUID.randomUUID().toString());
        skillEntity2.setUserId(testUserId);
        skillEntity2.setName("Python");
        skillEntity2.setDescription("Python programming language");
        skillEntity2.setPeriod(ChronoUnit.DAYS);
        skillEntity2.setNumber(2);
        skillEntity2.setLevel(2);

        Skill skill2 = new Skill();
        skill2.setId(skillEntity2.getId());
        skill2.setUserId(skillEntity2.getUserId());
        skill2.setName(skillEntity2.getName());
        skill2.setDescription(skillEntity2.getDescription());
        skill2.setPeriod(ChronoUnit.DAYS);
        skill2.setNumber(skillEntity2.getNumber());
        skill2.setLevel(skillEntity2.getLevel());

        when(skillRepository.findByUserId(testUserId)).thenReturn(List.of(testSkillEntity, skillEntity2));
        when(skillEntityMapper.mapFromEntity(testSkillEntity)).thenReturn(testSkill);
        when(skillEntityMapper.mapFromEntity(skillEntity2)).thenReturn(skill2);

        // Act
        List<Skill> results = skillService.getAllByUser(testUserId);

        // Assert
        assertNotNull(results);
        assertEquals(2, results.size());
        assertEquals(testSkill.getName(), results.get(0).getName());
        assertEquals(skill2.getName(), results.get(1).getName());
        verify(skillRepository).findByUserId(testUserId);
        verify(skillEntityMapper).mapFromEntity(testSkillEntity);
        verify(skillEntityMapper).mapFromEntity(skillEntity2);
    }
} 
