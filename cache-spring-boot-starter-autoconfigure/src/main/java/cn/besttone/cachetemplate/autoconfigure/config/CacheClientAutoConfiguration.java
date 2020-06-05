package cn.besttone.cachetemplate.autoconfigure.config;

import cn.besttone.cachetemplate.autoconfigure.service.CacheTemplate;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import cn.besttone.cachetemplate.autoconfigure.service.impl.CacheTemplateImpl;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.*;

/**
 * @Author: Huanghz
 * @Description: 自动注册配置类
 * @Date:Created in 10:56 上午 2020/6/2
 * @ModifyBy:
 **/
@Configuration
@EnableConfigurationProperties(CacheClientProperties.class)
@Slf4j
public class CacheClientAutoConfiguration {
    /**
     * 创建连接池
     * @return
     */
    @Bean("cache-client-Executor")
    public ExecutorService cacheExecutor(){
        log.info(">-----------开始初始化缓存插件线程池------------->");
        ExecutorService executorService = new ThreadPoolExecutor(
                cacheClientProperties.getCorePoolSize(),
                cacheClientProperties.getMaxPoolSize(),
                cacheClientProperties.getAwaitTerminationSeconds(),
                TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(),
                new ThreadPoolExecutor.CallerRunsPolicy()
        );
        log.info("<-----------初始化缓存插件线程池完成-------------<");
        return executorService;


    }

    /**
     * 创建OkHttp归属于Spring 管理
     * @return
     */
    @Bean("cache-client-httpclient")
    public OkHttpClient okHttpClient(){
        log.info(">-----------开始初始化缓存插件OkHttp------------>");
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(chain -> {
                    Request request = chain.request().newBuilder()
                            .addHeader("username",cacheClientProperties.getUserName())
                            .addHeader("password",cacheClientProperties.getPassword())
                            .addHeader("requestId", UUID.randomUUID().toString())
                            .build();

                    return chain.proceed(request);

                })
                .dispatcher(new Dispatcher(cacheExecutor()))
                // 自定义连接池大小
                .connectionPool(new ConnectionPool(
                        cacheClientProperties.getMaxIdleConnections(),
                        cacheClientProperties.getKeepAliveDuration(),
                        cacheClientProperties.getKeepAliveDurationTimeUnit()))
                .connectTimeout(cacheClientProperties.getConnectTimeout(),
                        cacheClientProperties.getConnectTimeoutTimeUnit())
                .writeTimeout(cacheClientProperties.getWriteTimeout(),
                        cacheClientProperties.getWriteTimeoutTimeUnit())
                .readTimeout(cacheClientProperties.getReadTimeout(),
                        cacheClientProperties.getReadTimeoutTimeUnit())
                .build();
        log.info("<-----------初始化缓存插件OkHttp结束-------------<");

        return client;
    }

    /**
     * 初始化缓存客户端实现类
     * @return
     */
    @Bean
    @ConditionalOnClass(ObjectMapper.class)
    public CacheTemplate cacheTemplate(){
        return new CacheTemplateImpl(okHttpClient(),cacheClientProperties.getUrl(),objectMapper);

    }
    @Bean
    @ConditionalOnMissingBean(ObjectMapper.class)
    public CacheTemplate cacheTemplateWithOutSpringMvc(){
        return new CacheTemplateImpl(okHttpClient(),cacheClientProperties.getUrl(),objectMapper());

    }
    @Bean
    @ConditionalOnMissingBean(ObjectMapper.class)
    public ObjectMapper objectMapper(){

        ObjectMapper objectMapper = new ObjectMapper();
        // 转换为格式化的json
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        // 如果json中有新增的字段并且是实体类类中不存在的，不报错
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return objectMapper;

    }





    @Resource
    private CacheClientProperties cacheClientProperties;
    @Resource
    private ObjectMapper objectMapper;
}
