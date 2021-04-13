package org.geektimes.cache.serialization;

import org.apache.kafka.common.utils.Bytes;

public class BytesSerialization implements Serialization<Bytes> {

    @Override
    public Bytes deserialize(byte[] target) {
        return new Bytes(target);
    }

    @Override
    public byte[] serialize(Bytes source) {
        return source.get();
    }

    @Override
    public void close() {

    }
}
