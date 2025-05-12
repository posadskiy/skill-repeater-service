package com.posadskiy.skillrepeater.core.model;

import io.micronaut.core.annotation.Introspected;

import java.time.LocalDateTime;

@Introspected
public class Skill {
    private String id;
    private String userId;
    private String name;
    private String description;
    private String period;
    private Integer number;
    private LocalDateTime lastRepeated;
    private LocalDateTime nextRepeated;
    private Integer level;

    public LocalDateTime getNextRepeated() {
        return nextRepeated;
    }

    public void setNextRepeated(LocalDateTime nextRepeated) {
        this.nextRepeated = nextRepeated;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public LocalDateTime getLastRepeated() {
        return lastRepeated;
    }

    public void setLastRepeated(LocalDateTime lastRepeated) {
        this.lastRepeated = lastRepeated;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }
}
