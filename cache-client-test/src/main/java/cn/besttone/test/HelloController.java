package cn.besttone.test;

import cn.besttone.cachetemplate.autoconfigure.service.CacheTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.concurrent.TimeUnit;


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
    String test() throws IOException {
       return cacheTemplate.get("2");
    }
    @Resource
    private CacheTemplate cacheTemplate;

}
