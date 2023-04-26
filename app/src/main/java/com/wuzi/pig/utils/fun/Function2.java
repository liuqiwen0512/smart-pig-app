package com.wuzi.pig.utils.fun;

/**
 * 处理数据的回调
 */
public interface Function2<T1, T2> {
    void action(T1 var1, T2 var2);
}
