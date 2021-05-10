package org.geekbang.springcache.work.service;

import org.geekbang.springcache.work.domain.Book;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
@CacheConfig(cacheManager = "redisCacheManager", cacheNames = "book")
public class CacheService {

    @Cacheable(cacheNames = "book", key = "#id")
    public Book cacheable(String id) {
        System.out.println("cacheable id = " + id);
        Book book = new Book();
        book.setId(id);
        book.setName("cacheable");
        return book;
    }

    @Cacheable(cacheNames = "other", key = "#id")
    public Book cacheable1(String id) {
        System.out.println("cacheable id = " + id);
        Book book = new Book();
        book.setId(id);
        book.setName("cacheable");
        return book;
    }

//    @Cacheable(key = "#id")
//    public Book cacheable1(String id) {
//        System.out.println("cacheable id = " + id);
//        Book book = new Book();
//        book.setId(id);
//        book.setName("cacheable");
//        return book;
//    }

    @CachePut(key = "#id")
    public Book cachePut(String id) {
        System.out.println("cachePut id = " + id);
        Book book = new Book();
        book.setId(id);
        book.setName("cachePut");
        return book;
    }

    @CacheEvict(key = "#id")
    public void cacheEvict(String id) {
        System.out.println("cacheEvict id = " + id);
    }

//    @CacheEvict(cacheNames = "other", allEntries = true)
    @CacheEvict(cacheNames = "book", allEntries = true)
    public void cacheClear() {
        System.out.println("cacheClear allEntries");
    }

//    private RedisCacheManager redisCacheManager;
//
//    @Autowired(required = false)
//    private void getRedisCacheManager(RedisCacheManager redisCacheManager) {
//        this.redisCacheManager = redisCacheManager;
//    }
//
//    public void cacheClear(String cacheName) {
//        Cache cache = redisCacheManager.getCache(
//                cacheName != null && !cacheName.equals("") ? cacheName : DEFAULT_CACHE_NAME);
//        if (cache != null) {
//            cache.clear();
//        }
//    }
}
