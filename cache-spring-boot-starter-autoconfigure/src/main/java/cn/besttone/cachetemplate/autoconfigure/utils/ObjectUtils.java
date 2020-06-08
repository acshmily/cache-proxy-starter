package cn.besttone.cachetemplate.autoconfigure.utils;

/**
 * @Author: Huanghz
 * @Description: Object工具类
 * @Date:Created in 1:14 下午 2020/6/8
 * @ModifyBy:
 **/
public class ObjectUtils {
    /**
     * object 转 long
     * @param o
     * @return
     */
    public static Long convertToLong(Object o){
        String stringToConvert = String.valueOf(o);
        return Long.parseLong(stringToConvert);
    }

    /**
     * object 转 boolean
     * @param o
     * @return
     */
    public static Boolean convertToBoolean(Object o){
        return Boolean.parseBoolean(String.valueOf(o));
    }
}
