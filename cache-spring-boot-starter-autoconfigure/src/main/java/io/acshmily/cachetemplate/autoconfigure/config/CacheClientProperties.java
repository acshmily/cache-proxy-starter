package io.acshmily.cachetemplate.autoconfigure.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.concurrent.TimeUnit;

/**
 * Author: Huanghz
 * Description: 客户端配置项
 * Date:Created in 10:18 上午 2020/6/2
 * ModifyBy:
 **/
@Data
@ConfigurationProperties(prefix = "acshmily.cache")
public class CacheClientProperties {
    /**
     * Cache Server Url
     */
    private String url = "";
    /**
     * Cache Server username
     */
    private String userName = "";
    /**
     * Cache Server password
     */
    private String password = "";
    /**
     * Max Idle Connections
     */
    private Integer maxIdleConnections = 30;
    /**
     * 最大复用数
     */
    private Long keepAliveDuration = 300L;
    /**
     * 复用时间单位
     */
    private TimeUnit keepAliveDurationTimeUnit = TimeUnit.MINUTES;
    /**
     * 连接超时时间
     */
    private Long connectTimeout = 10L;
    /**
     * 连接超时时间单位
     */
    private TimeUnit connectTimeoutTimeUnit = TimeUnit.SECONDS;
    /**
     * 写超时时间
     */
    private Long writeTimeout = 10L;
    /**
     * 写超时时间单位
     */
    private TimeUnit writeTimeoutTimeUnit = TimeUnit.SECONDS;
    /**
     * 读超时时间
     */
    private Long readTimeout = 10L;
    /**
     * 读超时时间单位
     */
    private TimeUnit readTimeoutTimeUnit = TimeUnit.SECONDS;
    /**
     * 线程配置
     */
    private int corePoolSize = Runtime.getRuntime().availableProcessors();
    /**
     * 最大线程数
     */
    private int maxPoolSize = Runtime.getRuntime().availableProcessors() * 4;
    /**
     * 等待任务完成后退出
     */
    private Boolean waitForTasksToCompleteOnShutdown = true;
    /**
     * 线程等待超时
     */
    private int awaitTerminationSeconds = 60;






}
