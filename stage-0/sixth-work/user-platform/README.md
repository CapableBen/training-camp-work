1. 启动 redis

2. 进入my-cache项目

3. （需求1）
   
   3.1修改default-caching-provider.properties中
   
   javax.cache.CacheManager.mappings.redis=org.geektimes.cache.redis.JedisCacheManager
   
   3.2运行CachingTest测试类的testSampleRedis

4. （需求2）
   
   4.1修改default-caching-provider.properties中
   
   javax.cache.CacheManager.mappings.redis=org.geektimes.cache.redis.LettuceCacheManager
   
   4.2运行CachingTest测试类的testMyLettuce

