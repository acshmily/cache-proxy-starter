package io.acshmily.cachetemplate.client.emun;

/**
 * Author: Huanghz
 * Description: 请求path
 * Date:Created in 3:17 下午 2020/6/3
 * ModifyBy:
 **/
public enum RequestPath {
    HASH("/hash/operate"),
    COMMON("/common/operate"),
    LIST("/list/operate"),
    SET("/set/operate"),
    STRING("/string/operate"),
    ZSET("/zset/operate");

    RequestPath(String value) {
        this.value = value;
    }

    private String value;

    public String getValue() {
        return value;
    }
}
