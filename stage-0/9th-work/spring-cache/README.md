#### 作业 Spring Cache 与 Redis 整合

- 如何清除某个 Spring Cache 所有的 Keys 关联的对象
  - 如果 Redis 中心化方案，Redis + Sentinel
  - 如果 Redis 去中心化方案，Redis Cluster（还没思考，希望小马哥能给点方案，有实现更加完美）
- 如何将 RedisCacheManager 与 @Cacheable 注解打通

###### 1.启动 redis

###### 2.启动spring-cache项目（spring boot项目）

###### 3.访问（可以自定义修改 cacheName 和 key 进行测试）

- http://localhost:8080/get?id=1

   获取cacheName为 book，key为1的缓存

- http://localhost:8080/put?id=1

   修改cacheName为 book，key为1的缓存
- http://localhost:8080/del?id=1

   删除cacheName为 book，key为1的缓存
- http://localhost:8080/clear

   清除cacheName为 book 的下面所有缓存

