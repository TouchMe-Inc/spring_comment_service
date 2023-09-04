package by.touchme.commentservice.cache;

import org.springframework.cache.Cache;
import org.springframework.cache.support.SimpleValueWrapper;

import java.util.LinkedList;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class LRUCache implements Cache {
    private final String cacheName;
    private final int capacity;
    private final Map<Object, Object> data;
    private final LinkedList<Object> order;
    private final ReentrantReadWriteLock lock;

    public LRUCache(String cacheName, int capacity) {
        this.cacheName = cacheName;
        this.capacity = capacity;
        this.data = new ConcurrentHashMap<>(capacity);
        this.order = new LinkedList<>();
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

            order.remove(key);
            order.addFirst(key);

            return new SimpleValueWrapper(data.get(key));
        } finally {
            lock.readLock().unlock();
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
            if (data.containsKey(key)) {
                order.remove(key);
            } else if (data.size() >= capacity) {
                Object keyRemoved = order.removeLast();
                evict(keyRemoved);
            }

            data.put(key, value);
            order.addFirst(key);
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public void evict(Object key) {
        data.remove(key);
        order.remove(key);
    }

    @Override
    public void clear() {
        lock.writeLock().lock();

        try {
            data.clear();
            order.clear();
        } finally {
            lock.writeLock().unlock();
        }
    }
}