package com.ttn.amqp.ehcache3;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
class CacheListener {
    private static final Logger logger = LoggerFactory.getLogger(CacheListener.class);

    @RabbitListener(queues = Constants.ANONYMOUS_QUEUE)
    public void receiveMessage(CacheEventHolder<String, String> cacheEventHolder) {
        logger.info("Received: " + cacheEventHolder.getEventType() + " <" + cacheEventHolder + ">");
    }
}
