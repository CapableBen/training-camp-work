package org.geektimes.configuration.microprofile.config.converter;

import org.eclipse.microprofile.config.spi.Converter;

public class SimpleIntegerConverter implements Converter<Integer> {

    @Override
    public Integer convert(String value) {
        if (value == null) {
            throw new IllegalArgumentException("Null value passed to convert");
        } else {
            return Integer.parseInt(value);
        }
    }
}
