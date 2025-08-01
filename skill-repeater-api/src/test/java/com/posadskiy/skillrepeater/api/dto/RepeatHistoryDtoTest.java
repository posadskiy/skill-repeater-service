package com.posadskiy.skillrepeater.api.dto;

import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class RepeatHistoryDtoTest {

    @Test
    void testRepeatHistoryDtoCreation() {
        Date now = new Date();
        RepeatHistoryDto repeatHistoryDto = new RepeatHistoryDto(
            "test-history-id",
            "test-skill-id",
            "test-user-id",
            "Test comment",
            now
        );

        assertEquals("test-history-id", repeatHistoryDto.id());
        assertEquals("test-skill-id", repeatHistoryDto.skillId());
        assertEquals("test-user-id", repeatHistoryDto.userId());
        assertEquals("Test comment", repeatHistoryDto.comment());
        assertEquals(now, repeatHistoryDto.repeatedAt());
    }

    @Test
    void testRepeatHistoryDtoEqualsAndHashCode() {
        Date now = new Date();
        RepeatHistoryDto repeatHistoryDto1 = new RepeatHistoryDto(
            "test-history-id",
            "test-skill-id",
            "test-user-id",
            "Test comment",
            now
        );

        RepeatHistoryDto repeatHistoryDto2 = new RepeatHistoryDto(
            "test-history-id",
            "test-skill-id",
            "test-user-id",
            "Test comment",
            now
        );

        assertEquals(repeatHistoryDto1, repeatHistoryDto2);
        assertEquals(repeatHistoryDto1.hashCode(), repeatHistoryDto2.hashCode());
    }

    @Test
    void testRepeatHistoryDtoWithNullComment() {
        Date now = new Date();
        RepeatHistoryDto repeatHistoryDto = new RepeatHistoryDto(
            "test-history-id",
            "test-skill-id",
            "test-user-id",
            null,
            now
        );

        assertNull(repeatHistoryDto.comment());
        assertEquals("test-history-id", repeatHistoryDto.id());
        assertEquals("test-skill-id", repeatHistoryDto.skillId());
        assertEquals("test-user-id", repeatHistoryDto.userId());
        assertEquals(now, repeatHistoryDto.repeatedAt());
    }

    @Test
    void testRepeatHistoryDtoToString() {
        RepeatHistoryDto repeatHistoryDto = new RepeatHistoryDto(
            "test-history-id",
            "test-skill-id",
            "test-user-id",
            "Test comment",
            new Date()
        );

        String toString = repeatHistoryDto.toString();
        assertNotNull(toString);
        assertTrue(toString.contains("test-history-id"));
        assertTrue(toString.contains("test-skill-id"));
    }
} 