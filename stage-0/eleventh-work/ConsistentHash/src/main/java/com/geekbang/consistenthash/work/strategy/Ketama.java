package com.geekbang.consistenthash.work.strategy;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * TODO
 *
 * @author: Ben
 * @since: TODO
 * @date: 2021-05-19
 */
public class Ketama implements HashStrategy {

    private static MessageDigest md5Digest;

    static {
        try {
            md5Digest = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("MD5 not supported", e);
        }
    }

//    @Override
//    public long hash(String key) {
//        byte[] bKey = computeMd5(key);
//        long rv = ((long) (bKey[3] & 0xFF) << 24)
//                | ((long) (bKey[2] & 0xFF) << 16)
//                | ((long) (bKey[1] & 0xFF) << 8)
//                | (bKey[0] & 0xFF);
//        return (int) (rv & 0xffffffffL);
//    }

    @Override
    public long hash(String key) {
        byte[] digest = computeMd5(key);

        // 0-2^31-1
        long h = 0;
        for (int i = 0; i < 4; i++) {
            h <<= 8;
            h |= ((int) digest[i]) & 0xFF;
        }
        return h;
    }

    /**
     * Get the md5 of the given key.
     */
    public static byte[] computeMd5(String k) {
//        md5Digest.reset();
//        md5Digest.update(k.getBytes());
//        return md5Digest.digest();
        MessageDigest md5;
        try {
            md5 = (MessageDigest) md5Digest.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException("clone of MD5 not supported", e);
        }
        md5.update(k.getBytes());
        return md5.digest();
    }
}
