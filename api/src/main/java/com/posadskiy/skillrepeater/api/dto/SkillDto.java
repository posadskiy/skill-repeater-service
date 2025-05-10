package com.posadskiy.skillrepeater.api.dto;

import io.micronaut.core.annotation.Introspected;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.serde.annotation.Serdeable;
import jakarta.validation.constraints.NotBlank;

import java.util.Date;

@Serdeable
@Introspected
public record SkillDto(
    @Nullable String id,
    @NonNull @NotBlank Long userId,
    @NonNull @NotBlank String name,
    @Nullable String description,
    @NonNull @NotBlank String period,
    @NonNull @NotBlank String number,
    @Nullable Date lastRepeat,
    @Nullable Integer level
) {
}
