package com.ttn.amqp.ehcache3;

import org.ehcache.CacheManager;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.CacheEventListenerConfigurationBuilder;
import org.ehcache.config.builders.CacheManagerBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;
import org.ehcache.event.CacheEventListener;
import org.ehcache.event.EventType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class Application {

    private static final Logger logger = LoggerFactory.getLogger(Application.class);
    private static final String exchangeName = "ehcache-exchange";

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private FanoutExchange fanout;

    @Bean
    public Queue autoDeleteQueue() {
        return new AnonymousQueue(new AnonymousQueue.Base64UrlNamingStrategy("ehcache-"));
    }

    @Bean
    public FanoutExchange fanout() {
        return new FanoutExchange(exchangeName);
    }

    @Bean
    public Binding binding(FanoutExchange fanout, Queue autoDeleteQueue) {
        return BindingBuilder.bind(autoDeleteQueue).to(fanout);
    }

    @Bean
    MessageListenerAdapter listenerAdapter(CacheListener cacheListener) {
        return new MessageListenerAdapter(cacheListener, "receiveMessage");
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    SimpleMessageListenerContainer container(
            ConnectionFactory connectionFactory,
            MessageListenerAdapter listenerAdapter,
            Queue autoDeleteQueue,
            MessageConverter jsonMessageConverter) {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.setMessageConverter(jsonMessageConverter);
        container.setQueues(autoDeleteQueue);
        container.setMessageListener(listenerAdapter);
        return container;
    }

    @Bean
    CacheManager cacheManager() {
        final String cacheName = "foo";
        CacheEventListenerConfigurationBuilder cacheEventListenerConfiguration = CacheEventListenerConfigurationBuilder
                .newEventListenerConfiguration(
                        (CacheEventListener<String, String>) event ->
                                rabbitTemplate.convertAndSend(
                                        fanout.getName(),
                                        "",
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

    public static void main(String[] args) throws InterruptedException {
        SpringApplication.run(Application.class, args);
    }
}



