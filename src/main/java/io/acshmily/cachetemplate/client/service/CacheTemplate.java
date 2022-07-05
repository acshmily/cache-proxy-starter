package io.acshmily.cachetemplate.client.service;

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
     * @param key key
     * @param timeout timeout
     * @param timeUnit timeUnit
     */
    void expire(String key, long timeout, TimeUnit timeUnit);

    /**
     * 指定键在某个时间戳过期
     * @param key key
     * @param timestamp timestamp
     */
    void expireAt(String key, long timestamp);

    /**
     * 获取过期时间
     * @param key key
     * @param timeUnit timeUnit
     * @return
     */
    Long getExpire(String key, TimeUnit timeUnit);

    /**
     * 根据key删除缓存
     * @param keys keys
     */
    void delete(String... keys);

    /**
     * 判断key是否存在
     * @param key key
     * @return Boolean
     */
    Boolean hasKey(String key);

    /**
     * 删除指定key里的item
     * @param key key
     * @param hashKeys  hashKeys
     */
    void deleteHashKeys(String key, String... hashKeys);

    /**
     * 判断key内是否存在指定hashKey
     * @param key key
     * @param hashKey hashKey
     * @return Boolean
     */
    Boolean hasHashKey(String key, String hashKey);

    /**
     * 根据keykey及hashKey获取值并反序列化返回
     * @param key key
     * @param hashKey hashkey
     * @param valueType valueType
     * @param <T> T
     * @return valueType
     * @throws IOException IOException
     * @throws JsonParseException JsonParseException
     */
    <T> T hashGet(String key, String hashKey, Class<T> valueType) throws IOException, JsonParseException;

    /**
     * 获取key及hashKey获取值并直接返回
     * @param key key
     * @param hashKey hashKey
     * @return String
     */
    String hashGet(String key, String hashKey);

    /**
     * 根据key及hashKeys获取值返回并且反序列化
     * @param key key
     * @param valueType valueType
     * @param hashKeys hashKeys
     * @param <T> T
     * @return valueType
     * @throws IOException IOException
     * @throws JsonParseException JsonParseException
     */
    <T> List<T> hashMultiGet(String key, Class<T> valueType, String... hashKeys) throws IOException, JsonParseException;

    /**
     * 根据key及hashKeys返回数组
     * @param key key
     * @param hashKeys hashKeys
     * @return List
     * @throws JsonProcessingException JsonProcessingException
     */
    List<Object> hashMultiGet(String key, String... hashKeys) throws JsonProcessingException;

    /**
     * 对hash值进行加法运算
     * @param key key
     * @param hashKey hashKey
     * @param delta delta
     * @return Long
     */
    Long hashIncrement(String key, String hashKey, long delta);

    /**
     * 统计指定key的hash空间大小
     * @param key key
     * @return Long
     */
    Long hashSize(String key);

    /**
     * 对指定key的指定hashKey进行设置
     * @param key key
     * @param hashKey hashKey
     * @param value value
     * @throws JsonProcessingException JsonProcessingException
     */
    void hashPut(String key, String hashKey, Object value) throws JsonProcessingException;

    /**
     * 对指定key进行批量操作
     * @param key key
     * @param map map
     * @throws JsonProcessingException JsonProcessingException
     */
    void hashPutAll(String key, Map<String, Object> map) throws JsonProcessingException;

    /**
     * 根据Key获取所有Hash值并序列化返回
     * @param key key
     * @param valueType valueType
     * @param <T> T
     * @return  valueType
     * @throws JsonProcessingException JsonProcessingException
     */
    <T> Map<String, T> hashEntries(String key, Class<T> valueType) throws JsonProcessingException;

    /**
     * 根据key获取所有Hash值
     * @param key key
     * @return Map
     * @throws JsonProcessingException JsonProcessingException
     */
    Map<String, String> hashEntries(String key) throws JsonProcessingException;

    /**
     * 获取list内范围值，并反序列化返回
     * @param key key
     * @param start start
     * @param end end
     * @param valueType valueType
     * @param <T> T
     * @return valueType
     * @throws JsonProcessingException JsonProcessingException
     */
    <T> List<T> listRange(String key, long start, long end, Class<T> valueType) throws JsonProcessingException;

    /**
     * 获取list内范围值，并返回
     * @param key   key
     * @param start start
     * @param end   end
     * @return  List
     * @throws JsonProcessingException  JsonProcessingException
     */
    List<String> listRange(String key, long start, long end) throws JsonProcessingException;

    /**
     * 删除list首尾，只保留 [start, end] 之间的值
     * @param key   key
     * @param start start
     * @param end   end
     */
    void listTrim(String key, long start, long end);

    /**
     *
     * @param key   key
     * @return  Long
     */
    Long listSize(String key);

    /**
     * 向左push
     * @param key   key
     * @param object    object
     * @throws JsonProcessingException  JsonProcessingException
     */
    void listLeftPush(String key, Object object) throws JsonProcessingException;

    /**
     * 向左批量push
     * @param key   key
     * @param objects   objects
     * @throws JsonProcessingException  JsonProcessingException
     */
    void listLeftPushAll(String key, Object... objects) throws JsonProcessingException;

    /**
     * 向右push
     * @param key   key
     * @param object    object
     * @throws JsonProcessingException  JsonProcessingException
     */
    void listRightPush(String key, Object object) throws JsonProcessingException;

    /**
     * 批量向右push
     * @param key   key
     * @param objects   objects
     * @throws JsonProcessingException  JsonProcessingException
     */
    void listRightPushAll(String key, Object... objects) throws JsonProcessingException;

    /**
     * 指定插入在指定索引
     * @param key   key
     * @param index index
     * @param value value
     * @throws JsonProcessingException  JsonProcessingException
     */
    void listSet(String key, long index, Object value) throws JsonProcessingException;

    /**
     * 删除list上的指定索引
     * @param key   key
     * @param count count
     * @param value value
     */
    void listRemove(String key, long count, Object value);

    /**
     * 获取指定key的指定索引的值
     * @param key   key
     * @param index index
     * @return  String
     */
    String listIndex(String key, long index);

    /**
     * 获取指定key的指定索引的值反序列化返回
     * @param key   key
     * @param index index
     * @param valueType valueType
     * @param <T>   T
     * @return  valueType
     * @throws IOException  IOException
     * @throws JsonParseException   JsonParseException
     */
    <T> T listIndex(String key, long index, Class<T> valueType) throws IOException, JsonParseException;

    /**
     * 指定key进行左Pop操作
     * @param key   key
     * @return  String
     */
    String listLeftPop(String key);

    /**
     * 指定key进行左pop操作并反序列化返回
     * @param key   key
     * @param valueType valueType
     * @param <T>   T
     * @return  valueType
     * @throws IOException  IOException
     * @throws JsonParseException   JsonParseException
     */
    <T> T listLeftPop(String key, Class<T> valueType) throws IOException, JsonParseException;

    /**
     * 指定key进行右pop操作
     * @param key   key
     * @return  String
     */
    String listRightPop(String key);

    /**
     * 指定key进行右Pop操作
     * @param key   key
     * @param valueType valueType
     * @param <T>   T
     * @return  valueType
     * @throws IOException  IOException
     * @throws JsonParseException   JsonParseException
     */
    <T> T listRightPop(String key, Class<T> valueType) throws IOException, JsonParseException;

    /**
     * Add given {code objects} to set at {code key}.
     * @param key   key
     * @param objects   objects
     * @return  Long
     * @throws JsonProcessingException  JsonProcessingException
     */
    Long setAdd(String key, Object... objects) throws JsonProcessingException;

    /**
     * Remove given {code objects} from set at {code key} and return the number of removed elements.
     * @param key   key
     * @param objects   objects
     * @return  Long
     * @throws JsonProcessingException  JsonProcessingException
     */
    Long setRemove(String key, Object... objects) throws JsonProcessingException;

    /**
     * Get size of set at {code key}.
     * @param key   key
     * @return  Long
     */
    Long setSize(String key);

    /**
     * Check if set at {code key} contains {code value}.
     * @param key   key
     * @param o o
     * @return  Boolean
     * @throws JsonProcessingException  JsonProcessingException
     */
    Boolean setIsMember(String key, Object o) throws JsonProcessingException;

    /**
     * 根据key获取指定Set，并序列化返回
     * @param key   key
     * @param valueType valueType
     * @param <T>   T
     * @return  valueType
     */
    <T> Set<T> setMember(String key, Class<T> valueType);

    /**
     * 根据key获取指定Set
     * @param key   key
     * @return  Set
     */
    Set<String> setMember(String key);

    /**
     * 去重返回key里的set
     * @param key
     * @param count
     * @return
     */
    Set<String> distinctRandomMembers(String key,Long count);

    /**
     * 去重返回key里的set
     * @param key
     * @param count
     * @return
     * @param <T>
     */
    <T> Set<T> distinctRandomMembers(String key,Long count,Class<T> valueType);
    /**
     * Add {code value} to a sorted set at {code key}, or update its {code score} if it already exists.
     * @param key   key
     * @param value value
     * @param score score
     * @return  Boolean
     * @throws JsonProcessingException  JsonProcessingException
     */
    Boolean zSetAddOne(String key, Object value, double score) throws JsonProcessingException;

    /**
     * Add {code tuples} to a sorted set at {code key}, or update its {code score} if it already exists.
     * @param key   key
     * @param tuples    tuples
     * @return  Long
     * @throws JsonProcessingException  JsonProcessingException
     */
    Long zSetAdd(String key, Map<String, String> tuples) throws JsonProcessingException;

    /**
     * Remove {code values} from sorted set. Return number of removed elements.
     * @param key   key
     * @param objects   objects
     * @return  Long
     * @throws JsonProcessingException  JsonProcessingException
     */
    Long zSetRemove(String key, Object... objects) throws JsonProcessingException;

    /**
     * Increment the score of element with {code value} in sorted set by {code increment}.
     * @param key   key
     * @param value value
     * @param delta delta
     * @return  Double
     * @throws JsonProcessingException  JsonProcessingException
     */
    Double zSetIncrementScore(String key, Object value, double delta) throws JsonProcessingException;

    /**
     * Determine the index of element with {code value} in a sorted set.
     * @param key   key
     * @param object    object
     * @return  Long
     * @throws JsonProcessingException  JsonProcessingException
     */
    Long zSetRank(String key, Object object) throws JsonProcessingException;

    /**
     * Determine the index of element with {code value} in a sorted set when scored high to low.
     * @param key   key
     * @param object    object
     * @return  Long
     * @throws JsonProcessingException  JsonProcessingException
     */
    Long zSetReverseRank(String key, Object object) throws JsonProcessingException;

    /**
     * Get set of  between {code start} and {code end} from sorted set.
     * @param key   key
     * @param start start
     * @param end   end
     * @param valueType valueType
     * @param <T>   T
     * @return  valueType
     */
    <T> Map<T, Double> zSetRangeWithScores(String key, long start, long end, Class<T> valueType);

    /**
     * Get set of  between {code start} and {code end} from sorted set.
     * @param key   key
     * @param start start
     * @param end   end
     * @param valueType valueType
     * @param <T>   T
     * @return  Map
     */
    <T> Map<T, Double> zSetReverseRangeWithScores(String key, long start, long end, Class<T> valueType);

    /**
     * Get set of  in range from {code start} to {code end} where score is between {code min} and
     * @param key   key
     * @param min   min
     * @param max   max
     * @param offset    offset
     * @param count count
     * @param valueType valueType
     * @param <T>   T
     * @return  Map
     **/
    <T> Map<T, Double> zSetRangeByScoreWithScores(String key, double min, double max, long offset, long count, Class<T> valueType);

    /**
     *
     * @param key   key
     * @param min   min
     * @param max   max
     * @param offset    offset
     * @param count count
     * @param valueType valueType
     * @param <T>   T
     * @return  Map
     */
    <T> Map<T, Double> zSetReverseRangeByScoreWithScores(String key, double min, double max, long offset, long count, Class<T> valueType);

    /**
     * Count number of elements within sorted set with scores between {code min} and {code max}.
     * @param key   key
     * @param min   min
     * @param max   max
     * @return  Long
     */
    Long zSetCount(String key, double min, double max);

    /**
     * Get the size of sorted set with {code key}.
     * @param key   key
     * @return  Long
     */
    Long zSetCard(String key);

    /**
     * Get the score of element with {code value} from sorted set with key {code key}.
     * @param key   key
     * @param object    object
     * @return  Double
     * @throws JsonProcessingException  JsonProcessingException
     */
    Double zSetScore(String key, Object object) throws JsonProcessingException;

    /**
     * Remove elements in range between {code start} and {code end} from sorted set with {code key}.
     * @param key   key
     * @param start start
     * @param end   end
     * @return  Long
     */
    Long zSetRemoveRange(String key, long start, long end);

    /**
     * Remove elements with scores between {code min} and {code max} from sorted set with {code key}.
     * @param key   key
     * @param min   min
     * @param max   max
     * @return  Long
     */
    Long zSetRemoveByScore(String key, double min, double max);

    /**
     * Set {code value} for {code key}.
     * @param key   key
     * @param value value
     */
    void stringSet(String key, String value);

    /**
     * Set {code value} for {code key}.
     * @param key   key
     * @param value value
     * @throws JsonProcessingException  JsonProcessingException
     */
    void stringSet(String key, Object value) throws JsonProcessingException;

    /**
     * Set the {code value} and expiration {code timeout} for {code key}.
     * @param key   key
     * @param value value
     * @param timeout   timeout
     * @param unit  unit
     */
    void stringSet(String key, String value, long timeout, TimeUnit unit);

    /**
     * Set the {code value} and expiration {code timeout} for {code key}.
     * @param key   key
     * @param value value
     * @param timeout   timeout
     * @param unit  unit
     * @throws JsonProcessingException  JsonProcessingException
     */
    void stringSet(String key, Object value, long timeout, TimeUnit unit) throws JsonProcessingException;

    /**
     * Set {code key} to hold the string {code value} if {code key} is absent.
     * @param key   key
     * @param value value
     * @return  Boolean
     */
    Boolean stringSetIfAbsent(String key, String value);

    /**
     * Set {code key} to hold the string {code value} if {code key} is absent.
     * @param key   key
     * @param value value
     * @return  Boolean
     * @throws JsonProcessingException  JsonProcessingException
     */
    Boolean stringSetIfAbsent(String key, Object value) throws JsonProcessingException;

    /**
     * Set {code key} to hold the string {code value} and expiration {code timeout} if {code key} is absent.
     * @param key   key
     * @param value value
     * @param timeout   timeout
     * @param unit  unit
     * @return  Boolean
     */
    Boolean stringSetIfAbsent(String key, String value, long timeout, TimeUnit unit);

    /**
     * Set {code key} to hold the string {code value} and expiration {code timeout} if {code key} is absent.
     * @param key   key
     * @param value value
     * @param timeout   timeout
     * @param unit  unit
     * @return  Boolean
     * @throws JsonProcessingException  JsonProcessingException
     */
    Boolean stringSetIfAbsent(String key, Object value, long timeout, TimeUnit unit) throws JsonProcessingException;

    /**
     * Set multiple keys to multiple values using key-value pairs provided in {code tuple}.
     * @param map   map
     * @throws JsonProcessingException  JsonProcessingException
     */
    void stringMultiSet(Map<String, Object> map) throws JsonProcessingException;

    /**
     * Set multiple keys to multiple values using key-value pairs provided in {code tuple} only if the provided key does
     *  not exist.
     * @param map   map
     * @return  Boolean
     * @throws JsonProcessingException  JsonProcessingException
     */
    Boolean stringMultiSetIfAbsent(Map<String, Object> map) throws JsonProcessingException;


    /**
     * 默认返回String值
     * @param key   key
     * @return  String
     */
    String stringGet(String key);

    /**
     *
     * @param key   key
     * @param valueType valueType
     * @param <T>   T
     * @return  valueType
     * @throws JsonProcessingException  JsonProcessingException
     */
    <T> T stringGet(String key, Class<T> valueType) throws JsonProcessingException;

    /**
     * Get multiple {code keys}. Values are returned in the order of the requested keys.
     * @param keys  key
     * @return  List
     */
    List<String> stringMultiGet(String... keys);

    /**
     * Increment an integer value stored as string value under {code key} by {code delta}.
     * @param key   key
     * @param delta delta
     * @return  Long
     */
    Long stringIncrement(String key, long delta);

    /**
     * Decrement an integer value stored as string value under {code key} by {code delta}.
     * @param key   key
     * @param delta delta
     * @return  Long
     */
    Long stringDecrement(String key, long delta);

    /**
     * 转换类型
     * @param object    object
     * @param valueType valueType
     * @param <T>   T
     * @return  T
     * @throws JsonProcessingException  JsonProcessingException
     */
    <T> T convertObject(Object object, Class<T> valueType) throws JsonProcessingException;

    /**
     * 对象转String
     * @param object    object
     * @return  String
     * @throws JsonProcessingException  JsonProcessingException
     */
    String convertString(Object object) throws JsonProcessingException;

}



