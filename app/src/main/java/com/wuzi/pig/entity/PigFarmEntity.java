package com.wuzi.pig.entity;

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
}
