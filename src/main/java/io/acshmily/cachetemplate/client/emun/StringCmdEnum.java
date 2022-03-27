package io.acshmily.cachetemplate.client.emun;

/**
 * Author: Huanghz
 * Description: String 类型操作
 * Date:Created in 4:18 下午 2020/6/5
 * ModifyBy:
 **/
public enum StringCmdEnum implements CacheTemplateCmd {
    set,setEx,setNx,setNxEx,multiSet,multiSetNx,get,multiGet,increment,decrement
}
