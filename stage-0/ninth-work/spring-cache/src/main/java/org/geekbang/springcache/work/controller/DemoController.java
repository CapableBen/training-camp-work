package org.geekbang.springcache.work.controller;

import org.geekbang.springcache.work.domain.Book;
import org.geekbang.springcache.work.service.CacheService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.swing.*;
import java.util.List;

@RestController
public class DemoController {

//    Spring Cache 与 Redis 整合
//      如何清除某个 Spring Cache 所有的 Keys 关联的对象
//          如果 Redis 中心化方案，Redis + Sentinel
//          如果 Redis 去中心化方案，Redis Cluster
//      如何将 RedisCacheManager 与 @Cacheable 注解打通

    @Autowired
    private CacheService cacheService;

    @RequestMapping(value = "/get")
    public String cacheable(@RequestParam String id) {
        Book book = cacheService.cacheable(id);
        System.out.println("cacheable = " + book);
        return "cacheable";
    }

    @RequestMapping(value = "/get1")
    public String cacheable1(@RequestParam String id) {
        Book book = cacheService.cacheable1(id);
        System.out.println("cacheable = " + book);
        return "cacheable";
    }

    @RequestMapping(value = "/put")
    public String cachePut(@RequestParam String id) {
        Book book = cacheService.cachePut(id);
        System.out.println("cachePut = " + book);
        return "cachePut";
    }

    @RequestMapping(value = "/del")
    public String cacheEvict(@RequestParam String id) {
        cacheService.cacheEvict(id);
        return "cacheEvict";
    }

    @RequestMapping(value = "/clear")
    public String cacheClear() {
        cacheService.cacheClear();
        return "cacheClear";
    }

//    @RequestMapping(value = "/clear")
//    public String cacheClear(@RequestParam(required = false) String cacheName) {
//        cacheService.cacheClear(cacheName);
//        return "cacheClear";
//    }

}