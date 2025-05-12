package com.posadskiy.skillrepeater.core.storage.db.entity;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.data.annotation.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@MappedEntity("skills")
public class SkillEntity {

    @Id
    @GeneratedValue(GeneratedValue.Type.UUID)
    private String id;

    @NonNull
    @NotBlank
    private String userId;

    @NonNull
    @NotBlank
    private String name;

    @Nullable
    private String description;

    @NonNull
    @NotNull
    private String period;

    @NonNull
    @NotNull
    private Integer number;

    @NonNull
    @NotNull
    private Integer level;

    private LocalDateTime lastRepeated;

    private LocalDateTime nextRepeated;

    @DateUpdated
    private LocalDateTime updatedAt;

    @DateCreated
    private LocalDateTime createdAt;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public @NonNull @NotBlank String getUserId() {
        return userId;
    }

    public void setUserId(@NonNull @NotBlank String userId) {
        this.userId = userId;
    }

    public @NonNull @NotBlank String getName() {
        return name;
    }

    public void setName(@NonNull @NotBlank String name) {
        this.name = name;
    }

    public @Nullable String getDescription() {
        return description;
    }

    public void setDescription(@Nullable String description) {
        this.description = description;
    }

    public @NonNull @NotNull String getPeriod() {
        return period;
    }

    public void setPeriod(@NonNull @NotNull String period) {
        this.period = period;
    }

    public @NonNull @NotNull Integer getNumber() {
        return number;
    }

    public void setNumber(@NonNull @NotNull Integer number) {
        this.number = number;
    }

    public @NonNull @NotNull Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public LocalDateTime getLastRepeated() {
        return lastRepeated;
    }

    public void setLastRepeated(LocalDateTime lastRepeated) {
        this.lastRepeated = lastRepeated;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getNextRepeated() {
        return nextRepeated;
    }

    public void setNextRepeated(LocalDateTime nextRepeated) {
        this.nextRepeated = nextRepeated;
    }
}
