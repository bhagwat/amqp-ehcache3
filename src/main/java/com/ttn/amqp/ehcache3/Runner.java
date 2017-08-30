package com.ttn.amqp.ehcache3;

import org.ehcache.Cache;
import org.ehcache.CacheManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

@Component
class Runner implements CommandLineRunner {
    private static final Logger logger = LoggerFactory.getLogger(Runner.class);

    private final ConfigurableApplicationContext context;
    private final CacheManager cacheManager;

    public Runner(ConfigurableApplicationContext context,
                  CacheManager cacheManager) {
        this.context = context;
        this.cacheManager = cacheManager;
    }

    @Override
    public void run(String... args) throws Exception {
        logger.info("Sending message...");
        final Cache<String, String> cache = cacheManager.getCache("foo", String.class, String.class);
        logger.info("Putting message: 1");
        cache.put("Hello", "World");
        logger.info("Putting message: 2");
        cache.put("Hello", "Everyone");
        logger.info("Removing message: 1");
        cache.remove("Hello");
        logger.info("Sleeping");
        Thread.sleep(10000);
        context.close();
    }
}
