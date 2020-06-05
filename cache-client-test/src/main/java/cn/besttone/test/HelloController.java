package cn.besttone.test;

import cn.besttone.cachetemplate.autoconfigure.service.CacheTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;


/**
 * @Author: Huanghz
 * @Description: TODO
 * @Date:Created in 2:55 下午 2020/6/5
 * @ModifyBy:
 **/
@RequestMapping("hello")
@RestController
public class HelloController {
    @RequestMapping
    String test(){
        return cacheTemplate.helloWord();
    }
    @Resource
    private CacheTemplate cacheTemplate;

}
