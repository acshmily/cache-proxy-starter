package cn.besttone.cachetemplate.autoconfigure.service.impl;

import cn.besttone.cachetemplate.autoconfigure.bean.RequestBean;
import cn.besttone.cachetemplate.autoconfigure.bean.ResponseBean;
import cn.besttone.cachetemplate.autoconfigure.emun.*;
import cn.besttone.cachetemplate.autoconfigure.service.CacheTemplate;
import cn.besttone.cachetemplate.autoconfigure.utils.ObjectUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.springframework.beans.BeanUtils;
import org.springframework.boot.json.JsonParseException;

import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @Author: Huanghz
 * @Description: 服务实现类
 * @Date:Created in 1:51 下午 2020/6/3
 * @ModifyBy:
 **/
@Slf4j
public class CacheTemplateImpl implements CacheTemplate {
    public CacheTemplateImpl() {
    }

    public CacheTemplateImpl(OkHttpClient okHttpClient, String urlPath) {
        this.okHttpClient = okHttpClient;
        this.urlPath = urlPath;
    }
    public CacheTemplateImpl(OkHttpClient okHttpClient, String urlPath,ObjectMapper objectMapper) {
        this.okHttpClient = okHttpClient;
        this.urlPath = urlPath;
        this.objectMapper = objectMapper;
    }
    @Override
    public String helloWord() {
        Request request = new Request.Builder().url("http://www.baidu.com").build();

        try {
            Response response = okHttpClient.newCall(request).execute();
            return response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "测试存在部分错误";
    }

    /**
     * 指定键过期
     *
     * @param key
     * @param timeout
     * @param timeUnit
     */
    @Override
    public void expire(@NotNull String key, @NotNull long timeout, @NotNull TimeUnit timeUnit) {
        RequestBean request = new RequestBean();
        request.setCmd(CommonCmdEnum.expire);
        LinkedList<Object> args = new LinkedList<>(Arrays.asList(key, timeout, timeUnit));
        request.setArgs(args);
        call(RequestPath.COMMON,request);


    }

    /**
     * 指定键在某个时间戳过期
     *
     * @param key
     * @param timestamp
     */
    @Override
    public void expireAt(@NotNull String key,@NotNull long timestamp) {
        RequestBean request = new RequestBean();
        request.setCmd(CommonCmdEnum.expireAt);
        LinkedList<Object> args = new LinkedList<>(Arrays.asList(key, timestamp));
        request.setArgs(args);
        call(RequestPath.COMMON,request);

    }

    /**
     * 获取过期时间
     *
     * @param key
     * @param timeUnit 时间(秒) 返回0代表为永久有效
     * @return
     */
    @Override
    public Long getExpire(String key, TimeUnit timeUnit) {
        RequestBean request = new RequestBean();
        request.setCmd(CommonCmdEnum.expireAt);
        LinkedList<Object> args = new LinkedList<>(Arrays.asList(key, timeUnit));
        request.setArgs(args);
        ResponseBean responseBean = call(RequestPath.COMMON,request);
        return ObjectUtils.convertToLong(responseBean.getData());
    }

    /**
     * 根据key删除缓存
     *
     * @param keys
     */
    @Override
    public void delete(String... keys) {
        RequestBean request = new RequestBean();
        request.setCmd(CommonCmdEnum.delete);
        LinkedList<Object> args = new LinkedList<>(Arrays.asList(keys));
        request.setArgs(args);
        call(RequestPath.COMMON,request);
    }

    /**
     * 判断key是否存在
     *
     * @param key
     * @return
     */
    @Override
    public Boolean hasKey(String key) {
        RequestBean request = new RequestBean();
        request.setCmd(CommonCmdEnum.hasKey);
        LinkedList<Object> args = new LinkedList<>(Arrays.asList(key));
        request.setArgs(args);
        ResponseBean responseBean = call(RequestPath.COMMON,request);
        return ObjectUtils.convertToBoolean(responseBean.getData());
    }

    /**
     * 删除指定key里的item
     *
     * @param key
     * @param hashKeys
     */
    @Override
    public void deleteHashKeys(String key, String... hashKeys) {
        RequestBean request = new RequestBean();
        request.setCmd(HashCmdEnum.delete);
        LinkedList<Object> args = new LinkedList<>(Arrays.asList(key,hashKeys));
        request.setArgs(args);
        ResponseBean responseBean = call(RequestPath.HASH,request);

    }

    /**
     * 判断key内是否存在指定hashKey
     *
     * @param key
     * @param hashKey
     * @return
     */
    @Override
    public Boolean hasHashKey(String key, String hashKey) {
        RequestBean request = new RequestBean();
        request.setCmd(HashCmdEnum.hasKey);
        LinkedList<Object> args = new LinkedList<>(Arrays.asList(key,hashKey));
        request.setArgs(args);
        ResponseBean responseBean = call(RequestPath.HASH,request);
        return ObjectUtils.convertToBoolean(responseBean.getData());
    }

    /**
     * 按照key获取值并反序列化成实例返回(String 默认get)
     *
     * @param key
     * @return
     */
    @Override
    public String get(String key){
        RequestBean request = new RequestBean();
        request.setCmd(StringCmdEnum.get);
        LinkedList<Object> args = new LinkedList<>(Arrays.asList(key));
        request.setArgs(args);
        ResponseBean responseBean = call(RequestPath.STRING,request);
        return responseBean.getData();

    }

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
    @Override
    public <T> T hashGet(String key, String hashKey, Class<T> valueType) throws IOException, JsonParseException {
        RequestBean request = new RequestBean();
        request.setCmd(HashCmdEnum.get);
        LinkedList<Object> args = new LinkedList<>(Arrays.asList(key,hashKey));
        request.setArgs(args);
        ResponseBean responseBean = call(RequestPath.HASH,request);
        return objectMapper.readValue(responseBean.getData(),valueType);
    }

    /**
     * 获取key及hashKey获取值并直接返回
     *
     * @param key
     * @param hashKey
     * @return
     */
    @Override
    public String hashGet(String key, String hashKey) {
        RequestBean request = new RequestBean();
        request.setCmd(HashCmdEnum.get);
        LinkedList<Object> args = new LinkedList<>(Arrays.asList(key,hashKey));
        request.setArgs(args);
        ResponseBean responseBean = call(RequestPath.HASH,request);
        return responseBean.getData();
    }

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
    @Override
    public <T> List<T> hashMultiGet(String key, Class<T> valueType, String... hashKeys) throws IOException, JsonParseException {
        RequestBean request = new RequestBean();
        request.setCmd(HashCmdEnum.multiGet);
        LinkedList<Object> args = new LinkedList<>(Arrays.asList(key,hashKeys));
        request.setArgs(args);
        ResponseBean responseBean = call(RequestPath.HASH,request);
        return objectMapper.readValue(responseBean.getData(),new TypeReference<List<T>>(){});
    }

    /**
     * 根据key及hashKeys返回数组
     *
     * @param key
     * @param hashKeys
     * @return
     */
    @Override
    public List<String> hashMultiGet(String key, String... hashKeys) {
        return null;
    }

    /**
     * 对hash值进行加法运算
     *
     * @param key
     * @param hashKey
     * @param delta
     * @return
     */
    @Override
    public Long hashIncrement(String key, String hashKey, long delta) {
        return null;
    }

    /**
     * 统计指定key的hash空间大小
     *
     * @param key
     * @return
     */
    @Override
    public Long hashSize(String key) {
        return null;
    }

    /**
     * 对指定key的指定hashKey进行设置
     *
     * @param key
     * @param hashKey
     * @param value
     */
    @Override
    public void hashPut(String key, String hashKey, Object value) {

    }

    /**
     * 对指定key进行批量操作
     *
     * @param key
     * @param map
     */
    @Override
    public void hashPutAll(String key, Map<String, Object> map) {

    }

    /**
     * 根据Key获取所有Hash值并序列化返回
     *
     * @param key
     * @param valueType
     * @return
     */
    @Override
    public <T> Map<String, T> hashEntries(String key, Class<T> valueType) {
        return null;
    }

    /**
     * 根据key获取所有Hash值
     *
     * @param key
     * @return
     */
    @Override
    public Map<String, String> hashEntries(String key) {
        return null;
    }

    /**
     * 获取list内范围值，并反序列化返回
     *
     * @param key
     * @param start
     * @param end
     * @param valueType
     * @return
     */
    @Override
    public <T> List<T> listRange(String key, long start, long end, Class<T> valueType) {
        return null;
    }

    /**
     * 获取list内范围值，并返回
     *
     * @param key
     * @param start
     * @param end
     * @return
     */
    @Override
    public List<String> listRange(String key, long start, long end) {
        return null;
    }

    /**
     * 删除list首尾，只保留 [start, end] 之间的值
     *
     * @param key
     * @param start
     * @param end
     */
    @Override
    public void listTrim(String key, long start, long end) {

    }

    /**
     * 获取指定key的大小
     *
     * @param key
     * @return
     */
    @Override
    public Long listSize(String key) {
        return null;
    }

    /**
     * 向左push
     *
     * @param key
     * @param object
     */
    @Override
    public void listLeftPush(String key, Object object) {

    }

    /**
     * 向左批量push
     *
     * @param key
     * @param objects
     */
    @Override
    public void listLeftPushAll(String key, Object... objects) {

    }

    /**
     * 向右push
     *
     * @param key
     * @param object
     */
    @Override
    public void listRightPush(String key, Object object) {

    }

    /**
     * 批量向右push
     *
     * @param key
     * @param objects
     */
    @Override
    public void listRightPushAll(String key, Object... objects) {

    }

    /**
     * 指定插入在指定索引
     *
     * @param key
     * @param index
     * @param value
     */
    @Override
    public void listSet(String key, long index, Object value) {

    }

    /**
     * 删除list上的指定索引
     *
     * @param key
     * @param index
     */
    @Override
    public void listRemove(String key, long index) {

    }

    /**
     * 获取指定key的指定索引的值
     *
     * @param key
     * @param index
     * @return
     */
    @Override
    public String listIndex(String key, long index) {
        return null;
    }

    /**
     * 获取指定key的指定索引的值反序列化返回
     *
     * @param key
     * @param index
     * @param valueType
     * @return
     */
    @Override
    public <T> T listIndex(String key, long index, Class<T> valueType) throws IOException, JsonParseException {
        return null;
    }

    /**
     * 指定key进行左Pop操作
     *
     * @param key
     * @return
     */
    @Override
    public String listLeftPop(String key) {
        return null;
    }

    /**
     * 指定key进行左pop操作并反序列化返回
     *
     * @param key
     * @param valueType
     * @return
     */
    @Override
    public <T> T listLeftPop(String key, Class<T> valueType) throws IOException, JsonParseException {
        return null;
    }

    /**
     * 指定key进行右pop操作
     *
     * @param key
     * @return
     */
    @Override
    public String listRightPop(String key) {
        return null;
    }

    /**
     * 指定key进行右Pop操作
     *
     * @param key
     * @param valueType
     * @return
     * @throws IOException
     * @throws JsonParseException
     */
    @Override
    public <T> T listRightPop(String key, Class<T> valueType) throws IOException, JsonParseException {
        return null;
    }

    /**
     * Add given {@code objects} to set at {@code key}.
     *
     * @param key
     * @param objects
     * @return
     */
    @Override
    public Long setAdd(String key, Object... objects) {
        return null;
    }

    /**
     * Remove given {@code objects} from set at {@code key} and return the number of removed elements.
     *
     * @param key
     * @param objects
     * @return
     */
    @Override
    public Long setRemove(String key, Object... objects) {
        return null;
    }

    /**
     * Get size of set at {@code key}.
     *
     * @param key
     * @return
     */
    @Override
    public Long setSize(String key) {
        return null;
    }

    /**
     * Check if set at {@code key} contains {@code value}.
     *
     * @param key
     * @param o
     * @return
     */
    @Override
    public Boolean setIsMember(String key, Object o) {
        return null;
    }

    /**
     * 根据key获取指定Set，并序列化返回
     *
     * @param key
     * @param valueType
     * @return
     */
    @Override
    public <T> Set<T> setMember(String key, Class<T> valueType) {
        return null;
    }

    /**
     * 根据key获取指定Set
     *
     * @param key
     * @return
     */
    @Override
    public Set<String> setMember(String key) {
        return null;
    }

    /**
     * Add {@code value} to a sorted set at {@code key}, or update its {@code score} if it already exists.
     *
     * @param key
     * @param value
     * @param score
     * @return
     */
    @Override
    public Boolean zSetAddOne(String key, Object value, double score) {
        return null;
    }

    /**
     * Add {@code tuples} to a sorted set at {@code key}, or update its {@code score} if it already exists.
     *
     * @param key
     * @param tuples
     * @return
     */
    @Override
    public Long zSetAdd(String key, Map<String,Object> tuples) {
        return null;
    }

    /**
     * Remove {@code values} from sorted set. Return number of removed elements.
     *
     * @param key
     * @param objects
     * @return
     */
    @Override
    public Long zSetRemove(String key, Object... objects) {
        return null;
    }

    /**
     * Increment the score of element with {@code value} in sorted set by {@code increment}.
     *
     * @param key
     * @param value
     * @param delta
     * @return
     */
    @Override
    public Double zSetIncrementScore(String key, Object value, double delta) {
        return null;
    }

    /**
     * Determine the index of element with {@code value} in a sorted set.
     *
     * @param key
     * @param object
     * @return
     */
    @Override
    public Long zSetRank(String key, Object object) {
        return null;
    }

    /**
     * Determine the index of element with {@code value} in a sorted set when scored high to low.
     *
     * @param key
     * @param object
     * @return
     */
    @Override
    public Long zSetReverseRank(String key, Object object) {
        return null;
    }

    /**
     * Get set of between {@code start} and {@code end} from sorted set.
     *
     * @param key
     * @param start
     * @param end
     * @param valueType
     * @return
     */
    @Override
    public <T> Map<T, Double> zSetRangeWithScores(String key, long start, long end, Class<T> valueType) {
        return null;
    }

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
    @Override
    public <T> Map<T, Double> zSetRangeByScoreWithScores(String key, double min, double max, long offset, long count, Class<T> valueType) {
        return null;
    }

    /**
     * Get set of  in range from {@code start} to {@code end} where score is between {@code min} and
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
    @Override
    public <T> Map<T, Double> zSetReverseRangeByScoreWithScores(String key, double min, double max, long offset, long count, Class<T> valueType) {
        return null;
    }

    /**
     * Count number of elements within sorted set with scores between {@code min} and {@code max}.
     *
     * @param key
     * @param min
     * @param max
     * @return
     */
    @Override
    public Long zSetCount(String key, double min, double max) {
        return null;
    }

    /**
     * Get the size of sorted set with {@code key}.
     *
     * @param key
     * @return
     */
    @Override
    public Long zSetCard(String key) {
        return null;
    }

    /**
     * Get the score of element with {@code value} from sorted set with key {@code key}.
     *
     * @param key
     * @param object
     * @return
     */
    @Override
    public Double zSetScore(String key, Object object) {
        return null;
    }

    /**
     * Remove elements in range between {@code start} and {@code end} from sorted set with {@code key}.
     *
     * @param key
     * @param start
     * @param end
     * @return
     */
    @Override
    public Long zSetRemoveRange(String key, long start, long end) {
        return null;
    }

    /**
     * Remove elements with scores between {@code min} and {@code max} from sorted set with {@code key}.
     *
     * @param key
     * @param min
     * @param max
     * @return
     */
    @Override
    public Long zSetRemoveByScore(String key, double min, double max) {
        return null;
    }

    /**
     * Set {@code value} for {@code key}.
     *
     * @param key
     * @param value
     */
    @Override
    public void stringSet(String key, String value) {

    }

    /**
     * Set the {@code value} and expiration {@code timeout} for {@code key}.
     *
     * @param key
     * @param value
     * @param timeout
     * @param unit
     */
    @Override
    public void stringSet(String key, String value, long timeout, TimeUnit unit) {

    }

    /**
     * Set {@code key} to hold the string {@code value} if {@code key} is absent.
     *
     * @param key
     * @param value
     * @return
     */
    @Override
    public Boolean stringSetIfAbsent(String key, String value) {
        return null;
    }

    /**
     * Set {@code key} to hold the string {@code value} and expiration {@code timeout} if {@code key} is absent.
     *
     * @param key
     * @param value
     * @param timeout
     * @param unit
     * @return
     */
    @Override
    public Boolean stringSetIfAbsent(String key, String value, long timeout, TimeUnit unit) {
        return null;
    }

    /**
     * Set multiple keys to multiple values using key-value pairs provided in {@code tuple}.
     *
     * @param map
     */
    @Override
    public void stringMultiSet(Map<String, String> map) {

    }

    /**
     * Set multiple keys to multiple values using key-value pairs provided in {@code tuple} only if the provided key does
     * not exist.
     *
     * @param map
     * @return
     */
    @Override
    public Boolean stringMultiSetIfAbsent(Map<String, String> map) {
        return null;
    }

    /**
     * 默认返回String值
     *
     * @param key
     * @return
     */
    @Override
    public String stringGet(String key) {
        return null;
    }

    /**
     * Get multiple {@code keys}. Values are returned in the order of the requested keys.
     *
     * @param keys
     * @return
     */
    @Override
    public List<String> stringMultiGet(String... keys) {
        return null;
    }

    /**
     * Increment an integer value stored as string value under {@code key} by {@code delta}.
     *
     * @param key
     * @param delta
     * @return
     */
    @Override
    public Long stringIncrement(String key, long delta) {
        return null;
    }

    /**
     * Decrement an integer value stored as string value under {@code key} by {@code delta}.
     *
     * @param key
     * @param delta
     * @return
     */
    @Override
    public Long stringDecrement(String key, long delta) {
        return null;
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
     * @param requestPath
     * @param requestBean
     * @return
     */
    private ResponseBean call(RequestPath requestPath, RequestBean requestBean){
        Request request = null;
        ResponseBean responseBody = null;
        try {
            request = new Request.Builder().
                    url(urlPath + requestPath.getValue()).
                    post(RequestBody.create(JSON_TYPE,objectMapper.writeValueAsString(requestBean)))
                    .build();
            Response response = okHttpClient.newCall(request).execute();
            // 请求成功
            if(response.isSuccessful()){
                responseBody = objectMapper.readValue(response.body().string(),ResponseBean.class);
            }
        } catch (IOException e) {
            log.warn("本次调用失败,地址：{},参数:{},原因:{}",request.url(),request.body(),e.getMessage());
        }
        return responseBody;
    }
    private OkHttpClient okHttpClient;
    private String urlPath;
    private ObjectMapper objectMapper;

}
