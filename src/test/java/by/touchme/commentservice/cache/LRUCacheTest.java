package by.touchme.commentservice.cache;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.cache.Cache;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ActiveProfiles(profiles = "test")
@ExtendWith(SpringExtension.class)
public class LRUCacheTest {

    @DisplayName("JUnit test for LRUCache getCacheName")
    @Test
    void getCacheName() {
        Cache cache = new LFUCache("test", 3);
        Assertions.assertEquals("test", cache.getName());
    }

    @DisplayName("JUnit test for LRUCache getNonExistingCache")
    @Test
    void getNonExistingCache() {
        Cache cache = new LRUCache("test", 3);
        Assertions.assertNull(cache.get("1"));
    }

    @DisplayName("JUnit test for LRUCache getExistingCache")
    @Test
    void getExistingCache() {
        Cache cache = new LRUCache("test", 3);
        cache.put("key", "value");

        assertEqualsValueWrapper(cache.get("key"), "value");
    }

    @DisplayName("JUnit test for LRUCache updateCacheValue")
    @Test
    void updateCacheValue() {
        Cache cache = new LRUCache("test", 3);
        cache.put("key", "value");

        // Assert added key
        assertEqualsValueWrapper(cache.get("key"), "value");

        // Update value
        cache.put("key", "newValue");

        // Assert new key value
        assertEqualsValueWrapper(cache.get("key"), "newValue");
    }

    @DisplayName("JUnit test for LRUCache deleteNonRivalCache")
    @Test
    void deleteNonRivalCache() {
        Cache cache = new LRUCache("test", 3);
        cache.put("key1", "value1");
        cache.put("key2", "value2");
        cache.put("key3", "value3");

        // Assert added keys
        assertEqualsValueWrapper(cache.get("key1"), "value1");
        assertEqualsValueWrapper(cache.get("key2"), "value2");
        assertEqualsValueWrapper(cache.get("key3"), "value3");

        // Move keys to the top of the list
        cache.get("key1");
        cache.get("key2");

        // Add new key (and remove key3)
        cache.put("newKey", "newValue");

        // Assert result
        assertEqualsValueWrapper(cache.get("newKey"), "newValue");
        assertEqualsValueWrapper(cache.get("key1"), "value1");
        assertEqualsValueWrapper(cache.get("key2"), "value2");

        Assertions.assertNull(cache.get("key3"));
    }

    @DisplayName("JUnit test for LRUCache deleteCacheByKey")
    @Test
    void deleteCacheByKey() {
        Cache cache = new LRUCache("test", 3);
        cache.put("key", "value");

        // Assert added key
        assertEqualsValueWrapper(cache.get("key"), "value");

        // Remove key
        cache.evict("key");

        // Assert result
        Assertions.assertNull(cache.get("key"));
    }

    @DisplayName("JUnit test for LRUCache deleteAllCache")
    @Test
    void deleteAllCache() {
        Cache cache = new LRUCache("test", 3);
        cache.put("key1", "value1");
        cache.put("key2", "value2");

        // Assert added keys
        assertEqualsValueWrapper(cache.get("key1"), "value1");
        assertEqualsValueWrapper(cache.get("key2"), "value2");

        // Remove key
        cache.clear();

        // Assert result
        Assertions.assertNull(cache.get("key1"));
        Assertions.assertNull(cache.get("key2"));
    }

    void assertEqualsValueWrapper(Cache.ValueWrapper vw, Object value) {
        Assertions.assertNotNull(vw);
        Assertions.assertEquals(vw.get(), value);
    }
}
