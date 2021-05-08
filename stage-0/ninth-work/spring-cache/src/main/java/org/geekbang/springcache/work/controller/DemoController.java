package org.geekbang.springcache.work.controller;

import org.geekbang.springcache.work.domain.Book;
import org.geekbang.springcache.work.service.CacheService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class DemoController {

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