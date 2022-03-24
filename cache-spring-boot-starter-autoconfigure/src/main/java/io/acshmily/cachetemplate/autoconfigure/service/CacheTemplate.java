package io.acshmily.cachetemplate.autoconfigure.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import okhttp3.MediaType;
import org.springframework.boot.json.JsonParseException;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * Author: Huanghz
 * Description: 缓存抽象接口
 * Date:Created in 3:24 下午 2020/6/2
 * ModifyBy:
 **/
public interface CacheTemplate {
    MediaType JSON_TYPE = MediaType.parse("application/json");


    /**
     * 指定键过期
     *
     * @param key
     * @param timeout
     * @param timeUnit
     */
    void expire(String key, long timeout, TimeUnit timeUnit);

    /**
     * 指定键在某个时间戳过期
     *
     * @param key
     * @param timestamp
     */
    void expireAt(String key, long timestamp);

    /**
     * 获取过期时间
     *
     * @param key
     * @param timeUnit 时间(秒) 返回0代表为永久有效
     * @return
     */
    Long getExpire(String key, TimeUnit timeUnit);

    /**
     * 根据key删除缓存
     *
     * @param keys
     */
    void delete(String... keys);

    /**
     * 判断key是否存在
     *
     * @param key
     * @return
     */
    Boolean hasKey(String key);

    /**
     * 删除指定key里的item
     *
     * @param key
     * @param hashKeys
     */
    void deleteHashKeys(String key, String... hashKeys);

    /**
     * 判断key内是否存在指定hashKey
     *
     * @param key
     * @param hashKey
     * @return
     */
    Boolean hasHashKey(String key, String hashKey);

    /**
     * 根据keykey及hashKey获取值并反序列化返回
     *
     * @param key
     * @param hashKey
     * @param valueType
     * @return
     * @throws IOException
     * @throws JsonParseException
     */
    <T> T hashGet(String key, String hashKey, Class<T> valueType) throws IOException, JsonParseException;

    /**
     * 获取key及hashKey获取值并直接返回
     *
     * @param key
     * @param hashKey
     * @return
     */
    String hashGet(String key, String hashKey);

    /**
     * 根据key及hashKeys获取值返回并且反序列化
     *
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
     *
     * @param key
     * @param hashKeys
     * @return
     */
    List<Object> hashMultiGet(String key, String... hashKeys) throws JsonProcessingException;

    /**
     * 对hash值进行加法运算
     *
     * @param key
     * @param hashKey
     * @param delta
     * @return
     */
    Long hashIncrement(String key, String hashKey, long delta);

    /**
     * 统计指定key的hash空间大小
     *
     * @param key
     * @return
     */
    Long hashSize(String key);

    /**
     * 对指定key的指定hashKey进行设置
     *
     * @param key
     * @param hashKey
     * @param value
     */
    void hashPut(String key, String hashKey, Object value) throws JsonProcessingException;

    /**
     * 对指定key进行批量操作
     *
     * @param key
     * @param map
     */
    void hashPutAll(String key, Map<String, Object> map) throws JsonProcessingException;

    /**
     * 根据Key获取所有Hash值并序列化返回
     *
     * @param key
     * @param valueType
     * @return
     */
    <T> Map<String, T> hashEntries(String key, Class<T> valueType) throws JsonProcessingException;

    /**
     * 根据key获取所有Hash值
     *
     * @param key
     * @return
     */
    Map<String, String> hashEntries(String key) throws JsonProcessingException;

    /**
     * 获取list内范围值，并反序列化返回
     *
     * @param key
     * @param start
     * @param end
     * @param valueType
     * @return
     */
    <T> List<T> listRange(String key, long start, long end, Class<T> valueType) throws JsonProcessingException;

    /**
     * 获取list内范围值，并返回
     *
     * @param key
     * @param start
     * @param end
     * @return
     */
    List<String> listRange(String key, long start, long end) throws JsonProcessingException;

    /**
     * 删除list首尾，只保留 [start, end] 之间的值
     *
     * @param key
     * @param start
     * @param end
     */
    void listTrim(String key, long start, long end);

    /**
     * 获取指定key的大小
     *
     * @param key
     * @return
     */
    Long listSize(String key);

    /**
     * 向左push
     *
     * @param key
     * @param object
     */
    void listLeftPush(String key, Object object) throws JsonProcessingException;

    /**
     * 向左批量push
     *
     * @param key
     * @param objects
     */
    void listLeftPushAll(String key, Object... objects) throws JsonProcessingException;

    /**
     * 向右push
     *
     * @param key
     * @param object
     */
    void listRightPush(String key, Object object) throws JsonProcessingException;

    /**
     * 批量向右push
     *
     * @param key
     * @param objects
     */
    void listRightPushAll(String key, Object... objects) throws JsonProcessingException;

    /**
     * 指定插入在指定索引
     *
     * @param key
     * @param index
     * @param value
     */
    void listSet(String key, long index, Object value) throws JsonProcessingException;

    /**
     * 删除list上的指定索引
     *
     * @param key
     * @param count
     * @param value
     */
    void listRemove(String key, long count, Object value);

    /**
     * 获取指定key的指定索引的值
     *
     * @param key
     * @param index
     * @return
     */
    String listIndex(String key, long index);

    /**
     * 获取指定key的指定索引的值反序列化返回
     *
     * @param key
     * @param index
     * @param valueType
     * @param <T>
     * @return
     */
    <T> T listIndex(String key, long index, Class<T> valueType) throws IOException, JsonParseException;

    /**
     * 指定key进行左Pop操作
     *
     * @param key
     * @return
     */
    String listLeftPop(String key);

    /**
     * 指定key进行左pop操作并反序列化返回
     *
     * @param key
     * @param valueType
     * @param <T>
     * @return
     */
    <T> T listLeftPop(String key, Class<T> valueType) throws IOException, JsonParseException;

    /**
     * 指定key进行右pop操作
     *
     * @param key
     * @return
     */
    String listRightPop(String key);

    /**
     * 指定key进行右Pop操作
     *
     * @param key
     * @param valueType
     * @param <T>
     * @return
     * @throws IOException
     * @throws JsonParseException
     */
    <T> T listRightPop(String key, Class<T> valueType) throws IOException, JsonParseException;

    /**
     * Add given {@code objects} to set at {@code key}.
     *
     * @param key
     * @param objects
     * @return
     */
    Long setAdd(String key, Object... objects) throws JsonProcessingException;

    /**
     * Remove given {@code objects} from set at {@code key} and return the number of removed elements.
     *
     * @param key
     * @param objects
     * @return
     */
    Long setRemove(String key, Object... objects) throws JsonProcessingException;

    /**
     * Get size of set at {@code key}.
     *
     * @param key
     * @return
     */
    Long setSize(String key);

    /**
     * Check if set at {@code key} contains {@code value}.
     *
     * @param key
     * @param o
     * @return
     */
    Boolean setIsMember(String key, Object o) throws JsonProcessingException;

    /**
     * 根据key获取指定Set，并序列化返回
     *
     * @param key
     * @param valueType
     * @return
     */
    <T> Set<T> setMember(String key, Class<T> valueType);

    /**
     * 根据key获取指定Set
     *
     * @param key
     * @return
     */
    Set<String> setMember(String key);

    /**
     * Add {@code value} to a sorted set at {@code key}, or update its {@code score} if it already exists.
     *
     * @param key
     * @param value
     * @param score
     * @return
     */
    Boolean zSetAddOne(String key, Object value, double score) throws JsonProcessingException;

    /**
     * Add {@code tuples} to a sorted set at {@code key}, or update its {@code score} if it already exists.
     *
     * @param key
     * @param tuples
     * @return
     */
    Long zSetAdd(String key, Map<String, String> tuples) throws JsonProcessingException;

    /**
     * Remove {@code values} from sorted set. Return number of removed elements.
     *
     * @param key
     * @param objects
     * @return
     */
    Long zSetRemove(String key, Object... objects) throws JsonProcessingException;

    /**
     * Increment the score of element with {@code value} in sorted set by {@code increment}.
     *
     * @param key
     * @param value
     * @param delta
     * @return
     */
    Double zSetIncrementScore(String key, Object value, double delta) throws JsonProcessingException;

    /**
     * Determine the index of element with {@code value} in a sorted set.
     *
     * @param key
     * @param object
     * @return
     */
    Long zSetRank(String key, Object object) throws JsonProcessingException;

    /**
     * Determine the index of element with {@code value} in a sorted set when scored high to low.
     *
     * @param key
     * @param object
     * @return
     */
    Long zSetReverseRank(String key, Object object) throws JsonProcessingException;

    /**
     * Get set of  between {@code start} and {@code end} from sorted set.
     *
     * @param key
     * @param start
     * @param end
     * @param valueType
     * @return
     */
    <T> Map<T, Double> zSetRangeWithScores(String key, long start, long end, Class<T> valueType);

    /**
     * Get set of  between {@code start} and {@code end} from sorted set.
     *
     * @param key
     * @param start
     * @param end
     * @param valueType
     * @return
     */
    <T> Map<T, Double> zSetReverseRangeWithScores(String key, long start, long end, Class<T> valueType);

    /**
     * Get set of  in range from {@code start} to {@code end} where score is between {@code min} and
     *
     * @param key
     * @param min
     * @param max
     * @param offset
     * @param count
     * @param valueType
     * @return
     */
    <T> Map<T, Double> zSetRangeByScoreWithScores(String key, double min, double max, long offset, long count, Class<T> valueType);

    /**
     * Get set of in range from {@code start} to {@code end} where score is between {@code min} and
     * {@code max} from sorted set ordered high -> low.
     *
     * @param key
     * @param min
     * @param max
     * @param offset
     * @param count
     * @param valueType
     * @return
     */
    <T> Map<T, Double> zSetReverseRangeByScoreWithScores(String key, double min, double max, long offset, long count, Class<T> valueType);

    /**
     * Count number of elements within sorted set with scores between {@code min} and {@code max}.
     *
     * @param key
     * @param min
     * @param max
     * @return
     */
    Long zSetCount(String key, double min, double max);

    /**
     * Get the size of sorted set with {@code key}.
     *
     * @param key
     * @return
     */
    Long zSetCard(String key);

    /**
     * Get the score of element with {@code value} from sorted set with key {@code key}.
     *
     * @param key
     * @param object
     * @return
     */
    Double zSetScore(String key, Object object) throws JsonProcessingException;

    /**
     * Remove elements in range between {@code start} and {@code end} from sorted set with {@code key}.
     *
     * @param key
     * @param start
     * @param end
     * @return
     */
    Long zSetRemoveRange(String key, long start, long end);

    /**
     * Remove elements with scores between {@code min} and {@code max} from sorted set with {@code key}.
     *
     * @param key
     * @param min
     * @param max
     * @return
     */
    Long zSetRemoveByScore(String key, double min, double max);

    /**
     * Set {@code value} for {@code key}.
     *
     * @param key
     * @param value
     */
    void stringSet(String key, String value);

    /**
     * Set {@code value} for {@code key}.
     *
     * @param key
     * @param value
     */
    void stringSet(String key, Object value) throws JsonProcessingException;

    /**
     * Set the {@code value} and expiration {@code timeout} for {@code key}.
     *
     * @param key
     * @param value
     * @param timeout
     * @param unit
     */
    void stringSet(String key, String value, long timeout, TimeUnit unit);

    /**
     * Set the {@code value} and expiration {@code timeout} for {@code key}.
     *
     * @param key
     * @param value
     * @param timeout
     * @param unit
     */
    void stringSet(String key, Object value, long timeout, TimeUnit unit) throws JsonProcessingException;

    /**
     * Set {@code key} to hold the string {@code value} if {@code key} is absent.
     *
     * @param key
     * @param value
     * @return
     */
    Boolean stringSetIfAbsent(String key, String value);

    /**
     * Set {@code key} to hold the string {@code value} if {@code key} is absent.
     *
     * @param key
     * @param value
     * @return
     */
    Boolean stringSetIfAbsent(String key, Object value) throws JsonProcessingException;

    /**
     * Set {@code key} to hold the string {@code value} and expiration {@code timeout} if {@code key} is absent.
     *
     * @param key
     * @param value
     * @param timeout
     * @param unit
     * @return
     */
    Boolean stringSetIfAbsent(String key, String value, long timeout, TimeUnit unit);

    /**
     * Set {@code key} to hold the string {@code value} and expiration {@code timeout} if {@code key} is absent.
     *
     * @param key
     * @param value
     * @param timeout
     * @param unit
     * @return
     */
    Boolean stringSetIfAbsent(String key, Object value, long timeout, TimeUnit unit) throws JsonProcessingException;

    /**
     * Set multiple keys to multiple values using key-value pairs provided in {@code tuple}.
     *
     * @param map
     */
    void stringMultiSet(Map<String, Object> map) throws JsonProcessingException;

    /**
     * Set multiple keys to multiple values using key-value pairs provided in {@code tuple} only if the provided key does
     * not exist.
     *
     * @param map
     * @return
     */
    Boolean stringMultiSetIfAbsent(Map<String, Object> map) throws JsonProcessingException;


    /**
     * 默认返回String值
     *
     * @param key
     * @return
     */
    String stringGet(String key);

    /**
     * @param key
     * @param valueType
     * @param <T>
     */
    <T> T stringGet(String key, Class<T> valueType) throws JsonProcessingException;

    /**
     * Get multiple {@code keys}. Values are returned in the order of the requested keys.
     *
     * @param keys
     * @return
     */
    List<String> stringMultiGet(String... keys);

    /**
     * Increment an integer value stored as string value under {@code key} by {@code delta}.
     *
     * @param key
     * @param delta
     * @return
     */
    Long stringIncrement(String key, long delta);

    /**
     * Decrement an integer value stored as string value under {@code key} by {@code delta}.
     *
     * @param key
     * @param delta
     * @return
     */
    Long stringDecrement(String key, long delta);

    /**
     * 转换类型
     *
     * @param object
     * @param valueType
     * @param <T>
     * @return
     */
    <T> T convertObject(Object object, Class<T> valueType) throws JsonProcessingException;

    /**
     * 对象转String
     *
     * @param object
     * @return
     */
    String convertString(Object object) throws JsonProcessingException;

}



