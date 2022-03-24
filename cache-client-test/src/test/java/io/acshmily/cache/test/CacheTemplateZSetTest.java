package io.acshmily.cache.test;

import io.acshmily.cachetemplate.autoconfigure.service.CacheTemplate;
import io.acshmily.cache.test.bean.Role;
import io.acshmily.cache.test.bean.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * CacheTemplate zset 测试
 *
 * Author: ZJY
 */
@SpringBootTest
@Slf4j
public class CacheTemplateZSetTest {
    @Resource
    private CacheTemplate cacheTemplate;

    @Test
    void zSetAddOne() throws IOException {
        ObjectMapper om = new ObjectMapper();
        User user = new User();
        user.setPassword("0123");
        user.setUsername("hhh");
        System.out.println(cacheTemplate.zSetAddOne("zset",user, 2));
    }

    @Test
    void zSetAdd() throws IOException {
        ObjectMapper om = new ObjectMapper();
        Map<String, String> map = new HashMap<>();
        User user = new User();
        user.setPassword("0123");
        user.setUsername("hhh");
        Role role = new Role();
        role.setRoleName("admin");
        role.setRole("admin1");
        map.put(om.writeValueAsString(user), "15");
        map.put(om.writeValueAsString(role), "12");
        map.put("7", "7");
        map.put("5", "5");
        map.put("25", "25");
        map.put("1234qwer", "12313");
        System.out.println(cacheTemplate.zSetAdd("zset", map));
    }

    @Test
    void zSetRemove() throws IOException {
        User user = new User();
        user.setPassword("0123");
        user.setUsername("hhh");
        System.out.println(cacheTemplate.zSetRemove("zset", "7", user));
    }

    @Test
    void zSetIncrementScore() throws IOException {
        Role role = new Role();
        role.setRoleName("admin");
        role.setRole("admin1");
        System.out.println(cacheTemplate.zSetIncrementScore("zset", role, 123.4d));
    }

    @Test
    void zSetRank() throws IOException {
        Role role = new Role();
        role.setRoleName("admin");
        role.setRole("admin1");
        System.out.println(cacheTemplate.zSetRank("zset", role));
        //System.out.println(cacheTemplate.zSetRank("zset", "5"));
    }

    @Test
    void zSetReverseRank() throws IOException {
        Role role = new Role();
        role.setRoleName("admin");
        role.setRole("admin1");
        System.out.println(cacheTemplate.zSetReverseRank("zset", role));
        System.out.println(cacheTemplate.zSetReverseRank("zset", "5"));
    }

    @Test
    void zSetRangeWithScores() throws IOException {
        Map<String, Double> map = cacheTemplate.zSetRangeWithScores("zset", 0, -1, String.class);
        map.entrySet().stream().forEach(entry -> System.out.println(entry.getKey() + "*****" + entry.getValue()));
    }

    @Test
    void zSetReverseRangeWithScores() throws IOException {
        Map<String, Double> map = cacheTemplate.zSetReverseRangeWithScores("zset", 0, -1, String.class);
        map.entrySet().stream().forEach(entry -> System.out.println(entry.getKey() + "*****" + entry.getValue()));
    }

    @Test
    void zSetRangeByScoreWithScores() throws IOException {
        Map<String, Double> map = cacheTemplate.zSetRangeByScoreWithScores("zset", 10, 20, 0, 10, String.class);
        map.entrySet().stream().forEach(entry -> System.out.println(entry.getKey() + "*****" + entry.getValue()));
    }

    @Test
    void zSetReverseRangeByScoreWithScores() throws IOException {
        Map<String, Double> map = cacheTemplate.zSetReverseRangeByScoreWithScores("zset", 10, 20, 0, 10, String.class);
        map.entrySet().stream().forEach(entry -> System.out.println(entry.getKey() + "*****" + entry.getValue()));
    }

    @Test
    void zSetCount() throws IOException {
        System.out.println(cacheTemplate.zSetCount("zset", 20.123d, 30.0123d));
    }

    @Test
    void zSetCard() throws IOException {
        System.out.println(cacheTemplate.zSetCard("zset"));
    }

    @Test
    void zSetScore() throws IOException {
        Role role = new Role();
        role.setRoleName("admin");
        role.setRole("admin1");
        System.out.println(cacheTemplate.zSetScore("zset", role));
    }

    @Test
    void zSetRemoveRange() throws IOException {
        System.out.println(cacheTemplate.zSetRemoveRange("zset", 2, 3));
    }

    @Test
    void zSetRemoveByScore() throws IOException {
        System.out.println(cacheTemplate.zSetRemoveByScore("zset", 20.123d, 34.567d));
    }

}

