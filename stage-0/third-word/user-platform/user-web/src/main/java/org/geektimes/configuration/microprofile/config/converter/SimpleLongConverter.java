package org.geektimes.configuration.microprofile.config.converter;

import org.eclipse.microprofile.config.spi.Converter;

public class SimpleLongConverter implements Converter<Long> {

    @Override
    public Long convert(String value) {
        if (value == null) {
            throw new IllegalArgumentException("Null value passed to convert");
        } else {
            return Long.parseLong(value);
        }
    }
}
