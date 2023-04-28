package com.wuzi.pig.constant;

import com.wuzi.pig.R;
import com.wuzi.pig.entity.MenuEntity;

import java.util.ArrayList;
import java.util.List;

public class MenuConstant {

    public final static String MAIN_MENU_HOME = "main_menu_home";
    public final static String MAIN_MENU_ALARM = "main_menu_alarm";
    public final static String MAIN_MENU_MONITOR = "main_menu_monitor";
    public final static String MAIN_MENU_MANAGE = "main_menu_manage";
    public final static String MAIN_MENU_MINE = "main_menu_mine";

    public static List<MenuEntity> getMainMenus() {
        List<MenuEntity> list = new ArrayList<>();
        MenuEntity entity = new MenuEntity();
        entity.key = MAIN_MENU_HOME;
        entity.iconRes = R.drawable.selector_main_menu_home;
        entity.label = "首页";
        list.add(entity);

        entity = new MenuEntity();
        entity.key = MAIN_MENU_ALARM;
        entity.iconRes = R.drawable.selector_main_menu_alarm;
        entity.label = "告警";
        list.add(entity);

        entity = new MenuEntity();
        entity.key = MAIN_MENU_MONITOR;
        entity.iconRes = R.drawable.selector_main_menu_monitor;
        entity.label = "监测";
        list.add(entity);

        entity = new MenuEntity();
        entity.key = MAIN_MENU_MANAGE;
        entity.iconRes = R.drawable.selector_main_menu_manage;
        entity.label = "管理";
        list.add(entity);

        entity = new MenuEntity();
        entity.key = MAIN_MENU_MINE;
        entity.iconRes = R.drawable.selector_main_menu_mine;
        entity.label = "我的";
        list.add(entity);

        return list;
    }

}
