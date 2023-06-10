package com.wuzi.pig.module.monitor.tools;

import com.wuzi.pig.utils.tools.TimeUtils;

import java.util.Calendar;

public class MonTools {

    public static String nicknameOfTime(Calendar calendar) {
        int diff = TimeUtils.deviationDays(calendar, Calendar.getInstance());
        if (diff == 0) {
            return "今天";
        } else if (diff == -1) {
            return "昨天";
        } else if (diff == -2) {
            return "前天";
        }
        return "";
    }

    public static String formatTime(Calendar calendar) {
        int month = calendar.get(Calendar.MONTH) + 1;
        int date = calendar.get(Calendar.DAY_OF_MONTH);
        return month + "月" + date + "日";
    }

    public static String getFormatWeek(Calendar calendar) {
        String[] weeks = new String[]{"周日", "周一", "周二", "周三", "周四", "周五", "周六"};
        int week = calendar.get(Calendar.DAY_OF_WEEK);
        return weeks[Math.max(week - 1, 0)];
    }

}
