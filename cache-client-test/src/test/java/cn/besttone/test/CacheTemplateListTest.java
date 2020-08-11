package cn.besttone.test;

import cn.besttone.cachetemplate.autoconfigure.service.CacheTemplate;
import cn.besttone.test.bean.Role;
import cn.besttone.test.bean.User;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.List;

/**
 * CacheTemplate list 测试
 *
 * @Author: ZJY
 */
@SpringBootTest
@Slf4j
public class CacheTemplateListTest {
    @Resource
    private CacheTemplate cacheTemplate;

    @Test
    void listRange() throws Exception {
        List<String> list = cacheTemplate.listRange("list", 2, 3);
        System.out.println(list);
        List<Object> list1 = cacheTemplate.listRange("list", 1, -3, Object.class);
        System.out.println(list1);
    }

    @Test
    void listTrim() throws Exception {
        cacheTemplate.listTrim("list", 2, -2);
    }

    @Test
    void listSize() throws Exception {
        System.out.println(cacheTemplate.listSize("list"));
    }

    @Test
    void listLeftPush() throws Exception {
        Role role = new Role();
        role.setRoleName("admin");
        cacheTemplate.listLeftPush("list", "asdfgh");
        cacheTemplate.listLeftPush("list", role);
    }

    @Test
    void listLeftPushAll() throws Exception {
        Role role = new Role();
        role.setRoleName("admin");
        cacheTemplate.listLeftPushAll("list", 1, "2", 0.1234, role);
    }

    @Test
    void listRightPush() throws Exception {
        cacheTemplate.listRightPush("list", 123);
    }

    @Test
    void listRightPushAll() throws Exception {
        User user = new User();
        user.setPassword("0123");
        user.setUsername("hhh");
        cacheTemplate.listRightPushAll("list", user, 3, "dfg", 1.34567);
    }

    @Test
    void listSet() throws Exception {
        cacheTemplate.listSet("list", 3, "zxc");
        User user = new User();
        user.setPassword("0123");
        user.setUsername("hhh");
        cacheTemplate.listSet("list", 3, user);
    }

    @Test
    void listRemove() throws Exception {
        cacheTemplate.listRemove("list", 2, 1);
    }

    @Test
    void listIndex() throws Exception {
        String a = cacheTemplate.listIndex("list", 3);
        System.out.println(a);
        Role b = cacheTemplate.listIndex("list", 0, Role.class);
        System.out.println(b);
        Object c = cacheTemplate.listIndex("list", 6, Object.class);
        System.out.println(c);
    }

    @Test
    void listLeftPop() throws Exception {
        Role a = cacheTemplate.listLeftPop("list", Role.class);
        System.out.println(a);
        System.out.println(cacheTemplate.listLeftPop("list1"));
    }

    @Test
    void listRightPop() throws Exception {
        String a = cacheTemplate.listRightPop("list");
        System.out.println(a);
        System.out.println(cacheTemplate.listLeftPop("list1"));
    }

}

