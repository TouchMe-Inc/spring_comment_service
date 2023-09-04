package by.touchme.commentservice.config;

import lombok.Getter;

@Getter
public enum CacheTypes {

    NONE,
    LFU,
    LRU,
    REDIS,
}
