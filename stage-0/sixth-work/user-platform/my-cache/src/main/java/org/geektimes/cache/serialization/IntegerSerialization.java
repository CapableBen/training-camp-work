package org.geektimes.cache.serialization;

public class IntegerSerialization implements Serialization<Integer> {

    @Override
    public Integer deserialize(byte[] target) {
        int value = 0;
        byte[] var4 = target;
        int var5 = target.length;

        for (int var6 = 0; var6 < var5; ++var6) {
            byte b = var4[var6];
            value <<= 8;
            value |= b & 255;
        }

        return value;
    }

    @Override
    public byte[] serialize(Integer source) {
        return source == null ? null : new byte[]{(byte) (source >>> 24), (byte) (source >>> 16), (byte) (source >>> 8), source.byteValue()};
    }

    @Override
    public void close() {

    }
}
