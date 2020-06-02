package config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @Author: Huanghz
 * @Description: 客户端配置项
 * @Date:Created in 10:18 上午 2020/6/2
 * @ModifyBy:
 **/
@Data
@ConfigurationProperties(prefix = "haobai.cache")
public class CacheClientProperties {
    /**
     * Cache Server Url
     */
    private String url = "http://127.0.0.1:8080";
    /**
     * Cache Server username
     */
    private String userName;
    /**
     * Cache Server password
     */
    private String password;




}
