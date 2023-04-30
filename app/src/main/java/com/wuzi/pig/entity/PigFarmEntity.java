package com.wuzi.pig.entity;

import com.wuzi.pig.utils.StringUtils;

/*
            "searchValue": null,
            "createBy": "admin",
            "createTime": "2023-04-22 19:59:02",
            "updateBy": "",
            "updateTime": null,
            "remark": null,
            "params": {},
            "pigfarmId": 1,
            "pigfarmName": "",
            "createId": "1",
            "updateId": ""
* */
public class PigFarmEntity {

    private String pigfarmId;
    private String pigfarmName;

    public String getPigfarmId() {
        return pigfarmId;
    }

    public void setPigfarmId(String pigfarmId) {
        this.pigfarmId = pigfarmId;
    }

    public String getPigfarmName() {
        return pigfarmName;
    }

    public void setPigfarmName(String pigfarmName) {
        this.pigfarmName = pigfarmName;
    }

    public static boolean equals(PigFarmEntity entity1, PigFarmEntity entity2) {
        if (entity1 == entity2) {
            return true;
        }
        if (entity1 == null || entity2 == null) {
            return false;
        }
        return StringUtils.equals(entity1.getPigfarmId(), entity2.getPigfarmId());
    }
}
