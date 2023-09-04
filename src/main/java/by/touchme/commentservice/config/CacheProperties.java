package by.touchme.commentservice.config;

import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@ConfigurationProperties(prefix = "cache")
@ConfigurationPropertiesScan
@Getter
public final class CacheProperties {

    private final CacheTypes type;
    private final int capacity;

    public CacheProperties(CacheTypes type, int capacity) {
        this.type = type;
        this.capacity = capacity;
    }
}
