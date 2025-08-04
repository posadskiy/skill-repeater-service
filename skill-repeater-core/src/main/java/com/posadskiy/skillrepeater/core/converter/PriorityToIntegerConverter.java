package com.posadskiy.skillrepeater.core.converter;

import com.posadskiy.skillrepeater.api.model.Priority;
import io.micronaut.core.convert.ConversionContext;
import io.micronaut.core.convert.TypeConverter;
import jakarta.inject.Singleton;

import java.util.Optional;

@Singleton
public class PriorityToIntegerConverter implements TypeConverter<Priority, Integer> {
    
    @Override
    public Optional<Integer> convert(Priority value, Class<Integer> targetType, ConversionContext context) {
        if (value == null) {
            return Optional.of(Priority.MEDIUM.getValue());
        }
        return Optional.of(value.getValue());
    }
} 