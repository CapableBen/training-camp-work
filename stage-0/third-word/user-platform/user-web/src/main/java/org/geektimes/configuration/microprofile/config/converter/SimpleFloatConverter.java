package org.geektimes.configuration.microprofile.config.converter;

import org.eclipse.microprofile.config.spi.Converter;

public class SimpleFloatConverter implements Converter<Float> {

    @Override
    public Float convert(String value) {
        if (value == null) {
            throw new IllegalArgumentException("Null value passed to convert");
        } else {
            return Float.parseFloat(value);
        }
    }
}
