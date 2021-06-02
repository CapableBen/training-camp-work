#### 作业

如何解决多个 WebSecurityConfigurerAdapter Bean 配置相互冲突的问题？

提示：假设有两个 WebSecurityConfigurerAdapter Bean 定义，并且标注了不同的 @Order，其中一个关闭 CSRF，一个开启 CSRF，那么最终结果如何确定？

背景：Spring Boot 场景下，自动装配以及自定义 Starter 方式非常流行，部分开发人员掌握了 Spring Security 配置方法，并且自定义了自己的实现，解决了 Order 的问题，然而会出现不确定配置因素。



###### 启动程序后，访问 http://localhost:8080/ 可以看到下图，csrf处于开放状态，当存在多个httpSecurity配置对象时，通过实现PriorityOrdered接口来控制访问顺序，使得@Order 注解不能进行覆盖

![image-20210428195536558](.\image-20210428195536558.png)

###### 个人理解：

最核心在于以下代码，目的要控制 返回filterChainProxy对象的securityFilterChains

```java
	// org.springframework.security.config.annotation.web.builders.WebSecurity#performBuild
	@Override
	protected Filter performBuild() throws Exception {
		Assert.state(!this.securityFilterChainBuilders.isEmpty(),
				() -> "At least one SecurityBuilder<? extends SecurityFilterChain> needs to be specified. "
						+ "Typically this is done by exposing a SecurityFilterChain bean "
						+ "or by adding a @Configuration that extends WebSecurityConfigurerAdapter. "
						+ "More advanced users can invoke " + WebSecurity.class.getSimpleName()
						+ ".addSecurityFilterChainBuilder directly");
		int chainSize = this.ignoredRequests.size() + this.securityFilterChainBuilders.size();
		List<SecurityFilterChain> securityFilterChains = new ArrayList<>(chainSize);
		for (RequestMatcher ignoredRequest : this.ignoredRequests) {
            // 相当于 public void configure(WebSecurity web) 收集的 WebSecurity配置 进行组装
			securityFilterChains.add(new DefaultSecurityFilterChain(ignoredRequest));
		}
		for (SecurityBuilder<? extends SecurityFilterChain> securityFilterChainBuilder : this.securityFilterChainBuilders) {
            // securityFilterChainBuilder.build() 相当于把 httpSecurity 进行 
            // org.springframework.security.config.annotation.AbstractSecurityBuilder#build()
			securityFilterChains.add(securityFilterChainBuilder.build());
		}
        // 最终把上面两项收集到的 securityFilterChains 组装到 FilterChainProxy 返回 
        // 然后 FilterChainProxy 又被 DelegatingFilterProxy 委派
        // 最终目的返回 filterChainProxy
		FilterChainProxy filterChainProxy = new FilterChainProxy(securityFilterChains);
		if (this.httpFirewall != null) {
			filterChainProxy.setFirewall(this.httpFirewall);
		}
		if (this.requestRejectedHandler != null) {
			filterChainProxy.setRequestRejectedHandler(this.requestRejectedHandler);
		}
		filterChainProxy.afterPropertiesSet();

		Filter result = filterChainProxy;
		if (this.debugEnabled) {
			this.logger.warn("\n\n" + "********************************************************************\n"
					+ "**********        Security debugging is enabled.       *************\n"
					+ "**********    This may include sensitive information.  *************\n"
					+ "**********      Do not use in a production system!     *************\n"
					+ "********************************************************************\n\n");
			result = new DebugFilter(filterChainProxy);
		}
		this.postBuildAction.run();
		return result;
	}
```

