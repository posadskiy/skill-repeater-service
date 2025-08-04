package com.posadskiy.skillrepeater.api.dto;

import io.micronaut.core.annotation.Introspected;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.serde.annotation.Serdeable;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

import java.time.temporal.ChronoUnit;
import java.util.Date;
import com.posadskiy.skillrepeater.api.model.Priority;

@Serdeable
@Introspected
@Schema(description = "Data transfer object representing a skill that needs to be repeated")
public record SkillDto(
    @Schema(description = "Unique identifier of the skill", example = "123e4567-e89b-12d3-a456-426614174000")
    @Nullable String id,

    @Schema(description = "ID of the user who owns this skill", example = "1")
    @NonNull @NotBlank String userId,

    @Schema(description = "Name of the skill", example = "Java Programming")
    @NonNull @NotBlank String name,

    @Schema(description = "Optional description of the skill", example = "Core Java concepts and best practices")
    @Nullable String description,

    @Schema(description = "Repetition period (e.g., 'HOURS', 'DAYS', 'WEEKS', 'MONTHS', 'YEARS')", example = "DAYS")
    @NonNull @NotBlank ChronoUnit period,

    @Schema(description = "Number of repetitions or specific interval", example = "3")
    @NonNull @NotBlank String number,

    @Schema(description = "Date and time of the last repetition", example = "2024-03-20T10:30:00Z")
    @Nullable Date lastRepeated,
    
    @Schema(description = "Date and time of the next repetition", example = "2024-04-20T10:30:00Z")
    @Nullable Date nextRepeated,

    @Schema(description = "Current level of skill mastery (1-5)", example = "3", minimum = "1", maximum = "5")
    @Nullable Integer level,

    @Schema(description = "Priority level of the skill (LOW, MEDIUM, HIGH, CRITICAL)", example = "MEDIUM")
    @Nullable Priority priority
) {
}
