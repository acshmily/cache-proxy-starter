package cn.besttone.test;

import cn.besttone.cachetemplate.autoconfigure.service.CacheTemplate;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.concurrent.TimeUnit;

/**
 * CacheTemplate common 测试
 *
 * @Author: ZJY
 */
@SpringBootTest
@Slf4j
public class CacheTemplateCommonTest {
    @Resource
    private CacheTemplate cacheTemplate;

    @Test
    void expire() throws Exception {
        cacheTemplate.expire("1", 1, TimeUnit.MINUTES);
        System.out.println(cacheTemplate.getExpire("1", TimeUnit.SECONDS));
    }

    @Test
    void expireAt() throws Exception {
        cacheTemplate.expireAt("1", LocalDateTime.now().plusHours(1).toInstant(ZoneOffset.of("+8")).toEpochMilli());
    }

    @Test
    void getExpire() throws Exception {
        System.out.println(cacheTemplate.getExpire("1", TimeUnit.SECONDS));
        System.out.println(cacheTemplate.getExpire("1", TimeUnit.MINUTES));
    }

    @Test
    void delete() throws Exception {
        cacheTemplate.delete("2", "3");
    }

    @Test
    void hashKey() throws Exception {
        System.out.println(cacheTemplate.hasKey("4"));
    }
}

