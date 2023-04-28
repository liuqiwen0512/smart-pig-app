package com.wuzi.pig.utils;

import android.text.TextUtils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class StringUtils {

    public static String nullToString(String s) {
        return s == null ? "" : s;
    }

    public static String emptyToString(String s, String placeholder) {
        return isEmpty(s) ? placeholder : s;
    }

    public static String emptyToString(String s) {
        return emptyToString(s, "");
    }

    public static String nullToString(int i) {
        return i == 0 ? "" : String.valueOf(i);
    }

    public static String nullToString(long i) {
        return i == 0 ? "" : String.valueOf(i);
    }

    public static String valueOf(Object s) {
        return s == null ? "" : s.toString();
    }

    public static boolean contains(String s, String k) {
        if (isEmpty(s)) {
            return false;
        }
        return s.contains(k);
    }

    public static boolean equalsNoEmpty(String s1, String s2) {
        return !isEmpty(s1) && !isEmpty(s2) && s1.equals(s2);
    }

    public static boolean equals(String s1, String s2) {
        if (s1 == s2) {
            return true;
        }
        if (s1 != null) {
            return s1.equals(s2);
        }
        if (s2 != null) {
            return s2.equals(s1);
        }
        return false;
    }

    public static boolean equals(Object s1, Object s2) {
        return equals(String.valueOf(s1), String.valueOf(s2));
    }

    public static boolean isEmpty(String s) {
        return s == null || s.isEmpty();
    }

    public static String ASCII16ToString(String text) {
        if (text == null) {
            return "";
        }
        if (!text.contains(",")) {
            return text;
        }
        StringBuffer buffer = new StringBuffer();
        String[] split = text.split(",");
        for (String item : split) {
            buffer.append((char) Integer.parseInt(item, 16));
        }
        return buffer.toString();
    }

    public static String md5(String input) {
        if (TextUtils.isEmpty(input)) {
            return "";
        }
        MessageDigest md5 = null;
        try {
            md5 = MessageDigest.getInstance("MD5");
            byte[] bytes = md5.digest(input.getBytes());
            String result = "";
            for (byte b : bytes) {
                String temp = Integer.toHexString(b & 0xff);
                if (temp.length() == 1) {
                    temp = "0" + temp;
                }
                result += temp;
            }
            return result;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String md5Password(String pwd) {
        String md5Pwd = md5(pwd);
        char[] chars = md5Pwd.toCharArray();
        String reverseStr = "";
        for (int i = chars.length - 1; i >= 0; i--) {
            reverseStr += chars[i];
        }
        return reverseStr;
    }
}
