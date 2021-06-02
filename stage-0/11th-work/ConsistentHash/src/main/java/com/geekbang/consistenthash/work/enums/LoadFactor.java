package com.geekbang.consistenthash.work.enums;

/**
 * TODO
 *
 * @author: Ben
 * @since: TODO
 * @date: 2021-05-19
 */
public enum LoadFactor {

    //
    _DEFAULT(1),
    MEMORY8G(5),
    MEMORY16G(10),
    MEMORY32G(20);

    private int vrNum;

    private LoadFactor(int vrNum) {
        this.vrNum = vrNum;
    }

    public int getVrNum() {
        return vrNum;
    }
}
