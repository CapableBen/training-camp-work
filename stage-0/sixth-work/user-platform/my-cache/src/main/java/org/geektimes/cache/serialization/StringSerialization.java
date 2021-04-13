package org.geektimes.cache.serialization;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class StringSerialization implements Serialization<String> {

    private Charset charset = StandardCharsets.UTF_8;

    @Override
    public String deserialize(byte[] target) {
        return target == null ? null : new String(target, charset);
    }

    @Override
    public byte[] serialize(String source) {
        return source == null ? null : source.getBytes(charset);
    }

    @Override
    public void close() {

    }
}