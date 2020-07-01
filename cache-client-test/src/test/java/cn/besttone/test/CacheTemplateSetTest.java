package cn.besttone.test;

import cn.besttone.cachetemplate.autoconfigure.service.CacheTemplate;
import cn.besttone.test.bean.Role;
import cn.besttone.test.bean.User;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.Set;

/**
 * CacheTemplate set 测试
 *
 * @Author: ZJY
 */
@SpringBootTest
@Slf4j
public class CacheTemplateSetTest {
    @Resource
    private CacheTemplate cacheTemplate;

    @Test
    void setAdd() throws Exception {
        User user = new User();
        user.setPassword("0123");
        user.setUsername("hhh");
        Role role = new Role();
        role.setRoleName("admin");
        role.setRole("admin");
        System.out.println(cacheTemplate.setAdd("set", 1, 2, "3", user, role));
    }

    @Test
    void setRemove() throws Exception {
        User user = new User();
        user.setPassword("0123");
        user.setUsername("hhh");
        System.out.println(cacheTemplate.setRemove("set", 2, user));
    }

    @Test
    void setSize() throws Exception {
        System.out.println(cacheTemplate.setSize("set"));
    }

    @Test
    void setIsMember() throws Exception {
        Role role = new Role();
        role.setRoleName("admin");
        role.setRole("admin1");
        System.out.println(cacheTemplate.setIsMember("set", "3"));
        System.out.println(cacheTemplate.setIsMember("set", role));
    }

    @Test
    void setMember() throws Exception {
        Set<String> set = cacheTemplate.setMember("set");
        System.out.println(set);
        Set<Object> set1 = cacheTemplate.setMember("set", Object.class);
        System.out.println(set1);
        System.out.println(cacheTemplate.setMember("set1"));
    }

}

