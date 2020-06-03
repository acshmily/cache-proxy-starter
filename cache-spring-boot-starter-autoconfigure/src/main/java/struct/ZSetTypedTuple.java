package struct;

import org.springframework.lang.Nullable;

/**
 * @Author: Huanghz
 * @Description: 抽取可排序定义
 * @Date:Created in 8:52 上午 2020/6/3
 * @ModifyBy:
 **/
public interface ZSetTypedTuple<T> extends Comparable<ZSetTypedTuple<T>> {
    @Nullable
    T getValue();

    @Nullable
    Double getScore();
}
