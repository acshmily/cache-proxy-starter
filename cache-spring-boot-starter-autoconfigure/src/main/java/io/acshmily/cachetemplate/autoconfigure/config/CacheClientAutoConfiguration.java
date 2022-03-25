package io.acshmily.cachetemplate.autoconfigure.config;

import io.acshmily.cachetemplate.autoconfigure.service.CacheTemplate;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.acshmily.cachetemplate.autoconfigure.service.impl.CacheTemplateImpl;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;
import java.util.UUID;
import java.util.concurrent.*;

/**
 * Author: Huanghz
 * Description: 自动注册配置类
 * Date:Created in 10:56 上午 2020/6/2
 * ModifyBy:
 **/
@Configuration
@EnableConfigurationProperties(CacheClientProperties.class)
@Slf4j
public class CacheClientAutoConfiguration {
    /**
     * 创建连接池
     * @return 返回实例
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
     * 手动初始化对象
     * @param cacheClientProperties cacheClientProperties
     * @return 返回实例
     */
    public ExecutorService cacheExecutor(CacheClientProperties cacheClientProperties){
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
     * @return 返回实例
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
     * 手动初始化对象
     * @param cacheClientProperties 配置信息
     * @return 返回实例
     */
    public OkHttpClient okHttpClient(CacheClientProperties cacheClientProperties){
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
                .dispatcher(new Dispatcher(cacheExecutor(cacheClientProperties)))
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
     * @return 返回实例
     */
    @Bean
    public CacheTemplate cacheTemplate(){
        return new CacheTemplateImpl(okHttpClient(),cacheClientProperties.getUrl(),objectMapper());

    }

    /**
     * 手动初始化的时候需要
     * @param cacheClientProperties cacheClientProperties
     * @return  CacheTemplate
     */
    public CacheTemplate cacheTemplate(CacheClientProperties cacheClientProperties){
        return new CacheTemplateImpl(okHttpClient(cacheClientProperties),cacheClientProperties.getUrl(),objectMapper());

    }
    public ObjectMapper objectMapper(){

        ObjectMapper objectMapper = new ObjectMapper();
        // 转换为格式化的json
        //objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        // 如果json中有新增的字段并且是实体类类中不存在的，不报错
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return objectMapper;

    }





    @Resource
    private CacheClientProperties cacheClientProperties;

}
