package cn.besttone.test;

import cn.besttone.cachetemplate.autoconfigure.service.CacheTemplate;
import cn.besttone.test.bean.User;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author: Huanghz
 * @Description: 测试类
 * @Date:Created in 9:45 上午 2020/6/9
 * @ModifyBy:
 **/
@SpringBootTest
@Slf4j
public class CacheTemplateTest {
    @Test
    void test() throws IOException {
        cacheTemplate.hashPut("test","1",new User("test","password"));
        User user = cacheTemplate.hashGet("test","1",User.class);
        log.info("user:{}",user);
        System.out.println(cacheTemplate.hashGet("test","1"));
    }
    @Test
    void test1() throws IOException {
        Map<String,Object> map = new HashMap<>();
        map.put("1",new User("test","password"));
        map.put("2",new User("test1","password"));
        cacheTemplate.hashPutAll("test3",map);
        //System.out.println(cacheTemplate.hashEntries("test2"));
        Map<String,User> result = cacheTemplate.hashEntries("test2",User.class);
        System.out.println(result.get("1"));
    }

    @Resource
    private CacheTemplate cacheTemplate;
}

