package cn.besttone.test;

import cn.besttone.cachetemplate.autoconfigure.service.CacheTemplate;
import cn.besttone.test.bean.Role;
import cn.besttone.test.bean.User;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * CacheTemplate hash 测试
 *
 * @Author: ZJY
 */
@SpringBootTest
@Slf4j
public class CacheTemplateHashTest {
    @Resource
    private CacheTemplate cacheTemplate;

    @Test
    void deleteHashKeys() throws Exception {
        cacheTemplate.deleteHashKeys("hash", "2", "3");
    }

    @Test
    void hasHashKey() throws Exception {
        System.out.println(cacheTemplate.hasHashKey("hash", "6"));
    }

    @Test
    void hashGet() throws Exception {
        System.out.println(cacheTemplate.hashGet("hash", "40"));
        System.out.println(cacheTemplate.hashGet("hash", "40", User.class));
    }

    @Test
    void hashMultiGet() throws Exception {
        List<Object> list = cacheTemplate.hashMultiGet("hash", Object.class, "6", "40");
        list.stream().forEach(System.out::println);
        List<Object> list1 = cacheTemplate.hashMultiGet("hash", "1", "2", "user");
        list1.stream().forEach(System.out::println);
    }

    @Test
    void hashIncrement() throws Exception {
        System.out.println(cacheTemplate.hashIncrement("hash", "5", 2));
    }

    @Test
    void hashSize() throws Exception {
        System.out.println(cacheTemplate.hashSize("hash"));
    }

    @Test
    void hashPut() throws Exception {
        Role role = new Role();
        role.setRoleName("admin");
        cacheTemplate.hashPut("hash", "role", role);
    }

    @Test
    void hashPutAll() throws Exception {
        Role role = new Role();
        role.setRoleName("admin");
        User user = new User();
        user.setPassword("0123");
        user.setUsername("hhh");
        Map<String, Object> map = new LinkedHashMap() {
            {
                put("role", role);
                put("user", user);
                put("1", 1);
                put("2", 2.3456d);
                put("3", 0.1234f);


            }

        };
        cacheTemplate.hashPutAll("hash", map);
    }

    @Test
    void hashEntries() throws Exception {
        Map<String, Object> map = cacheTemplate.hashEntries("hash1", Object.class);
        User user = cacheTemplate.convertObject(map.get("user"), User.class);
        Role role = cacheTemplate.convertObject(map.get("role"), Role.class);
    }
}

