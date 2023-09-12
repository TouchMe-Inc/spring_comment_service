package by.touchme.commentservice.cache;

import org.springframework.cache.Cache;
import org.springframework.cache.support.SimpleValueWrapper;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class LFUCache implements Cache {
    private final String cacheName;
    private final int capacity;
    private final Map<Object, Object> data;
    private final Map<Object, Integer> frequency;
    private final ReentrantReadWriteLock lock;

    public LFUCache(String cacheName, int capacity) {
        this.cacheName = cacheName;
        this.capacity = capacity;
        this.data = new ConcurrentHashMap<>(capacity);
        this.frequency = new ConcurrentHashMap<>(capacity);
        this.lock = new ReentrantReadWriteLock();
    }

    @Override
    public String getName() {
        return cacheName;
    }

    @Override
    public Object getNativeCache() {
        return null;
    }

    @Override
    public ValueWrapper get(Object key) {
        lock.readLock().lock();

        try {
            if (!data.containsKey(key)) {
                return null;
            }

            frequency.put(key, frequency.get(key) + 1);

            return new SimpleValueWrapper(data.get(key));
        } finally {
            this.lock.readLock().unlock();
        }
    }

    @Override
    public <T> T get(Object key, Class<T> type) {
        return null;
    }

    @Override
    public <T> T get(Object key, Callable<T> valueLoader) {
        return null;
    }

    @Override
    public void put(Object key, Object value) {
        lock.writeLock().lock();

        try {
            int freq = 1;

            if (this.data.containsKey(key)) {
                freq += frequency.get(key);
            } else if (data.size() >= capacity) {
                // Add Tree for O(1) and order for remove only old record
                Map.Entry<Object, Integer> removedEntry = Collections.min(
                        frequency.entrySet(),
                        Map.Entry.comparingByValue()
                );
                evict(removedEntry.getKey());
            }

            data.put(key, value);
            frequency.put(key, freq);
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public void evict(Object key) {
        data.remove(key);
        frequency.remove(key);
    }

    @Override
    public void clear() {
        lock.writeLock().lock();

        try {
            data.clear();
            frequency.clear();
        } finally {
            lock.writeLock().unlock();
        }
    }
}