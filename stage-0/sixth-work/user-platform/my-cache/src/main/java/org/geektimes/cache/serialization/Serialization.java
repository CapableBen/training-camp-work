package org.geektimes.cache.serialization;

import java.io.Closeable;

public interface Serialization<T> extends Closeable {

    T deserialize(byte[] target);

    byte[] serialize(T source);

    void close();
}
