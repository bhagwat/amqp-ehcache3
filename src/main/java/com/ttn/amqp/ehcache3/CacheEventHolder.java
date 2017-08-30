package com.ttn.amqp.ehcache3;

import org.ehcache.event.CacheEvent;
import org.ehcache.event.EventType;

import java.io.Serializable;

class CacheEventHolder<K, V> implements Serializable {
    private String cacheName;
    private K key;
    private V oldValue;
    private V newValue;
    private EventType eventType;

    public K getKey() {
        return key;
    }

    public void setKey(K key) {
        this.key = key;
    }

    public V getOldValue() {
        return oldValue;
    }

    public void setOldValue(V oldValue) {
        this.oldValue = oldValue;
    }

    public V getNewValue() {
        return newValue;
    }

    public void setNewValue(V newValue) {
        this.newValue = newValue;
    }

    public EventType getEventType() {
        return eventType;
    }

    public void setEventType(EventType eventType) {
        this.eventType = eventType;
    }

    public String getCacheName() {
        return cacheName;
    }

    public void setCacheName(String cacheName) {
        this.cacheName = cacheName;
    }

    CacheEventHolder(String cacheName, CacheEvent<? extends K, ? extends V> event) {
        this.cacheName = cacheName;
        this.key = event.getKey();
        this.oldValue = event.getOldValue();
        this.newValue = event.getNewValue();
        this.eventType = event.getType();
    }

    @Override
    public String toString() {
        return "{" +
                "cache:" + this.cacheName +
                ", Type: " + getEventType() +
                ", Key: " + getKey() +
                ", OldValue: " + getOldValue() +
                ", newValue: " + getNewValue() +
                "}";
    }
}
