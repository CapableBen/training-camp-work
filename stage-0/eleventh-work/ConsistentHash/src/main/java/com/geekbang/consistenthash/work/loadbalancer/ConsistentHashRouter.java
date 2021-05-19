/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.geekbang.consistenthash.work.loadbalancer;

import com.geekbang.consistenthash.work.node.Node;
import com.geekbang.consistenthash.work.node.VirtualNode;
import com.geekbang.consistenthash.work.strategy.HashStrategy;
import com.geekbang.consistenthash.work.strategy.Jdk;
import com.geekbang.consistenthash.work.strategy.Ketama;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Collection;
import java.util.Iterator;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * @param <T>
 * @author linjunjie1103@gmail.com
 * <p>
 * To hash Node objects to a hash ring with a certain amount of virtual node.
 * Method routeNode will return a Node instance which the object key should be allocated to according to consistent hash algorithm
 */
public class ConsistentHashRouter<T extends Node> {
    private final SortedMap<Long, VirtualNode<T>> ring = new TreeMap<>();
    private final HashStrategy hashStrategy;

    public ConsistentHashRouter(Collection<T> pNodes) {
        // default Jdk hashcode
        this(pNodes, new Jdk());
    }

    /**
     * @param pNodes       collections of physical nodes
     * @param hashStrategy hash Function to hash Node instances
     */
    public ConsistentHashRouter(Collection<T> pNodes, HashStrategy hashStrategy) {
        if (hashStrategy == null) {
            throw new NullPointerException("Hash Function is null");
        }
        this.hashStrategy = hashStrategy;
        if (pNodes != null) {
            for (T pNode : pNodes) {
                addNode(pNode, pNode.getVirtualNode());
            }
        }
        System.out.println("ring = " + ring);
    }

    public void addNode(T pNode) {
        this.addNode(pNode, pNode.getVirtualNode());
    }

    /**
     * add physic node to the hash ring with some virtual nodes
     *
     * @param pNode      physical node needs added to hash ring
     * @param vNodeCount the number of virtual node of the physical node. Value should be greater than or equals to 0
     */
    public void addNode(T pNode, int vNodeCount) {
        if (vNodeCount < 0) {
            throw new IllegalArgumentException("illegal virtual node counts :" + vNodeCount);
        }
        // 获取当前节点最大数量+1 -> 作为索引
        int existingReplicas = getExistingReplicas(pNode);
        // 每个节点动态添加虚拟节点数量
        for (int i = 0; i < vNodeCount; i++) {
            VirtualNode<T> vNode = new VirtualNode<>(pNode, i + existingReplicas);
            ring.put(hashStrategy.hash(vNode.getKey()), vNode);
        }
        System.out.println("ring = " + ring);
    }

    /**
     * remove the physical node from the hash ring
     *
     * @param pNode
     */
    public void removeNode(T pNode) {
        Iterator<Long> it = ring.keySet().iterator();
        while (it.hasNext()) {
            Long key = it.next();
            VirtualNode<T> virtualNode = ring.get(key);
            if (virtualNode.isVirtualNodeOf(pNode)) {
                it.remove();
            }
        }
    }

    /**
     * with a specified key, route the nearest Node instance in the current hash ring
     *
     * @param objectKey the object key to find a nearest Node
     * @return
     */
    public T routeNode(String objectKey) {
        if (ring.isEmpty()) {
            return null;
        }
        Long hashVal = hashStrategy.hash(objectKey);
        SortedMap<Long, VirtualNode<T>> tailMap = ring.tailMap(hashVal);
        Long nodeHashVal = !tailMap.isEmpty() ? tailMap.firstKey() : ring.firstKey();
        return ring.get(nodeHashVal).getPhysicalNode();
    }


    public int getExistingReplicas(T pNode) {
        int replicas = 0;
        for (VirtualNode<T> vNode : ring.values()) {
            if (vNode.isVirtualNodeOf(pNode)) {
                replicas++;
            }
        }
        return replicas;
    }

}
