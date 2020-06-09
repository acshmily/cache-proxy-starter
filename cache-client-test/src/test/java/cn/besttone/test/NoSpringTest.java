package cn.besttone.test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

/**
 * @Author: Huanghz
 * @Description: TODO
 * @Date:Created in 12:11 下午 2020/6/9
 * @ModifyBy:
 **/
public class NoSpringTest {

    @Test
    void test() throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        System.out.println(objectMapper.writeValueAsString("123"));
    }
}
