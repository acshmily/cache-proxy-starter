package io.acshmily.cachetemplate.client.bean;

/**
 * Author: Huanghz
 * Description: 返回Bean
 * Date:Created in 12:50 下午 2020/6/8
 * ModifyBy:
 **/

public class ResponseBean {
    private String code;
    private String message;
    private Object data;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
