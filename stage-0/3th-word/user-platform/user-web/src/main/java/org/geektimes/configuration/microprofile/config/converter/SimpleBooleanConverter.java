package org.geektimes.configuration.microprofile.config.converter;

import org.eclipse.microprofile.config.spi.Converter;

public class SimpleBooleanConverter implements Converter<Boolean> {

    @Override
    public Boolean convert(String value) {
        if (value == null) {
            throw new IllegalArgumentException("Null value passed to convert");
        } else {
            return Boolean.parseBoolean(value);
        }
    }
}
