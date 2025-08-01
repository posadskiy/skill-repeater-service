package com.posadskiy.skillrepeater.core.exception;

import io.micronaut.serde.annotation.Serdeable;

@Serdeable
public record ErrorMessage(Boolean status, String message) {
}
