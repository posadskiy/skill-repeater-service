package com.posadskiy.skillrepeater.core.converter;

import com.posadskiy.skillrepeater.api.model.Priority;
import io.micronaut.core.convert.ConversionContext;
import io.micronaut.core.convert.TypeConverter;
import jakarta.inject.Singleton;

import java.util.Optional;

@Singleton
public class PriorityTypeConverter implements TypeConverter<Integer, Priority> {
    
    @Override
    public Optional<Priority> convert(Integer value, Class<Priority> targetType, ConversionContext context) {
        if (value == null) {
            return Optional.of(Priority.MEDIUM);
        }
        return Optional.of(Priority.fromValue(value));
    }
} 