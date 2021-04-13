package org.geektimes.cache.redis;

import io.lettuce.core.codec.RedisCodec;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class GenericRedisCodec<K, V> implements RedisCodec<K, V> {

//    private Charset charset = Charset.forName("UTF-8");
    private Charset charset = StandardCharsets.UTF_8;

    @Override
    public K decodeKey(ByteBuffer bytes) {
        CharBuffer decode = charset.decode(bytes);
        return (K) decode;
    }

    @Override
    public V decodeValue(ByteBuffer bytes) {
        try {
            byte[] array = new byte[bytes.remaining()];
            bytes.get(array);
            ObjectInputStream is = new ObjectInputStream(new ByteArrayInputStream(array));
            return (V) is.readObject();
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public ByteBuffer encodeKey(K key) {
        return charset.encode(key.toString());
    }

    @Override
    public ByteBuffer encodeValue(V value) {
        try {
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            ObjectOutputStream os = new ObjectOutputStream(bytes);
            os.writeObject(value);
            return ByteBuffer.wrap(bytes.toByteArray());
        } catch (IOException e) {
            return null;
        }
    }
}