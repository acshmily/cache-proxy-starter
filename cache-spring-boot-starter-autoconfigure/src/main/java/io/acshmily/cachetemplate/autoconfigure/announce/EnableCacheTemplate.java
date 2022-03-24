package io.acshmily.cachetemplate.autoconfigure.announce;

import io.acshmily.cachetemplate.autoconfigure.config.CacheClientAutoConfiguration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * Author: Huanghz
 * Description: 初始化注解
 * Date:Created in 2:24 下午 2020/6/5
 * ModifyBy:
 **/
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(CacheClientAutoConfiguration.class)
public @interface EnableCacheTemplate {
}
