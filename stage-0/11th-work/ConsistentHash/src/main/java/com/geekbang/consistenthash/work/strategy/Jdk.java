package com.geekbang.consistenthash.work.strategy;

/**
 * TODO
 *
 * @author: Ben
 * @since: TODO
 * @date: 2021-05-19
 */
public class Jdk implements HashStrategy{
    @Override
    public long hash(String key) {
        return key.hashCode();
    }
}
