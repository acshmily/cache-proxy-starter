package io.acshmily.cache.test.bean;

import jdk.nashorn.internal.objects.annotations.Constructor;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @Author: Huanghz
 * @Description: 用户测试
 * @Date:Created in 9:45 上午 2020/6/9
 * @ModifyBy:
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class User {
    private String username;
    private String password;
}
