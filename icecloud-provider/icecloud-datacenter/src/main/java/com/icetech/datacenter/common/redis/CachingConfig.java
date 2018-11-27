package com.icetech.datacenter.common.redis;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.icetech.datacenter.common.config.RedisConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;

import java.lang.reflect.Method;

/**
 * 注解式环境管理
 */
@Configuration
@EnableCaching
public class CachingConfig extends CachingConfigurerSupport {
	@Autowired
	private RedisConfig redisConfig;
	     
	   /** 
	    * 生产key的策略 
	    *  
	    * @return 
	    */  
	 
	   @Bean
	   @Override  
	   public KeyGenerator keyGenerator() {
	       return new KeyGenerator() {
	           @Override  
	           public Object generate(Object target, Method method, Object... params) {  
	               StringBuilder sb = new StringBuilder();  
	               sb.append(target.getClass().getName());  
	               sb.append(method.getName());  
	               for (Object obj : params) {  
	                   sb.append(obj.toString());  
	               }  
	               return sb.toString();  
	           }  
	       };  
	   }  
	 
	   /** 
	    * 管理缓存 
	    *  
	    * @param redisTemplate 
	    * @return 
	    */  
	 
//	   @SuppressWarnings("rawtypes")
//	   @Bean
//	   public CacheManager CacheManager(RedisTemplate redisTemplate) {
//	       RedisCacheManager rcm = new RedisCacheManager(redisTemplate);
//	       // 设置cache过期时间,时间单位是秒
//	       rcm.setDefaultExpiration(60);
//	       Map<String, Long> map = new HashMap<String, Long>();
//	       map.put("test", 60L);
//	       rcm.setExpires(map);
//	       return rcm;
//	   }
	     
	   /** 
	    * redis 数据库连接池 
	    * @return 
	    */  
	 
	   @Bean
	   public JedisConnectionFactory redisConnectionFactory() {
	       JedisConnectionFactory factory = new JedisConnectionFactory();  
	       factory.setHostName(redisConfig.getHost());
	       factory.setPort(redisConfig.getPort());
	       factory.setPassword(redisConfig.getPassword());
	       factory.setTimeout(redisConfig.getTimeout()); // 设置连接超时时间
	       return factory;  
	   }  
	 
	   /** 
	    * redisTemplate配置 
	    *  
	    * @param factory 
	    * @return 
	    */  
	   @SuppressWarnings({ "rawtypes", "unchecked" })  
	   @Bean
	   public RedisTemplate<String, String> redisTemplate(RedisConnectionFactory factory) {
	       StringRedisTemplate template = new StringRedisTemplate(factory);
	       Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer(Object.class);
	       ObjectMapper om = new ObjectMapper();
	       om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
	       om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
	       jackson2JsonRedisSerializer.setObjectMapper(om);  
	       template.setValueSerializer(jackson2JsonRedisSerializer);  
	       template.afterPropertiesSet();  
	       return template;  
	   }  
}
