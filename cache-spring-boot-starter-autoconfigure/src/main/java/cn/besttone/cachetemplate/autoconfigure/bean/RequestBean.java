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

public class RequestBean {
    private CacheTemplateCmd cmd;
    private LinkedList<Object> args;

    public CacheTemplateCmd getCmd() {
        return cmd;
    }

    public void setCmd(CacheTemplateCmd cmd) {
        this.cmd = cmd;
    }

    public LinkedList<Object> getArgs() {
        return args;
    }

    public void setArgs(LinkedList<Object> args) {
        this.args = args;
    }
}
