package com.posadskiy.skillrepeater.api.dto;

import io.micronaut.core.annotation.Introspected;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.serde.annotation.Serdeable;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Date;

@Serdeable
@Introspected
@Schema(description = "Data transfer object representing a skill repetition history entry")
public record RepeatHistoryDto(
    @Schema(description = "Unique identifier of the history entry", example = "123e4567-e89b-12d3-a456-426614174000")
    @NonNull String id,

    @Schema(description = "ID of the skill that was repeated", example = "123e4567-e89b-12d3-a456-426614174000")
    @NonNull String skillId,

    @Schema(description = "ID of the user who repeated the skill", example = "123e4567-e89b-12d3-a456-426614174000")
    @NonNull String userId,

    @Schema(description = "Optional comment about the repetition", example = "Completed all exercises")
    String comment,

    @Schema(description = "Date and time when the skill was repeated", example = "2024-03-20T10:30:00Z")
    @NonNull Date repeatedAt
) {} 