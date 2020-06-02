package service;

import java.util.concurrent.TimeUnit;

/**
 * @Author: Huanghz
 * @Description: 缓存抽象接口
 * @Date:Created in 3:24 下午 2020/6/2
 * @ModifyBy:
 **/
public interface CacheTemplate {
    /**
     * 指定键过期
     * @param key
     * @param timeout
     * @param timeUnit
     */
    void expire(String key, long timeout, TimeUnit timeUnit);

    /**
     * 指定键在某个时间戳过期
     * @param key
     * @param timestamp
     */
    void expireAt(String key,long timestamp);

    /**
     * 获取过期时间
     * @param key
     * @param timeUnit 时间(秒) 返回0代表为永久有效
     * @return
     */
    Long getExpire(String key,TimeUnit timeUnit);

    /**
     * 根据key删除缓存
     * @param keys
     */
    void delete(String... keys);

    /**
     * 判断key是否存在
     * @param key
     * @return
     */
    Boolean hasKey(String key);

    /**
     * 删除指定key里的item
     * @param key
     * @param hashKeys
     */
    void deleteHashKeys(String key,String ...hashKeys);




}



