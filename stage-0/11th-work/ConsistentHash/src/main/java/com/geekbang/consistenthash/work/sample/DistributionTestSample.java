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
package com.geekbang.consistenthash.work.sample;


import com.geekbang.consistenthash.work.enums.LoadFactor;
import com.geekbang.consistenthash.work.loadbalancer.ConsistentHashRouter;
import com.geekbang.consistenthash.work.node.ServiceNode;
import com.geekbang.consistenthash.work.strategy.Ketama;
import com.geekbang.consistenthash.work.util.HashUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicInteger;

import static com.geekbang.consistenthash.work.util.HashUtil.getRandomIp;

/**
 * reference https://github.com/Jaskey/ConsistentHash.git
 * <p>
 * 通过 Java 实现两种 (以及) 更多的一致性 Hash 算法 (可选) 实现服务节点动态更新
 * org.apache.dubbo.rpc.cluster.loadbalance.ConsistentHashLoadBalance
 * <p>
 * a test sample to test the hash function routing distribution, by default ConsistentHashRouter will use a inner MD5 hash algorithm
 */
public class DistributionTestSample {

    public static void main(String[] args) {
        //initialize 4 service node
        ServiceNode node1 = new ServiceNode("IDC1", "10.8.1.11", 8080, LoadFactor._DEFAULT);
        ServiceNode node2 = new ServiceNode("IDC1", "10.8.3.99", 8080, LoadFactor.MEMORY8G);
        ServiceNode node3 = new ServiceNode("IDC1", "10.9.11.105", 8080, LoadFactor.MEMORY16G);
        ServiceNode node4 = new ServiceNode("IDC1", "10.10.9.210", 8080, LoadFactor.MEMORY32G);

        //hash them to hash ring.
        // 1. By default a MD5 hash function will be used, you can modify a little if you want to test your own hash funtion
        // 2. Another factor which is will influence distribution is the numbers of virtual nodes, you can change this factor , below, we use 20 virtual nodes for each physical node.
        ConsistentHashRouter<ServiceNode> consistentHashRouter = new ConsistentHashRouter<>(
                Arrays.asList(node1, node2, node3, node4), new Ketama());

        List<String> requestIps = new ArrayList<>();
        for (int i = 0; i < 10000; i++) {
            requestIps.add(getRandomIp());
        }
        String[] ips = requestIps.toArray(new String[requestIps.size()]);

        System.out.println();
        System.out.println("-------------output init distribution result------------");
        println(goRoute(consistentHashRouter, ips));

        ServiceNode node5 = new ServiceNode("IDC2", "127.0.0.1", 8080, LoadFactor.MEMORY8G);
        System.out.println();
        System.out.println("-------------putting new node online " + node5.getKey() + "------------");
        consistentHashRouter.addNode(node5);
        println(goRoute(consistentHashRouter, ips));

        consistentHashRouter.removeNode(node3);
        System.out.println();
        System.out.println("-------------remove node online " + node3.getKey() + "------------");
        println(goRoute(consistentHashRouter, ips));
    }

    private static TreeMap<String, AtomicInteger> goRoute(ConsistentHashRouter<ServiceNode> consistentHashRouter, String... requestIps) {
        TreeMap<String, AtomicInteger> res = new TreeMap<>();
        for (String requestIp : requestIps) {
            ServiceNode mynode = consistentHashRouter.routeNode(requestIp);
            res.putIfAbsent(mynode.getKey(), new AtomicInteger());
            res.get(mynode.getKey()).incrementAndGet();
//            System.out.println(requestIp + " is routed to " + mynode);
        }
        return res;
    }

    private static void println(TreeMap<String, AtomicInteger> treeMap) {
        System.out.println(treeMap.toString());
        Integer[] integers = treeMap.values().stream().map(AtomicInteger::intValue).toArray(Integer[]::new);
        System.out.printf("variance： %s, standardDeviation： %s \n", HashUtil.variance(integers), HashUtil.standardDeviation(integers));
    }

}
