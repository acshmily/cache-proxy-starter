package io.acshmily.cachetemplate.client.service.impl;

import io.acshmily.cachetemplate.client.bean.RequestBean;
import io.acshmily.cachetemplate.client.bean.ResponseBean;
import io.acshmily.cachetemplate.client.emun.*;
import io.acshmily.cachetemplate.client.service.CacheTemplate;
import io.acshmily.cachetemplate.client.utils.ObjectUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.json.JsonParseException;

import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * Author: Huanghz
 * Description: 服务实现类
 * Date:Created in 1:51 下午 2020/6/3
 * ModifyBy:
 **/
public class CacheTemplateImpl implements CacheTemplate {
    private OkHttpClient okHttpClient;
    private String urlPath;
    private ObjectMapper objectMapper;

    public CacheTemplateImpl() {
    }

    public CacheTemplateImpl(OkHttpClient okHttpClient, String urlPath) {
        this.okHttpClient = okHttpClient;
        this.urlPath = urlPath;
    }

    public CacheTemplateImpl(OkHttpClient okHttpClient, String urlPath, ObjectMapper objectMapper) {
        this.okHttpClient = okHttpClient;
        this.urlPath = urlPath;
        this.objectMapper = objectMapper;
    }

    /**
     * 指定键过期
     * @param key   key
     * @param timeout   timeout
     * @param timeUnit  timeUnit
     */
    @Override
    public void expire(@NotNull String key, @NotNull long timeout, @NotNull TimeUnit timeUnit) {
        RequestBean request = new RequestBean();
        request.setCmd(CommonCmdEnum.expire);
        LinkedList<Object> args = new LinkedList<>(Arrays.asList(key, timeout, timeUnit.toString()));
        request.setArgs(args);
        call(RequestPath.COMMON, request);


    }

    /**
     * 指定键在某个时间戳过期
     * @param key   key
     * @param timestamp timestamp
     */
    @Override
    public void expireAt(@NotNull String key, @NotNull long timestamp) {
        RequestBean request = new RequestBean();
        request.setCmd(CommonCmdEnum.expireAt);
        LinkedList<Object> args = new LinkedList<>(Arrays.asList(key, String.valueOf(timestamp)));
        request.setArgs(args);
        call(RequestPath.COMMON, request);

    }

    /**
     * 获取过期时间
     * @param key   key
     * @param timeUnit  timeUnit
     * @return  Long
     */
    @Override
    public Long getExpire(@NotNull String key, TimeUnit timeUnit) {
        RequestBean request = new RequestBean();
        request.setCmd(CommonCmdEnum.getExpire);
        LinkedList<Object> args = new LinkedList<>(Arrays.asList(key, timeUnit.toString()));
        request.setArgs(args);
        ResponseBean responseBean = call(RequestPath.COMMON, request);
        return ObjectUtils.convertToLong(responseBean.getData());
    }

    /**
     * 根据key删除缓存
     * @param keys  keys
     */
    @Override
    public void delete(String... keys) {
        RequestBean request = new RequestBean();
        request.setCmd(CommonCmdEnum.delete);
        LinkedList<Object> args = new LinkedList<>(Arrays.asList(Arrays.asList(keys)));
        request.setArgs(args);
        call(RequestPath.COMMON, request);
    }

    /**
     * 判断key是否存在
     * @param key   key
     * @return  Boolean
     */
    @Override
    public Boolean hasKey(@NotNull String key) {
        RequestBean request = new RequestBean();
        request.setCmd(CommonCmdEnum.hasKey);
        LinkedList<Object> args = new LinkedList<>(Arrays.asList(key));
        request.setArgs(args);
        ResponseBean responseBean = call(RequestPath.COMMON, request);
        return ObjectUtils.convertToBoolean(responseBean.getData());
    }

    /**
     * 删除指定key里的item
     * @param key   key
     * @param hashKeys  hashKeys
     */
    @Override
    public void deleteHashKeys(@NotNull String key, String... hashKeys) {
        RequestBean request = new RequestBean();
        request.setCmd(HashCmdEnum.delete);
        LinkedList<Object> args = new LinkedList<>(Arrays.asList(key, hashKeys));
        request.setArgs(args);
        ResponseBean responseBean = call(RequestPath.HASH, request);

    }

    /**
     * 判断key内是否存在指定hashKey
     * @param key   key
     * @param hashKey   hashKey
     * @return  Boolean
     */
    @Override
    public Boolean hasHashKey(@NotNull String key, String hashKey) {
        RequestBean request = new RequestBean();
        request.setCmd(HashCmdEnum.hasKey);
        LinkedList<Object> args = new LinkedList<>(Arrays.asList(key, hashKey));
        request.setArgs(args);
        ResponseBean responseBean = call(RequestPath.HASH, request);
        return ObjectUtils.convertToBoolean(responseBean.getData());
    }

    /**
     * 根据keykey及hashKey获取值并反序列化返回
     * @param key   key
     * @param hashKey   hashKey
     * @param valueType valueType
     * @param <T>   T
     * @return  T
     * @throws IOException  IOException
     * @throws JsonParseException   JsonParseException
     */
    @Override
    public <T> T hashGet(@NotNull String key, String hashKey, Class<T> valueType) throws IOException, JsonParseException {
        RequestBean request = new RequestBean();
        request.setCmd(HashCmdEnum.get);
        LinkedList<Object> args = new LinkedList<>(Arrays.asList(key, hashKey));
        request.setArgs(args);
        ResponseBean responseBean = call(RequestPath.HASH, request);
        return convertObject(responseBean.getData(), valueType);
    }

    /**
     * 获取key及hashKey获取值并直接返回
     * @param key   key
     * @param hashKey   hashKey
     * @return  String
     */
    @Override
    public String hashGet(@NotNull String key, String hashKey) {
        RequestBean request = new RequestBean();
        request.setCmd(HashCmdEnum.get);
        LinkedList<Object> args = new LinkedList<>(Arrays.asList(key, hashKey));
        request.setArgs(args);
        ResponseBean responseBean = call(RequestPath.HASH, request);
        return ObjectUtils.convertToString(responseBean.getData());
    }

    /**
     * 根据key及hashKeys获取值返回并且反序列化
     * @param key   key
     * @param valueType valueType
     * @param hashKeys  hashKeys
     * @param <T>   T
     * @return  T
     * @throws IOException  IOException
     * @throws JsonParseException   JsonParseException
     */
    @Override
    public <T> List<T> hashMultiGet(@NotNull String key, @NotNull Class<T> valueType, String... hashKeys) throws IOException, JsonParseException {
        RequestBean request = new RequestBean();
        request.setCmd(HashCmdEnum.multiGet);
        LinkedList<Object> args = new LinkedList<>(Arrays.asList(key, hashKeys));
        request.setArgs(args);
        ResponseBean responseBean = call(RequestPath.HASH, request);
        return objectMapper.readValue(String.valueOf(responseBean.getData()), new TypeReference<List<T>>() {
        });
    }

    /**
     * 根据key及hashKeys返回数组
     * @param key   key
     * @param hashKeys  hashKeys
     * @return  List
     * @throws JsonProcessingException  JsonProcessingException
     */
    @Override
    public List<Object> hashMultiGet(@NotNull String key, String... hashKeys) throws JsonProcessingException {
        RequestBean request = new RequestBean();
        request.setCmd(HashCmdEnum.multiGet);
        LinkedList<Object> args = new LinkedList<>(Arrays.asList(key, hashKeys));
        request.setArgs(args);
        ResponseBean responseBean = call(RequestPath.HASH, request);
        return objectMapper.readValue(String.valueOf(responseBean.getData()), new TypeReference<List<Object>>() {
        });
    }

    /**
     * 对hash值进行加法运算
     * @param key   key
     * @param hashKey   hashKey
     * @param delta delta
     * @return  Long
     */
    @Override
    public Long hashIncrement(String key, String hashKey, long delta) {
        RequestBean request = new RequestBean();
        request.setCmd(HashCmdEnum.increment);
        LinkedList<Object> args = new LinkedList<>(Arrays.asList(key, hashKey, delta));
        request.setArgs(args);
        ResponseBean responseBean = call(RequestPath.HASH, request);
        return ObjectUtils.convertToLong(responseBean.getData());
    }

    /**
     *  统计指定key的hash空间大小
     * @param key   key
     * @return  Long
     */
    @Override
    public Long hashSize(String key) {
        RequestBean request = new RequestBean();
        request.setCmd(HashCmdEnum.size);
        LinkedList<Object> args = new LinkedList<>(Arrays.asList(key));
        request.setArgs(args);
        ResponseBean responseBean = call(RequestPath.HASH, request);
        return ObjectUtils.convertToLong(responseBean.getData());
    }

    /**
     * 对指定key的指定hashKey进行设置
     * @param key   key
     * @param hashKey   hashKey
     * @param value value
     * @throws JsonProcessingException  JsonProcessingException
     */
    @Override
    public void hashPut(String key, String hashKey, Object value) throws JsonProcessingException {
        RequestBean request = new RequestBean();
        request.setCmd(HashCmdEnum.put);
        LinkedList<Object> args = new LinkedList<>(Arrays.asList(key, hashKey, isPrimitive(value) ? value : objectMapper.writeValueAsString(value)));
        request.setArgs(args);
        ResponseBean responseBean = call(RequestPath.HASH, request);
    }

    /**
     * 对指定key进行批量操作
     * @param key   key
     * @param map   map
     * @throws JsonProcessingException  JsonProcessingException
     */
    @Override
    public void hashPutAll(String key, Map<String, Object> map) throws JsonProcessingException {
        RequestBean request = new RequestBean();
        request.setCmd(HashCmdEnum.putAll);
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            if (!isPrimitive(entry.getValue())) {
                map.put(entry.getKey(), objectMapper.writeValueAsString(entry.getValue()));
            }
        }
        LinkedList<Object> args = new LinkedList<>(Arrays.asList(key, map));
        request.setArgs(args);
        ResponseBean responseBean = call(RequestPath.HASH, request);
    }

    /**
     * 根据Key获取所有Hash值并序列化返回
     * @param key   key
     * @param valueType valueType
     * @param <T>   T
     * @return  Map
     * @throws JsonProcessingException  JsonProcessingException
     */
    @Override
    public <T> Map<String, T> hashEntries(String key, Class<T> valueType) throws JsonProcessingException {
        RequestBean request = new RequestBean();
        request.setCmd(HashCmdEnum.entries);
        LinkedList<Object> args = new LinkedList<>(Arrays.asList(key));
        request.setArgs(args);
        ResponseBean responseBean = call(RequestPath.HASH, request);
        return (LinkedHashMap<String, T>) responseBean.getData();

        //return objectMapper.readValue(responseBean.getData().toString(),new TypeReference<Map<String,T>>(){});
    }

    /**
     * 根据key获取所有Hash值
     * @param key   key
     * @return  Map
     * @throws JsonProcessingException  JsonProcessingException
     */
    @Override
    public Map<String, String> hashEntries(String key) throws JsonProcessingException {
        return hashEntries(key, String.class);
    }

    /**
     * 获取list内范围值，并反序列化返回
     * @param key   key
     * @param start start
     * @param end   end
     * @param valueType valueType
     * @param <T>   T
     * @return  T
     * @throws JsonProcessingException  JsonProcessingException
     */
    @Override
    public <T> List<T> listRange(String key, long start, long end, Class<T> valueType) throws JsonProcessingException {
        RequestBean request = new RequestBean();
        request.setCmd(ListCmdEnum.range);
        LinkedList<Object> args = new LinkedList<>(Arrays.asList(key, start, end));
        request.setArgs(args);
        ResponseBean responseBean = call(RequestPath.LIST, request);
        return (List<T>) responseBean.getData();
    }

    /**
     * 获取list内范围值，并返回
     * @param key   key
     * @param start start
     * @param end   end
     * @return  List
     * @throws JsonProcessingException  JsonProcessingException
     */
    @Override
    public List<String> listRange(String key, long start, long end) throws JsonProcessingException {
        return listRange(key, start, end, String.class);
    }

    /**
     * 删除list首尾，只保留 [start, end] 之间的值
     * @param key   key
     * @param start start
     * @param end   end
     */
    @Override
    public void listTrim(String key, long start, long end) {
        RequestBean request = new RequestBean();
        request.setCmd(ListCmdEnum.trim);
        LinkedList<Object> args = new LinkedList<>(Arrays.asList(key, start, end));
        request.setArgs(args);
        ResponseBean responseBean = call(RequestPath.LIST, request);
    }

    /**
     * 获取指定key的大小
     * @param key   key
     * @return  Long
     */
    @Override
    public Long listSize(String key) {
        RequestBean request = new RequestBean();
        request.setCmd(ListCmdEnum.size);
        LinkedList<Object> args = new LinkedList<>(Arrays.asList(key));
        request.setArgs(args);
        ResponseBean responseBean = call(RequestPath.LIST, request);
        return ObjectUtils.convertToLong(responseBean.getData());
    }

    /**
     * 向左push
     * @param key   key
     * @param object    object
     * @throws JsonProcessingException  JsonProcessingException
     */
    @Override
    public void listLeftPush(String key, Object object) throws JsonProcessingException {
        RequestBean request = new RequestBean();
        request.setCmd(ListCmdEnum.leftPush);
        LinkedList<Object> args = new LinkedList<>(Arrays.asList(key, isPrimitive(object) ? object : objectMapper.writeValueAsString(object)));
        request.setArgs(args);
        ResponseBean responseBean = call(RequestPath.LIST, request);
    }

    /**
     * 向左批量push
     * @param key   key
     * @param objects   objects
     * @throws JsonProcessingException  JsonProcessingException
     */
    @Override
    public void listLeftPushAll(String key, Object... objects) throws JsonProcessingException {
        RequestBean request = new RequestBean();
        request.setCmd(ListCmdEnum.leftPushAll);
        final List<Object> collect = new ArrayList<>(objects.length);
        for (Object value : objects) {
            if (!isPrimitive(value)) {
                value = objectMapper.writeValueAsString(value);
            }
            collect.add(value);
        }
        LinkedList<Object> args = new LinkedList<>(Arrays.asList(key, collect));
        request.setArgs(args);
        ResponseBean responseBean = call(RequestPath.LIST, request);
    }

    /**
     * 向右push
     * @param key   key
     * @param object    object
     * @throws JsonProcessingException  JsonProcessingException
     */
    @Override
    public void listRightPush(String key, Object object) throws JsonProcessingException {
        RequestBean request = new RequestBean();
        request.setCmd(ListCmdEnum.rightPush);
        LinkedList<Object> args = new LinkedList<>(Arrays.asList(key, isPrimitive(object) ? object : objectMapper.writeValueAsString(object)));
        request.setArgs(args);
        ResponseBean responseBean = call(RequestPath.LIST, request);
    }

    /**
     * 批量向右push
     * @param key   key
     * @param objects   objects
     * @throws JsonProcessingException  JsonProcessingException
     */
    @Override
    public void listRightPushAll(String key, Object... objects) throws JsonProcessingException {
        RequestBean request = new RequestBean();
        request.setCmd(ListCmdEnum.rightPushAll);
        final List<Object> collect = new ArrayList<>(objects.length);
        for (Object value : objects) {
            if (!isPrimitive(value)) {
                value = objectMapper.writeValueAsString(value);
            }
            collect.add(value);
        }
        LinkedList<Object> args = new LinkedList<>(Arrays.asList(key, collect));
        request.setArgs(args);
        ResponseBean responseBean = call(RequestPath.LIST, request);
    }

    /**
     * 指定插入在指定索引
     * @param key   key
     * @param index index
     * @param value value
     * @throws JsonProcessingException  JsonProcessingException
     */
    @Override
    public void listSet(String key, long index, Object value) throws JsonProcessingException {
        RequestBean request = new RequestBean();
        request.setCmd(ListCmdEnum.set);
        LinkedList<Object> args = new LinkedList<>(Arrays.asList(key, index, isPrimitive(value) ? value : objectMapper.writeValueAsString(value)));
        request.setArgs(args);
        ResponseBean responseBean = call(RequestPath.LIST, request);
    }

    /**
     * 删除list上的指定索引
     * @param key   key
     * @param count count
     * @param value value
     */
    @Override
    public void listRemove(String key, long count, Object value) {
        RequestBean request = new RequestBean();
        request.setCmd(ListCmdEnum.remove);
        LinkedList<Object> args = new LinkedList<>(Arrays.asList(key, count, value));
        request.setArgs(args);
        ResponseBean responseBean = call(RequestPath.LIST, request);
    }

    /**
     * 获取指定key的指定索引的值
     * @param key   key
     * @param index index
     * @return  String
     */
    @Override
    public String listIndex(String key, long index) {
        RequestBean request = new RequestBean();
        request.setCmd(ListCmdEnum.index);
        LinkedList<Object> args = new LinkedList<>(Arrays.asList(key, index));
        request.setArgs(args);
        ResponseBean responseBean = call(RequestPath.LIST, request);
        return ObjectUtils.convertToString(responseBean.getData());
    }

    /**
     * 获取指定key的指定索引的值反序列化返回
     * @param key   key
     * @param index index
     * @param valueType valueType
     * @param <T>   T
     * @return  T
     * @throws IOException  IOException
     * @throws JsonParseException   JsonParseException
     */
    @Override
    public <T> T listIndex(String key, long index, Class<T> valueType) throws IOException, JsonParseException {
        RequestBean request = new RequestBean();
        request.setCmd(ListCmdEnum.index);
        LinkedList<Object> args = new LinkedList<>(Arrays.asList(key, index));
        request.setArgs(args);
        ResponseBean responseBean = call(RequestPath.LIST, request);
        return convertObject(responseBean.getData(), valueType);
    }

    /**
     * 指定key进行左Pop操作
     * @param key   key
     * @return  String
     */
    @Override
    public String listLeftPop(String key) {
        RequestBean request = new RequestBean();
        request.setCmd(ListCmdEnum.leftPop);
        LinkedList<Object> args = new LinkedList<>(Arrays.asList(key));
        request.setArgs(args);
        ResponseBean responseBean = call(RequestPath.LIST, request);
        return ObjectUtils.convertToString(responseBean.getData());
    }

    /**
     * 指定key进行左pop操作并反序列化返回
     * @param key   key
     * @param valueType valueType
     * @param <T>   T
     * @return T
     * @throws IOException  IOException
     * @throws JsonParseException   JsonParseException
     */
    @Override
    public <T> T listLeftPop(String key, Class<T> valueType) throws IOException, JsonParseException {
        RequestBean request = new RequestBean();
        request.setCmd(ListCmdEnum.leftPop);
        LinkedList<Object> args = new LinkedList<>(Arrays.asList(key));
        request.setArgs(args);
        ResponseBean responseBean = call(RequestPath.LIST, request);
        return convertObject(responseBean.getData(), valueType);
    }

    /**
     * 指定key进行右pop操作
     * @param key   key
     * @return  String
     */
    @Override
    public String listRightPop(String key) {
        RequestBean request = new RequestBean();
        request.setCmd(ListCmdEnum.rightPop);
        LinkedList<Object> args = new LinkedList<>(Arrays.asList(key));
        request.setArgs(args);
        ResponseBean responseBean = call(RequestPath.LIST, request);
        return ObjectUtils.convertToString(responseBean.getData());
    }

    /**
     * 指定key进行右Pop操作
     * @param key   key
     * @param valueType valueType
     * @param <T>   T
     * @return  T
     * @throws IOException  IOException
     * @throws JsonParseException   JsonParseException
     */
    @Override
    public <T> T listRightPop(String key, Class<T> valueType) throws IOException, JsonParseException {
        RequestBean request = new RequestBean();
        request.setCmd(ListCmdEnum.rightPop);
        LinkedList<Object> args = new LinkedList<>(Arrays.asList(key));
        request.setArgs(args);
        ResponseBean responseBean = call(RequestPath.LIST, request);
        return convertObject(responseBean.getData(), valueType);
    }

    /**
     * Add given {code objects} to set at {code key}.
     * @param key   key
     * @param objects   objects
     * @return  Long
     * @throws JsonProcessingException  JsonProcessingException
     */
    @Override
    public Long setAdd(String key, Object... objects) throws JsonProcessingException {
        RequestBean request = new RequestBean();
        request.setCmd(SetCmdEnum.add);
        final List<Object> collect = new ArrayList<>(objects.length);
        for (Object value : objects) {
            if (!isPrimitive(value)) {
                value = objectMapper.writeValueAsString(value);
            }
            collect.add(value);
        }
        LinkedList<Object> args = new LinkedList<>(Arrays.asList(key, collect));
        request.setArgs(args);
        ResponseBean responseBean = call(RequestPath.SET, request);
        return ObjectUtils.convertToLong(responseBean.getData());
    }

    /**
     * Remove given {code objects} from set at {code key} and return the number of removed elements.
     * @param key   key
     * @param objects   objects
     * @return  Long
     * @throws JsonProcessingException  JsonProcessingException
     */
    @Override
    public Long setRemove(String key, Object... objects) throws JsonProcessingException {
        RequestBean request = new RequestBean();
        request.setCmd(SetCmdEnum.remove);
        final List<Object> collect = new ArrayList<>(objects.length);
        for (Object value : objects) {
            if (!isPrimitive(value)) {
                value = objectMapper.writeValueAsString(value);
            }
            collect.add(value);
        }
        LinkedList<Object> args = new LinkedList<>(Arrays.asList(key, collect));
        request.setArgs(args);
        ResponseBean responseBean = call(RequestPath.SET, request);
        return ObjectUtils.convertToLong(responseBean.getData());
    }

    /**
     * Get size of set at {code key}.
     * @param key   key
     * @return  Long
     */
    @Override
    public Long setSize(String key) {
        RequestBean request = new RequestBean();
        request.setCmd(SetCmdEnum.size);
        LinkedList<Object> args = new LinkedList<>(Arrays.asList(key));
        request.setArgs(args);
        ResponseBean responseBean = call(RequestPath.SET, request);
        return ObjectUtils.convertToLong(responseBean.getData());
    }

    /**
     * Check if set at {code key} contains {code value}.
     * @param key   key
     * @param o object
     * @return  Boolean
     * @throws JsonProcessingException  JsonProcessingException
     */
    @Override
    public Boolean setIsMember(String key, Object o) throws JsonProcessingException {
        RequestBean request = new RequestBean();
        request.setCmd(SetCmdEnum.isMember);
        LinkedList<Object> args = new LinkedList<>(Arrays.asList(key, isPrimitive(o) ? o : objectMapper.writeValueAsString(o)));
        request.setArgs(args);
        ResponseBean responseBean = call(RequestPath.SET, request);
        return ObjectUtils.convertToBoolean(responseBean.getData());
    }

    /**
     * 根据key获取指定Set，并序列化返回
     * @param key   key
     * @param valueType valueType
     * @param <T>   T
     * @return  T
     */
    @Override
    public <T> Set<T> setMember(String key, Class<T> valueType) {
        RequestBean request = new RequestBean();
        request.setCmd(SetCmdEnum.members);
        LinkedList<Object> args = new LinkedList<>(Arrays.asList(key));
        request.setArgs(args);
        ResponseBean responseBean = call(RequestPath.SET, request);
        return ((List<T>) responseBean.getData()).stream().collect(Collectors.toSet());
    }

    /**
     * 根据key获取指定Set
     * @param key   key
     * @return  Set
     */
    @Override
    public Set<String> setMember(String key) {
        RequestBean request = new RequestBean();
        request.setCmd(SetCmdEnum.members);
        LinkedList<Object> args = new LinkedList<>(Arrays.asList(key));
        request.setArgs(args);
        ResponseBean responseBean = call(RequestPath.SET, request);
        return ((List<String>) responseBean.getData()).stream().collect(Collectors.toSet());
    }

    /**
     * 去重返回数据
     * @param key key
     * @param count count
     * @return
     */
    @Override
    public Set<String> distinctRandomMembers(String key, Long count) {
        RequestBean request = new RequestBean();
        request.setCmd(SetCmdEnum.distinctRandomMembers);
        LinkedList<Object> args = new LinkedList<>(Arrays.asList(key));
        request.setArgs(args);
        ResponseBean responseBean = call(RequestPath.SET, request);
        return ((List<String>) responseBean.getData()).stream().collect(Collectors.toSet());

    }

    /**
     * 去重返回数据
     * @param key key
     * @param count count
     * @param valueType valueType
     * @return
     */
    @Override
    public <T> Set<T> distinctRandomMembers(String key, Long count, Class<T> valueType) {
        RequestBean request = new RequestBean();
        request.setCmd(SetCmdEnum.distinctRandomMembers);
        LinkedList<Object> args = new LinkedList<>(Arrays.asList(key));
        request.setArgs(args);
        ResponseBean responseBean = call(RequestPath.SET, request);
        return ((List<T>) responseBean.getData()).stream().collect(Collectors.toSet());

    }

    /**
     * Add {code value} to a sorted set at {code key}, or update its {code score} if it already exists.
     * @param key   key
     * @param value value
     * @param score score
     * @return  Boolean
     * @throws JsonProcessingException  JsonProcessingException
     */
    @Override
    public Boolean zSetAddOne(String key, Object value, double score) throws JsonProcessingException {
        RequestBean request = new RequestBean();
        request.setCmd(ZSetCmdEnum.addOne);
        LinkedList<Object> args = new LinkedList<>(Arrays.asList(key, isPrimitive(value) ? value : objectMapper.writeValueAsString(value), String.valueOf(score)));
        request.setArgs(args);
        ResponseBean responseBean = call(RequestPath.ZSET, request);
        return ObjectUtils.convertToBoolean(responseBean.getData());
    }

    /**
     * Add {code tuples} to a sorted set at {code key}, or update its {code score} if it already exists.
     * @param key   key
     * @param tuples    tuples
     * @return  Long
     * @throws JsonProcessingException  JsonProcessingException
     */
    @Override
    public Long zSetAdd(String key, Map<String, String> tuples) throws JsonProcessingException {
        RequestBean request = new RequestBean();
        request.setCmd(ZSetCmdEnum.add);
        LinkedList<Object> args = new LinkedList<>(Arrays.asList(key, tuples));
        request.setArgs(args);
        ResponseBean responseBean = call(RequestPath.ZSET, request);
        return ObjectUtils.convertToLong(responseBean.getData());

    }

    /**
     * Remove {code values} from sorted set. Return number of removed elements.
     * @param key   key
     * @param objects   objects
     * @return  Long
     * @throws JsonProcessingException  JsonProcessingException
     */
    @Override
    public Long zSetRemove(String key, Object... objects) throws JsonProcessingException {
        RequestBean request = new RequestBean();
        request.setCmd(ZSetCmdEnum.remove);
        final List<Object> collect = new ArrayList<>(objects.length);
        for (Object value : objects) {
            if (!isPrimitive(value)) {
                value = objectMapper.writeValueAsString(value);
            }
            collect.add(value);
        }
        LinkedList<Object> args = new LinkedList<>(Arrays.asList(key, collect));
        request.setArgs(args);
        ResponseBean responseBean = call(RequestPath.ZSET, request);
        return ObjectUtils.convertToLong(responseBean.getData());
    }

    /**
     * Increment the score of element with {code value} in sorted set by {code increment}.
     * @param key   key
     * @param value value
     * @param delta delta
     * @return  Double
     * @throws JsonProcessingException  JsonProcessingException
     */
    @Override
    public Double zSetIncrementScore(String key, Object value, double delta) throws JsonProcessingException {
        RequestBean request = new RequestBean();
        request.setCmd(ZSetCmdEnum.incrementScore);
        LinkedList<Object> args = new LinkedList<>(Arrays.asList(key, isPrimitive(value) ? value : objectMapper.writeValueAsString(value), String.valueOf(delta)));
        request.setArgs(args);
        ResponseBean responseBean = call(RequestPath.ZSET, request);
        return ObjectUtils.convertToDouble(responseBean.getData());
    }

    /**
     * Determine the index of element with {code value} in a sorted set.
     * @param key   key
     * @param object    object
     * @return  Long
     * @throws JsonProcessingException  JsonProcessingException
     */
    @Override
    public Long zSetRank(String key, Object object) throws JsonProcessingException {
        RequestBean request = new RequestBean();
        request.setCmd(ZSetCmdEnum.rank);
        LinkedList<Object> args = new LinkedList<>(Arrays.asList(key, isPrimitive(object) ? object : objectMapper.writeValueAsString(object)));
        request.setArgs(args);
        ResponseBean responseBean = call(RequestPath.ZSET, request);
        return ObjectUtils.convertToLong(responseBean.getData());
    }

    /**
     * Determine the index of element with {code value} in a sorted set when scored high to low.
     * @param key   key
     * @param object    object
     * @return  Long
     * @throws JsonProcessingException  JsonProcessingException
     */
    @Override
    public Long zSetReverseRank(String key, Object object) throws JsonProcessingException {
        RequestBean request = new RequestBean();
        request.setCmd(ZSetCmdEnum.reverseRank);
        LinkedList<Object> args = new LinkedList<>(Arrays.asList(key, isPrimitive(object) ? object : objectMapper.writeValueAsString(object)));
        request.setArgs(args);
        ResponseBean responseBean = call(RequestPath.ZSET, request);
        return ObjectUtils.convertToLong(responseBean.getData());
    }

    /**
     *  Get set of between {code start} and {code end} from sorted set.
     * @param key   key
     * @param start start
     * @param end   end
     * @param valueType valueType
     * @param <T>   T
     * @return  Map
     */
    @Override
    public <T> Map<T, Double> zSetRangeWithScores(String key, long start, long end, Class<T> valueType) {
        RequestBean request = new RequestBean();
        request.setCmd(ZSetCmdEnum.rangeWithScores);
        LinkedList<Object> args = new LinkedList<>(Arrays.asList(key, start, end));
        request.setArgs(args);
        ResponseBean responseBean = call(RequestPath.ZSET, request);
        return (Map<T, Double>) responseBean.getData();
    }

    /**
     * Get set of between {code start} and {code end} from sorted set.
     * @param key   key
     * @param start start
     * @param end   end
     * @param valueType valueType
     * @param <T>   T
     * @return  Map
     */
    @Override
    public <T> Map<T, Double> zSetReverseRangeWithScores(String key, long start, long end, Class<T> valueType) {
        RequestBean request = new RequestBean();
        request.setCmd(ZSetCmdEnum.reverseRangeWithScores);
        LinkedList<Object> args = new LinkedList<>(Arrays.asList(key, start, end));
        request.setArgs(args);
        ResponseBean responseBean = call(RequestPath.ZSET, request);
        return (Map<T, Double>) responseBean.getData();
    }

    /**
     * Get set of  in range from {code start} to {code end} where score is between {code min}
     * @param key   key
     * @param min   min
     * @param max   max
     * @param offset    offset
     * @param count count
     * @param valueType valueType
     * @param <T>   T
     * @return  Map
     */
    @Override
    public <T> Map<T, Double> zSetRangeByScoreWithScores(String key, double min, double max, long offset, long count, Class<T> valueType) {
        RequestBean request = new RequestBean();
        request.setCmd(ZSetCmdEnum.rangeByScoreWithScores);
        LinkedList<Object> args = new LinkedList<>(Arrays.asList(key, String.valueOf(min), String.valueOf(max), offset, count));
        request.setArgs(args);
        ResponseBean responseBean = call(RequestPath.ZSET, request);
        return (Map<T, Double>) responseBean.getData();
    }

    /**
     *  Get set of  in range from {code start} to {code end} where score is between {code min} and
     *   {code max} from sorted set ordered high to low.
     * @param key   key
     * @param min   min
     * @param max   max
     * @param offset    offset
     * @param count count
     * @param valueType valueType
     * @param <T>   T
     * @return  Map
     */
    @Override
    public <T> Map<T, Double> zSetReverseRangeByScoreWithScores(String key, double min, double max, long offset, long count, Class<T> valueType) {
        RequestBean request = new RequestBean();
        request.setCmd(ZSetCmdEnum.reverseRangeByScoreWithScores);
        LinkedList<Object> args = new LinkedList<>(Arrays.asList(key, String.valueOf(min), String.valueOf(max), offset, count));
        request.setArgs(args);
        ResponseBean responseBean = call(RequestPath.ZSET, request);
        return (Map<T, Double>) responseBean.getData();
    }

    /**
     * Count number of elements within sorted set with scores between {code min} and {code max}.
     * @param key   key
     * @param min   min
     * @param max   max
     * @return  Long
     */
    @Override
    public Long zSetCount(String key, double min, double max) {
        RequestBean request = new RequestBean();
        request.setCmd(ZSetCmdEnum.count);
        LinkedList<Object> args = new LinkedList<>(Arrays.asList(key, String.valueOf(min), String.valueOf(max)));
        request.setArgs(args);
        ResponseBean responseBean = call(RequestPath.ZSET, request);
        return ObjectUtils.convertToLong(responseBean.getData());
    }

    /**
     * Get the size of sorted set with {code key}.
     * @param key   key
     * @return  Long
     */
    @Override
    public Long zSetCard(String key) {
        RequestBean request = new RequestBean();
        request.setCmd(ZSetCmdEnum.zCard);
        LinkedList<Object> args = new LinkedList<>(Arrays.asList(key));
        request.setArgs(args);
        ResponseBean responseBean = call(RequestPath.ZSET, request);
        return ObjectUtils.convertToLong(responseBean.getData());
    }

    /**
     * Get the score of element with {code value} from sorted set with key {code key}.
     * @param key   key
     * @param object    object
     * @return  Double
     * @throws JsonProcessingException  JsonProcessingException
     */
    @Override
    public Double zSetScore(String key, Object object) throws JsonProcessingException {
        RequestBean request = new RequestBean();
        request.setCmd(ZSetCmdEnum.score);
        LinkedList<Object> args = new LinkedList<>(Arrays.asList(key, isPrimitive(object) ? object : objectMapper.writeValueAsString(object)));
        request.setArgs(args);
        ResponseBean responseBean = call(RequestPath.ZSET, request);
        return ObjectUtils.convertToDouble(responseBean.getData());
    }

    /**
     * Remove elements in range between {code start} and {code end} from sorted set with {code key}.
     * @param key   key
     * @param start start
     * @param end   end
     * @return  Long
     */
    @Override
    public Long zSetRemoveRange(String key, long start, long end) {
        RequestBean request = new RequestBean();
        request.setCmd(ZSetCmdEnum.removeRange);
        LinkedList<Object> args = new LinkedList<>(Arrays.asList(key, start, end));
        request.setArgs(args);
        ResponseBean responseBean = call(RequestPath.ZSET, request);
        return ObjectUtils.convertToLong(responseBean.getData());
    }

    /**
     * Remove elements with scores between {code min} and {code max} from sorted set with {code key}.
     * @param key   key
     * @param min   min
     * @param max   max
     * @return  Long
     */
    @Override
    public Long zSetRemoveByScore(String key, double min, double max) {
        RequestBean request = new RequestBean();
        request.setCmd(ZSetCmdEnum.removeRangeByScore);
        LinkedList<Object> args = new LinkedList<>(Arrays.asList(key, String.valueOf(min), String.valueOf(max)));
        request.setArgs(args);
        ResponseBean responseBean = call(RequestPath.ZSET, request);
        return ObjectUtils.convertToLong(responseBean.getData());
    }

    /**
     * Set {code value} for {code key}.
     * @param key   key
     * @param value value
     */
    @Override
    public void stringSet(String key, String value) {
        RequestBean request = new RequestBean();
        request.setCmd(StringCmdEnum.set);
        LinkedList<Object> args = new LinkedList<>(Arrays.asList(key, value));
        request.setArgs(args);
        ResponseBean responseBean = call(RequestPath.STRING, request);
    }

    /**
     * Set the {code value} and expiration {code timeout} for {code key}.
     * @param key   key
     * @param value value
     * @param timeout   timeout
     * @param unit  unit
     */
    @Override
    public void stringSet(String key, String value, long timeout, TimeUnit unit) {
        RequestBean request = new RequestBean();
        request.setCmd(StringCmdEnum.setEx);
        LinkedList<Object> args = new LinkedList<>(Arrays.asList(key, value, timeout, unit));
        request.setArgs(args);
        ResponseBean responseBean = call(RequestPath.STRING, request);
    }

    /**
     * Set {code key} to hold the string {code value} if {code key} is absent.
     * @param key   key
     * @param value value
     * @return  Boolean
     */
    @Override
    public Boolean stringSetIfAbsent(String key, String value) {
        RequestBean request = new RequestBean();
        request.setCmd(StringCmdEnum.setNx);
        LinkedList<Object> args = new LinkedList<>(Arrays.asList(key, value));
        request.setArgs(args);
        ResponseBean responseBean = call(RequestPath.STRING, request);
        return ObjectUtils.convertToBoolean(responseBean.getData());
    }

    /**
     *  Set {code key} to hold the string {code value} and expiration {code timeout} if {code key} is absent.
     * @param key   key
     * @param value value
     * @param timeout   timeout
     * @param unit  unit
     * @return  Boolean
     */
    @Override
    public Boolean stringSetIfAbsent(String key, String value, long timeout, TimeUnit unit) {
        RequestBean request = new RequestBean();
        request.setCmd(StringCmdEnum.setNxEx);
        LinkedList<Object> args = new LinkedList<>(Arrays.asList(key, value, timeout, unit));
        request.setArgs(args);
        ResponseBean responseBean = call(RequestPath.STRING, request);
        return ObjectUtils.convertToBoolean(responseBean.getData());
    }


    /**
     * 默认返回String值
     * @param key   key
     * @return  String
     */
    @Override
    public String stringGet(String key) {
        RequestBean request = new RequestBean();
        request.setCmd(StringCmdEnum.get);
        LinkedList<Object> args = new LinkedList<>(Arrays.asList(key));
        request.setArgs(args);
        ResponseBean responseBean = call(RequestPath.STRING, request);
        return ObjectUtils.convertToString(responseBean.getData());
    }

    /**
     * 根据key查询并反序列化
     * @param key   key
     * @param valueType valueType
     * @param <T>   T
     * @return  T
     * @throws JsonProcessingException  JsonProcessingException
     */
    @Override
    public <T> T stringGet(String key, Class<T> valueType) throws JsonProcessingException {
        RequestBean request = new RequestBean();
        request.setCmd(StringCmdEnum.get);
        LinkedList<Object> args = new LinkedList<>(Arrays.asList(key));
        request.setArgs(args);
        ResponseBean responseBean = call(RequestPath.STRING, request);
        return convertObject(responseBean.getData(), valueType);
    }

    /**
     * Get multiple {code keys}. Values are returned in the order of the requested keys.
     * @param keys  keys
     * @return  List
     */
    @Override
    public List<String> stringMultiGet(String... keys) {
        RequestBean request = new RequestBean();
        request.setCmd(StringCmdEnum.multiGet);
        LinkedList<Object> args = new LinkedList<>(Arrays.asList(Arrays.asList(keys)));
        request.setArgs(args);
        ResponseBean responseBean = call(RequestPath.STRING, request);
        return (List<String>) responseBean.getData();
    }

    /**
     * Increment an integer value stored as string value under {code key} by {code delta}.
     * @param key   key
     * @param delta delta
     * @return  Long
     */
    @Override
    public Long stringIncrement(String key, long delta) {
        RequestBean request = new RequestBean();
        request.setCmd(StringCmdEnum.increment);
        LinkedList<Object> args = new LinkedList<>(Arrays.asList(key, delta));
        request.setArgs(args);
        ResponseBean responseBean = call(RequestPath.STRING, request);
        return ObjectUtils.convertToLong(responseBean.getData());
    }

    /**
     * Decrement an integer value stored as string value under {code key} by {code delta}.
     * @param key   key
     * @param delta delta
     * @return  Long
     */
    @Override
    public Long stringDecrement(String key, long delta) {
        RequestBean request = new RequestBean();
        request.setCmd(StringCmdEnum.decrement);
        LinkedList<Object> args = new LinkedList<>(Arrays.asList(key, delta));
        request.setArgs(args);
        ResponseBean responseBean = call(RequestPath.STRING, request);
        return ObjectUtils.convertToLong(responseBean.getData());
    }

    /**
     * Set {code value} for {code key}.
     * @param key   key
     * @param value value
     * @throws JsonProcessingException  JsonProcessingException
     */
    @Override
    public void stringSet(String key, Object value) throws JsonProcessingException {
        RequestBean request = new RequestBean();
        request.setCmd(StringCmdEnum.set);
        LinkedList<Object> args = new LinkedList<>(Arrays.asList(key, isPrimitive(value) ? value : objectMapper.writeValueAsString(value)));
        request.setArgs(args);
        ResponseBean responseBean = call(RequestPath.STRING, request);
    }

    /**
     * Set the {code value} and expiration {code timeout} for {code key}.
     * @param key   key
     * @param value value
     * @param timeout   timeout
     * @param unit  unit
     * @throws JsonProcessingException  JsonProcessingException
     */
    @Override
    public void stringSet(String key, Object value, long timeout, TimeUnit unit) throws JsonProcessingException {
        RequestBean request = new RequestBean();
        request.setCmd(StringCmdEnum.setEx);
        LinkedList<Object> args = new LinkedList<>(Arrays.asList(key, isPrimitive(value) ? value : objectMapper.writeValueAsString(value), timeout, unit));
        request.setArgs(args);
        ResponseBean responseBean = call(RequestPath.STRING, request);

    }

    /**
     * Set {code key} to hold the string {code value} if {code key} is absent.
     * @param key   key
     * @param value value
     * @return  Boolean
     * @throws JsonProcessingException  JsonProcessingException
     */
    @Override
    public Boolean stringSetIfAbsent(String key, Object value) throws JsonProcessingException {
        RequestBean request = new RequestBean();
        request.setCmd(StringCmdEnum.setNx);
        LinkedList<Object> args = new LinkedList<>(Arrays.asList(key, isPrimitive(value) ? value : objectMapper.writeValueAsString(value)));
        request.setArgs(args);
        ResponseBean responseBean = call(RequestPath.STRING, request);
        return ObjectUtils.convertToBoolean(responseBean.getData());

    }

    /**
     * Set {code key} to hold the string {code value} and expiration {code timeout} if {code key} is absent.
     * @param key   key
     * @param value value
     * @param timeout   timeout
     * @param unit  unit
     * @return  Boolean
     * @throws JsonProcessingException  JsonProcessingException
     */
    @Override
    public Boolean stringSetIfAbsent(String key, Object value, long timeout, TimeUnit unit) throws JsonProcessingException {
        RequestBean request = new RequestBean();
        request.setCmd(StringCmdEnum.setNxEx);
        LinkedList<Object> args = new LinkedList<>(Arrays.asList(key, isPrimitive(value) ? value : objectMapper.writeValueAsString(value), timeout, unit));
        request.setArgs(args);
        ResponseBean responseBean = call(RequestPath.STRING, request);
        return ObjectUtils.convertToBoolean(responseBean.getData());
    }

    /**
     *  Set multiple keys to multiple values using key-value pairs provided in {code tuple}.
     * @param map   map
     * @throws JsonProcessingException  JsonProcessingException
     */
    @Override
    public void stringMultiSet(Map<String, Object> map) throws JsonProcessingException {
        RequestBean request = new RequestBean();
        request.setCmd(StringCmdEnum.multiSet);
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            if (!isPrimitive(entry.getValue())) {
                map.put(entry.getKey(), objectMapper.writeValueAsString(entry.getValue()));
            }
        }
        LinkedList<Object> args = new LinkedList<>(Arrays.asList(map));
        request.setArgs(args);
        ResponseBean responseBean = call(RequestPath.STRING, request);
    }

    /**
     *  Set multiple keys to multiple values using key-value pairs provided in {code tuple} only if the provided key does
     *  not exist.
     * @param map   map
     * @return  Boolean
     * @throws JsonProcessingException  JsonProcessingException
     */
    @Override
    public Boolean stringMultiSetIfAbsent(Map<String, Object> map) throws JsonProcessingException {
        RequestBean request = new RequestBean();
        request.setCmd(StringCmdEnum.multiSetNx);
        Map<String, Object> result = new HashMap<>(map.size());
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            if (!isPrimitive(entry.getValue())) {
                map.put(entry.getKey(), objectMapper.writeValueAsString(entry.getValue()));
            }
        }
        LinkedList<Object> args = new LinkedList<>(Arrays.asList(map));
        request.setArgs(args);
        ResponseBean responseBean = call(RequestPath.STRING, request);
        return ObjectUtils.convertToBoolean(responseBean.getData());
    }

    @Override
    public <T> T convertObject(Object object, Class<T> valueType) throws JsonProcessingException {
        if (object == null) {
            return null;
        }
        return objectMapper.readValue(object.toString(), valueType);
    }

    /**
     * 对象转String
     * @param object    object
     * @return  String
     * @throws JsonProcessingException  JsonProcessingException
     */
    @Override
    public String convertString(Object object) throws JsonProcessingException {
        if (object == null) {
            return null;
        }
        return objectMapper.writeValueAsString(object);
    }

    private boolean isPrimitive(Object obj) {
        try {
            return (obj instanceof String) || ((Class<?>) obj.getClass().getField("TYPE").get(null)).isPrimitive();
        } catch (Exception e) {
            return false;
        }
    }


    public OkHttpClient getOkHttpClient() {
        return okHttpClient;
    }

    public void setOkHttpClient(OkHttpClient okHttpClient) {
        this.okHttpClient = okHttpClient;
    }

    public String getUrlPath() {
        return urlPath;
    }

    public void setUrlPath(String urlPath) {
        this.urlPath = urlPath;
    }

    public ObjectMapper getObjectMapper() {
        return objectMapper;
    }

    public void setObjectMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    /**
     * 输出返回定义
     * @param requestPath   requestPath
     * @param requestBean   requestBean
     * @return  ResponseBean
     */
    private ResponseBean call(RequestPath requestPath, RequestBean requestBean) {
        Request request = null;
        ResponseBean responseBody = null;
        try {
            request = new Request.Builder().
                    url(urlPath + requestPath.getValue()).
                    post(RequestBody.create(JSON_TYPE, objectMapper.writeValueAsString(requestBean)))
                    .build();
            Response response = okHttpClient.newCall(request).execute();
            // 请求成功
            if (response.isSuccessful()) {
                responseBody = objectMapper.readValue(response.body().string(), ResponseBean.class);
            }
        } catch (IOException e) {
            log.warn("本次调用失败,地址：{},参数:{},原因:{}", request.url(), request.body(), e.getMessage());
        }
        return responseBody;
    }

    private final static Logger log = LoggerFactory.getLogger(CacheTemplateImpl.class);


}
