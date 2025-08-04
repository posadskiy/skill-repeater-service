package com.posadskiy.skillrepeater.api.dto;

import org.junit.jupiter.api.Test;

import java.time.temporal.ChronoUnit;
import java.util.Date;
import com.posadskiy.skillrepeater.api.model.Priority;

import static org.junit.jupiter.api.Assertions.*;

class SkillDtoTest {

    @Test
    void testSkillDtoCreation() {
        SkillDto skillDto = new SkillDto(
            "test-skill-id",
            1L,
            "Java Programming",
            "Learn Java programming language",
            ChronoUnit.DAYS,
            "1",
            new Date(),
            new Date(),
            1,
            Priority.MEDIUM
        );

        assertEquals("test-skill-id", skillDto.id());
        assertEquals(1L, skillDto.userId());
        assertEquals("Java Programming", skillDto.name());
        assertEquals("Learn Java programming language", skillDto.description());
        assertEquals(ChronoUnit.DAYS, skillDto.period());
        assertEquals("1", skillDto.number());
        assertEquals(1, skillDto.level());
        assertEquals(Priority.MEDIUM, skillDto.priority());
        assertNotNull(skillDto.lastRepeated());
        assertNotNull(skillDto.nextRepeated());
    }

    @Test
    void testSkillDtoEqualsAndHashCode() {
        Date now = new Date();
        SkillDto skillDto1 = new SkillDto(
            "test-skill-id",
            1L,
            "Java Programming",
            "Learn Java programming language",
            ChronoUnit.DAYS,
            "1",
            now,
            now,
            1,
            Priority.MEDIUM
        );

        SkillDto skillDto2 = new SkillDto(
            "test-skill-id",
            1L,
            "Java Programming",
            "Learn Java programming language",
            ChronoUnit.DAYS,
            "1",
            now,
            now,
            1,
            Priority.MEDIUM
        );

        assertEquals(skillDto1, skillDto2);
        assertEquals(skillDto1.hashCode(), skillDto2.hashCode());
    }

    @Test
    void testSkillDtoWithNullValues() {
        SkillDto skillDto = new SkillDto(
            null,
            1L,
            "Java Programming",
            null,
            ChronoUnit.DAYS,
            "1",
            null,
            null,
            null,
            null
        );

        assertNull(skillDto.id());
        assertNull(skillDto.description());
        assertNull(skillDto.lastRepeated());
        assertNull(skillDto.nextRepeated());
        assertNull(skillDto.level());
        assertEquals(1L, skillDto.userId());
        assertEquals("Java Programming", skillDto.name());
        assertEquals(ChronoUnit.DAYS, skillDto.period());
        assertEquals("1", skillDto.number());
    }

    @Test
    void testSkillDtoToString() {
        SkillDto skillDto = new SkillDto(
            "test-skill-id",
            1L,
            "Java Programming",
            "Learn Java programming language",
            ChronoUnit.DAYS,
            "1",
            new Date(),
            new Date(),
            1,
            Priority.MEDIUM
        );

        String toString = skillDto.toString();
        assertNotNull(toString);
        assertTrue(toString.contains("test-skill-id"));
        assertTrue(toString.contains("Java Programming"));
    }
} 