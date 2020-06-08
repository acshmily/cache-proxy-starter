package cn.besttone.cachetemplate.autoconfigure.bean;

import lombok.Data;

/**
 * @Author: Huanghz
 * @Description: 返回Bean
 * @Date:Created in 12:50 下午 2020/6/8
 * @ModifyBy:
 **/
@Data
public class ResponseBean {
    String code;
    String message;
    String data;
}
