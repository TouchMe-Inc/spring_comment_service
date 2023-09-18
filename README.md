# About spring_comment_service
This repository contains the comments microservice.


## Run the application
To run the application, run the following command in a terminal window directory:
`./gradlew bootRun`

## Cache
There are three types of caching implemented in the service:
- lfu
- lru
- redis

### Configuration example for lru cache:
```yaml
cache:
  type: lru
  capacity: 10
```

### Configuration example for lfu cache:
```yaml
cache:
  type: lfu
  capacity: 10
```

### Configuration example for redis cache:
```yaml
cache:
  type: redis

spring:
  data:
    redis:
      host: localhost
      port: 6379
```

## Docs
To generate documentation, you need to run the following commands:
- `./gradlew javadoc` - To generate JavaDoc documentation.
- `./gradlew asciidoctor` - To generate AsciiDoc documentation.
