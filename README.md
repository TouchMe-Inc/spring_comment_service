# About spring_comment_service
This repository contains the comments microservice.

## Search [NEW!]
`GET /api/v1/comment/search`

Request Body:
```json
{
  "criteriaList": [
    {
      "key": "text",
      "operation": "CONTAINS",
      "value": "Sed"
    },
    {
      "key": "text",
      "operation": "CONTAINS",
      "value": "Commodo"
    }
  ]
}
```

Response Body:
```json
{
  "content": [
    {
      "id": 9,
      "newsId": 1,
      "username": "User_A",
      "text": "Commodo ullamcorper a lacus vestibulum sed arcu non",
      "time": "2023-09-04T07:02:01.172+00:00"
    }
  ],
  "metadata": {
    "number": 0,
    "size": 20,
    "totalElements": 1,
    "totalPages": 1
  }
}
```

## Cache
There are three types of caching implemented in the service:
- lfu
- lru
- redis

Configuration example for lru cache:
```yaml
cache:
  type: lru
  capacity: 10
```

Configuration example for lfu cache:
```yaml
cache:
  type: lfu
  capacity: 10
```

Configuration example for redis cache:
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
