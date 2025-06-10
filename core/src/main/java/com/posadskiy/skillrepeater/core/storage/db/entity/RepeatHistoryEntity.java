package com.posadskiy.skillrepeater.core.storage.db.entity;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.data.annotation.DateCreated;
import io.micronaut.data.annotation.GeneratedValue;
import io.micronaut.data.annotation.Id;
import io.micronaut.data.annotation.MappedEntity;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@MappedEntity("repeat_history")
public class RepeatHistoryEntity {

    @Id
    @GeneratedValue(GeneratedValue.Type.UUID)
    private String id;

    @NonNull
    @NotBlank
    private String skillId;

    @NonNull
    @NotBlank
    private String userId;

    @Nullable
    private String comment;

    @DateCreated
    private LocalDateTime repeatedAt;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public @NonNull @NotBlank String getSkillId() {
        return skillId;
    }

    public void setSkillId(@NonNull @NotBlank String skillId) {
        this.skillId = skillId;
    }

    public @NonNull @NotBlank String getUserId() {
        return userId;
    }

    public void setUserId(@NonNull @NotBlank String userId) {
        this.userId = userId;
    }

    public @Nullable String getComment() {
        return comment;
    }

    public void setComment(@Nullable String comment) {
        this.comment = comment;
    }

    public LocalDateTime getRepeatedAt() {
        return repeatedAt;
    }

    public void setRepeatedAt(LocalDateTime repeatedAt) {
        this.repeatedAt = repeatedAt;
    }
}
