package com.posadskiy.skillrepeater.core.model;

import io.micronaut.core.annotation.Introspected;

import java.time.LocalDateTime;

@Introspected
public class RepeatHistory {
    private String id;
    private String skillId;
    private String comment;
    private LocalDateTime repeatedAt;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSkillId() {
        return skillId;
    }

    public void setSkillId(String skillId) {
        this.skillId = skillId;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public LocalDateTime getRepeatedAt() {
        return repeatedAt;
    }

    public void setRepeatedAt(LocalDateTime repeatedAt) {
        this.repeatedAt = repeatedAt;
    }
}
