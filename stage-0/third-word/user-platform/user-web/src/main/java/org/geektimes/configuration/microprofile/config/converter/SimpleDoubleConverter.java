package org.geektimes.configuration.microprofile.config.converter;

import org.eclipse.microprofile.config.spi.Converter;

public class SimpleDoubleConverter implements Converter<Double> {

    @Override
    public Double convert(String value) {
        if (value == null) {
            throw new IllegalArgumentException("Null value passed to convert");
        } else {
            return Double.parseDouble(value);
        }
    }
}
