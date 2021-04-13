package org.geektimes.cache.serialization;

import java.nio.ByteBuffer;

public class ByteBufferSerialization implements Serialization<ByteBuffer> {

    @Override
    public ByteBuffer deserialize(byte[] target) {
        return ByteBuffer.wrap(target);
    }

    @Override
    public byte[] serialize(ByteBuffer source) {
        return new byte[source.remaining()];
    }

    @Override
    public void close() {

    }
}
