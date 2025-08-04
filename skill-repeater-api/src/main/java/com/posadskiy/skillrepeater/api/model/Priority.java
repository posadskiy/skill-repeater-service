package com.posadskiy.skillrepeater.api.model;

import com.fasterxml.jackson.annotation.JsonCreator;

/**
 * Priority levels for skills
 */
public enum Priority {
    LOW(1, "Low"),
    MEDIUM(2, "Medium"),
    HIGH(3, "High"),
    CRITICAL(4, "Critical");

    private final int value;
    private final String displayName;

    Priority(int value, String displayName) {
        this.value = value;
        this.displayName = displayName;
    }

    public int getValue() {
        return value;
    }

    public String getDisplayName() {
        return displayName;
    }

    @JsonCreator
    public static Priority fromValue(int value) {
        for (Priority priority : values()) {
            if (priority.value == value) {
                return priority;
            }
        }
        // Default to MEDIUM for invalid values instead of throwing exception
        return MEDIUM;
    }

    public static Priority fromString(String name) {
        try {
            return Priority.valueOf(name.toUpperCase());
        } catch (IllegalArgumentException e) {
            return MEDIUM; // Default to MEDIUM for invalid values
        }
    }
} 