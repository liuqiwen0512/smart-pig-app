package com.wuzi.pig.utils.tools;

public class IntegerUtils {

    public static int parseInt(String str) {
        if(str == null || "".equals(str.trim())) {
            return 0;
        }
        try {
            return Integer.parseInt(str);
        } catch (Exception e) {

        }
        return 0;
    }

}
