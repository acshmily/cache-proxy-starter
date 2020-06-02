package service;

import org.springframework.boot.json.JsonParseException;

import java.io.IOException;
import java.util.List;
import java.util.Map;
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

    /**
     * 判断key内是否存在指定hashKey
     * @param key
     * @param hashKey
     * @return
     */
    Boolean hasHashKey(String key,String hashKey);

    /**
     * 按照key获取值并反序列化成实例返回
     * @param key
     * @param valueType
     * @return
     * @throws IOException
     * @throws JsonParseException
     */
    <T> T get(String key,Class<T> valueType) throws IOException, JsonParseException;

    /**
     * 默认返回String值
     * @param key
     * @return
     */
    String get(String key);
    /**
     * 根据keykey及hashKey获取值并反序列化返回
     * @param key
     * @param hashKey
     * @param valueType
     * @return
     * @throws IOException
     * @throws JsonParseException
     */
    <T> T hashGet(String key,String hashKey,Class<T> valueType) throws IOException, JsonParseException;

    /**
     * 获取key及hashKey获取值并直接返回
     * @param key
     * @param hashKey
     * @return
     */
    String hashGet(String key,String hashKey);

    /**
     * 根据key及hashKeys获取值返回并且反序列化
     * @param key
     * @param valueType
     * @param hashKeys
     * @return
     * @throws IOException
     * @throws JsonParseException
     */
    <T> List<T> hashMultiGet(String key, Class<T> valueType, String... hashKeys) throws IOException, JsonParseException;

    /**
     * 根据key及hashKeys返回数组
     * @param key
     * @param hashKeys
     * @return
     */
    List<String> hashMultiGet(String key,String ...hashKeys);

    /**
     * 对hash值进行加法运算
     * @param key
     * @param hashKey
     * @param delta
     * @return
     */
    Long hashIncrement(String key,String hashKey,long delta);

    /**
     * 统计指定key的hash空间大小
     * @param key
     * @return
     */
    Long hashSize(String key);

    /**
     * 对指定key的指定hashKey进行设置
     * @param key
     * @param hashKey
     * @param value
     */
    void hashPut(String key,String hashKey,Object value);

    /**
     * 对指定key进行批量操作
     * @param key
     * @param map
     */
    void hashPutAll(String key, Map<String,Object> map);

    /**
     * 根据Key获取所有Hash值并序列化返回
     * @param key
     * @param valueType
     * @return
     */
    <T> Map<String,T> hashEntries(String key,Class<T> valueType);

    /**
     * 根据key获取所有Hash值
     * @param key
     * @return
     */
    Map<String,String> hashEntries(String key);

    /**
     * 获取list内范围值，并反序列化返回
     * @param key
     * @param start
     * @param end
     * @param valueType
     * @return
     */
    <T> List<T> listRange(String key, long start, long end,Class<T> valueType);

    /**
     * 获取list内范围值，并返回
     * @param key
     * @param start
     * @param end
     * @return
     */
    List<String> listRange(String key,long start, long end);

    /**
     * 删除list首尾，只保留 [start, end] 之间的值
     * @param key
     * @param start
     * @param end
     */
    void listTrim(String key, long start, long end);

    /**
     * 获取指定key的大小
     * @param key
     * @return
     */
    Long listSize(String key);

    /**
     * 向左push
     * @param key
     * @param object
     */
    void listLeftPush(String key,Object object);

    /**
     * 向左批量push
     * @param key
     * @param objects
     */
    void listLeftPushAll(String key,Object ...objects);

    /**
     * 向右push
     * @param key
     * @param object
     */
    void listRightPush(String key,Object object);

    /**
     * 批量向右push
     * @param key
     * @param objects
     */
    void listRightPushAll(String key,Object ...objects);

    /**
     * 指定插入在指定索引
     * @param key
     * @param index
     * @param value
     */
    void listSet(String key,long index, Object value);

    /**
     * 删除list上的指定索引
     * @param key
     * @param index
     */
    void listRemove(String key,long index);

    /**
     * 获取指定key的指定索引的值
     * @param key
     * @param index
     * @return
     */
    String listIndex(String key,long index);

    /**
     * 获取指定key的指定索引的值反序列化返回
     * @param key
     * @param index
     * @param valueType
     * @param <T>
     * @return
     */
    <T> T listIndex(String key,long index,Class<T> valueType) throws IOException, JsonParseException;

    /**
     * 指定key进行左Pop操作
     * @param key
     * @return
     */
    String listLeftPop(String key);

    /**
     * 指定key进行左pop操作并反序列化返回
     * @param key
     * @param valueType
     * @param <T>
     * @return
     */
    <T> T listLeftPop(String key,Class<T> valueType)  throws IOException, JsonParseException;

    /**
     * 指定key进行右pop操作
     * @param key
     * @return
     */
    String listRightPop(String key);
    /**
     * 指定key进行右Pop操作
     * @param key
     * @param valueType
     * @param <T>
     * @return
     * @throws IOException
     * @throws JsonParseException
     */
    <T> T listRightPop(String key,Class<T> valueType)  throws IOException, JsonParseException;


}



