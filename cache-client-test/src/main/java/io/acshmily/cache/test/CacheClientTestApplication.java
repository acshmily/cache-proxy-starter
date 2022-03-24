package io.acshmily.cache.test;

import io.acshmily.cachetemplate.autoconfigure.announce.EnableCacheTemplate;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Author: Huanghz
 * Description: 启动测试类
 * Date:Created in 2:41 下午 2020/6/5
 * ModifyBy:
 **/
@SpringBootApplication
@EnableCacheTemplate
public class CacheClientTestApplication {
    public static void main(String[] args) {
        SpringApplication.run(CacheClientTestApplication.class, args);
    }

}
