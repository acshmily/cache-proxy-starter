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
     *
     * @param o
     * @return
     */
    public static Long convertToLong(Object o) {
        if (isNull(o))
            return null;
        String stringToConvert = String.valueOf(o);
        return Long.parseLong(stringToConvert);
    }

    /**
     * object 转 boolean
     *
     * @param o
     * @return
     */
    public static Boolean convertToBoolean(Object o) {
        if (isNull(o))
            return null;
        return Boolean.parseBoolean(String.valueOf(o));
    }

    /**
     * object 转 double
     *
     * @param o
     * @return
     */
    public static Double convertToDouble(Object o) {
        if (isNull(o))
            return null;
        return Double.parseDouble(String.valueOf(o));
    }

    /**
     * object 转 string
     *
     * @param o
     * @return
     */
    public static String convertToString(Object o) {
        if (isNull(o))
            return null;
        return String.valueOf(o);
    }

    /**
     * 判断是否为null
     *
     * @param obj
     * @return
     */
    public static boolean isNull(Object obj) {
        return null == obj || obj.equals(null);
    }
}
