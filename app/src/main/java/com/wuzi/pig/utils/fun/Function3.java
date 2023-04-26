package com.wuzi.pig.utils.fun;

/**
 * 处理数据的回调
 */
public interface Function3<T1, T2, T3> {
    void action(T1 var1, T2 var2, T3 var3);
}
