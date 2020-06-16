package cn.besttone.test;

import cn.besttone.cachetemplate.autoconfigure.service.CacheTemplate;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * CacheTemplate string 测试
 *
 * @Author: ZJY
 */
@SpringBootTest
@Slf4j
public class CacheTemplateStringTest {
    @Resource
    private CacheTemplate cacheTemplate;

    @Test
    void stringSet() throws Exception {
        cacheTemplate.stringSet("1", "1");
    }

    @Test
    void stringSetEx() throws Exception {
        cacheTemplate.stringSet("1", "1", 1, TimeUnit.HOURS);
    }

    @Test
    void stringSetIfAbsent() throws Exception {
        System.out.println(cacheTemplate.stringSetIfAbsent("1", "1"));
        System.out.println(cacheTemplate.stringSetIfAbsent("2", "2"));
    }

    @Test
    void stringSetIfAbsentEx() throws Exception {
        System.out.println(cacheTemplate.stringSetIfAbsent("2", "2", 1, TimeUnit.HOURS));
        System.out.println(cacheTemplate.stringSetIfAbsent("3", "3", 1, TimeUnit.HOURS));

    }

    @Test
    void stringMultiSetIfAbsent() throws Exception {
        Map<String, String> map = new HashMap<>();
        map.put("1", "1");
        map.put("2", "2");
        map.put("3", "3");
        map.put("4", "4");
        System.out.println(cacheTemplate.stringMultiSetIfAbsent(map));
    }

    @Test
    void stringGet() throws Exception {
        System.out.println(cacheTemplate.stringGet("1"));
    }

    @Test
    void stringMultiGet() throws Exception {
        cacheTemplate.stringMultiGet("1", "2", "3", "4").stream().forEach(System.out::println);
    }

    @Test
    void stringIncrement() throws Exception {
        System.out.println(cacheTemplate.stringIncrement("4", 1));
    }

    @Test
    void stringDecrement() throws Exception {
        System.out.println(cacheTemplate.stringDecrement("4", 1));
    }

}

