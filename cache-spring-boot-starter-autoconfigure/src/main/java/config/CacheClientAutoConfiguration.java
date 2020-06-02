package config;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @Author: Huanghz
 * @Description: 自动注册配置类
 * @Date:Created in 10:56 上午 2020/6/2
 * @ModifyBy:
 **/
@Configuration
@EnableConfigurationProperties(CacheClientProperties.class)
public class CacheClientAutoConfiguration {
    // todo 初始化提供外部使用
}
