package cn.besttone.cachetemplate.autoconfigure.bean;

import cn.besttone.cachetemplate.autoconfigure.emun.CacheTemplateCmd;
import lombok.Data;

import java.util.LinkedList;
import java.util.List;

/**
 * @Author: Huanghz
 * @Description: 请求Bean
 * @Date:Created in 6:02 下午 2020/6/5
 * @ModifyBy:
 **/
@Data
public class RequestBean {
    private CacheTemplateCmd cmd;
    private LinkedList<Object> args;
}
