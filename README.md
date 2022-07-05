# Use Guide

## maven
```xml
<dependency>
    <groupId>io.github.acshmily</groupId>
    <artifactId>cache-springboot-starter</artifactId>
    <version>1.3</version>
</dependency>


```

## set cache info,like application to append

```yaml
acshmily:
  cache:
    user-name: ai-test
    password: test
    url: http://127.0.0.1:8080
```

### Enable @EnableCacheTemplate
```java
@SpringBootApplication
@EnableCacheTemplate
public class CacheClientTestApplication {
    public static void main(String[] args) {
        SpringApplication.run(CacheClientTestApplication.class, args);
    }

}
```