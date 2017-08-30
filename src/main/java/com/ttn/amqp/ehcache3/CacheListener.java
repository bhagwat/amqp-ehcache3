package com.ttn.amqp.ehcache3;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
class CacheListener {
    private static final Logger logger = LoggerFactory.getLogger(CacheListener.class);

    public void receiveMessage(byte[] bytes) {
        logger.info("Received byte[] bytes <>");
    }

    public void receiveMessage(CacheEventHolder<String, String> cacheEventHolder) {
        logger.info("Received <" + cacheEventHolder + ">");
        logger.info("CacheName: " + cacheEventHolder.toString());
    }
}
