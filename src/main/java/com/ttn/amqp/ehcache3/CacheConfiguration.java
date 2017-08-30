package com.ttn.amqp.ehcache3;

import org.ehcache.CacheManager;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.CacheEventListenerConfigurationBuilder;
import org.ehcache.config.builders.CacheManagerBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;
import org.ehcache.event.CacheEventListener;
import org.ehcache.event.EventType;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CacheConfiguration {
    private final RabbitTemplate rabbitTemplate;

    @Autowired
    public CacheConfiguration(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Bean
    CacheManager cacheManager() {
        final String cacheName = "foo";
        CacheEventListenerConfigurationBuilder cacheEventListenerConfiguration = CacheEventListenerConfigurationBuilder
                .newEventListenerConfiguration(
                        (CacheEventListener<String, String>) event ->
                                rabbitTemplate.convertAndSend(
                                        Constants.EHCACHE_EXCHANGE,
                                        Constants.ROUTING_KEY,
                                        new CacheEventHolder<String, String>(cacheName, event)
                                ),
                        EventType.CREATED, EventType.UPDATED, EventType.EVICTED, EventType.REMOVED, EventType.EXPIRED)
                .unordered()
                .asynchronous();
        return CacheManagerBuilder
                .newCacheManagerBuilder()
                .withCache(
                        cacheName,
                        CacheConfigurationBuilder.newCacheConfigurationBuilder(
                                String.class,
                                String.class,
                                ResourcePoolsBuilder.heap(10)
                        ).add(cacheEventListenerConfiguration)
                ).build(true);
    }
}
