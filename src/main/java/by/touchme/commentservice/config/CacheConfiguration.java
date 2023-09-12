package by.touchme.commentservice.config;


import by.touchme.commentservice.cache.LFUCache;
import by.touchme.commentservice.cache.LRUCache;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.support.NoOpCacheManager;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.io.Serializable;
import java.util.Collections;


@EnableConfigurationProperties(CacheProperties.class)
@EnableCaching
@Configuration
public class CacheConfiguration {

    private final CacheProperties properties;

    CacheConfiguration(CacheProperties properties) {
        this.properties = properties;
    }

    @Bean
    public RedisTemplate<String, Serializable> redisCacheTemplate(LettuceConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, Serializable> template = new RedisTemplate<>();
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        template.setConnectionFactory(redisConnectionFactory);
        return template;
    }

    @Bean
    public CacheManager cacheManager(RedisConnectionFactory factory) {
        CacheTypes cacheTypes = properties.getType();

        switch (cacheTypes) {
            case LRU, LFU -> {
                SimpleCacheManager cacheManager = new SimpleCacheManager();
                
                cacheManager.setCaches(
                        Collections.singletonList(
                                createCache()
                        )
                );

                return cacheManager;
            }
            case REDIS -> {
                RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig();

                RedisCacheConfiguration redisCacheConfiguration = config
                        .serializeKeysWith(
                                RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
                        .serializeValuesWith(RedisSerializationContext.SerializationPair
                                .fromSerializer(new GenericJackson2JsonRedisSerializer()));

                return RedisCacheManager.builder(factory).cacheDefaults(redisCacheConfiguration).build();
            }
            default -> {
                return new NoOpCacheManager();
            }
        }
    }

    private Cache createCache() {
        int capacity = this.properties.getCapacity();
        CacheTypes cacheTypes = this.properties.getType();

        return switch (cacheTypes) {
            case LRU -> new LRUCache("comments", capacity);
            case LFU -> new LFUCache("comments", capacity);
            default -> null;
        };
    }
}
