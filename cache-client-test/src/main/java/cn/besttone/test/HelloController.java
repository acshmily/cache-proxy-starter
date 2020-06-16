package cn.besttone.test;

import cn.besttone.cachetemplate.autoconfigure.service.CacheTemplate;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;
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
    List<Object> test() throws IOException {
       return cacheTemplate.listRange("list",0,-1,Object.class);
    }
    @Resource
    private CacheTemplate cacheTemplate;

    @Resource
    private ObjectMapper objectMapper;

}
