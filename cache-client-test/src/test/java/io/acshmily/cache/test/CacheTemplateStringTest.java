package io.acshmily.cache.test;

import io.acshmily.cachetemplate.client.service.CacheTemplate;
import io.acshmily.cache.test.bean.User;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * CacheTemplate string 测试
 *
 * Author: ZJY
 */
@SpringBootTest
@Slf4j
public class CacheTemplateStringTest {
    @Resource
    private CacheTemplate cacheTemplate;

    @Test
    void stringSet() throws Exception {
        User user = new User();
        user.setPassword("0123");
        user.setUsername("hhh");
        cacheTemplate.stringSet("11111", user);
        cacheTemplate.stringSet("12", 1);
        cacheTemplate.stringSet("13", "13");
    }

    @Test
    void stringSetEx() throws Exception {
        User user = new User();
        user.setPassword("0123");
        user.setUsername("hhh");
        cacheTemplate.stringSet("1", "1", 1, TimeUnit.HOURS);
        cacheTemplate.stringSet("11111", user, 1, TimeUnit.HOURS);
    }

    @Test
    void stringSetIfAbsent() throws Exception {
        User user = new User();
        user.setPassword("0123");
        user.setUsername("hhh");
        System.out.println(cacheTemplate.stringSetIfAbsent("1", "1"));
        System.out.println(cacheTemplate.stringSetIfAbsent("2", "2"));
        System.out.println(cacheTemplate.stringSetIfAbsent("22222", user));
    }

    @Test
    void stringSetIfAbsentEx() throws Exception {
        User user = new User();
        user.setPassword("0123");
        user.setUsername("hhh");
        System.out.println(cacheTemplate.stringSetIfAbsent("2", "2", 1, TimeUnit.HOURS));
        System.out.println(cacheTemplate.stringSetIfAbsent("3", "3", 1, TimeUnit.HOURS));
        System.out.println(cacheTemplate.stringSetIfAbsent("33333", user, 1, TimeUnit.HOURS));

    }

    @Test
    void stringMultiSetIfAbsent() throws Exception {
        User user = new User();
        user.setPassword("0123");
        user.setUsername("hhh");
        Map<String, Object> map = new HashMap<>();
        map.put("1", false);
        map.put("2", 1.123d);
        map.put("3", "3");
        map.put("4", 4);
        map.put("5", user);
        System.out.println(cacheTemplate.stringMultiSetIfAbsent(map));
    }

    @Test
    void stringMultiSet() throws Exception {
        User user = new User();
        user.setPassword("0123");
        user.setUsername("hhh");
        Map<String, Object> map = new HashMap<>();
        map.put("1", false);
        map.put("2", 1.123d);
        map.put("3", "3");
        map.put("4", 4);
        map.put("5", user);
        cacheTemplate.stringMultiSet(map);
    }

    @Test
    void stringGet() throws Exception {
        System.out.println(cacheTemplate.stringGet("1").equals("1"));
        System.out.println(cacheTemplate.stringGet("5", User.class));
    }

    @Test
    void stringMultiGet() throws Exception {
       // cacheTemplate.stringMultiGet("1", "2", "3", "4");
        List<String> temp =  cacheTemplate.stringMultiGet("11", "22", "32", "41");
        System.out.println(temp);
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

