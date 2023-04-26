package com.wuzi.pig.utils.tools;

import java.util.List;

public class CollectionUtils {

    public static boolean isEmpty(List list) {
        return list == null || list.isEmpty();
    }

    public static int size(List list) {
        return list == null ? 0 : list.size();
    }

    public static <T> T findLastObject(List<T> list) {
        if (!isEmpty(list)) {
            return list.get(list.size() -1);
        }
        return null;
    }

}
