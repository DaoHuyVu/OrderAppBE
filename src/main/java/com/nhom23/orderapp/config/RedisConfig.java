package com.nhom23.orderapp.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;

import java.time.Duration;

@Configuration
@EnableCaching
public class RedisConfig {
    @Value("${redis.port}")
    private Integer redisPort;
    @Value(("${redis.host}"))
    private String redisHost;
    @Bean
    public LettuceConnectionFactory connectionFactory(){
        RedisStandaloneConfiguration configuration = new RedisStandaloneConfiguration();
        configuration.setHostName(redisHost);
        configuration.setPort(redisPort);
        return new LettuceConnectionFactory();
    }
    private RedisCacheConfiguration redisCacheConfiguration(Duration duration){
        return RedisCacheConfiguration
                .defaultCacheConfig()
                .entryTtl(duration)
                .serializeValuesWith(
                        RedisSerializationContext
                                .SerializationPair
                                .fromSerializer(new GenericJackson2JsonRedisSerializer())
                );
    }
    @Bean
    public RedisCacheManager redisCacheManager(){
        return RedisCacheManager
                .builder(connectionFactory())
                .cacheDefaults(
                        redisCacheConfiguration(Duration.ofMinutes(10))
                                .disableCachingNullValues()
                )
                .withCacheConfiguration(
                        "menuItem",
                        redisCacheConfiguration(Duration.ofDays(1))
                )
                .withCacheConfiguration(
                        "menuItemDto",
                        redisCacheConfiguration(Duration.ofDays(1))
                )
                .build();
    }
}
