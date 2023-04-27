package com.wuzi.pig.entity;

/*
{
  "baseStation": "string",
  "createBy": "string",
  "createId": "string",
  "createTime": "2023-04-26T18:43:34.665Z",
  "params": {},
  "pigfarmId": "string",
  "pigstyId": 0,
  "pigstyName": "string",
  "remark": "string",
  "searchValue": "string",
  "updateBy": "string",
  "updateId": "string",
  "updateTime": "2023-04-26T18:43:34.665Z"
}
* */
public class PigstyEntity {

    private String pigstyId;
    private String pigfarmId;
    private String pigstyName;
    private String baseStation;

    public String getPigstyId() {
        return pigstyId;
    }

    public void setPigstyId(String pigstyId) {
        this.pigstyId = pigstyId;
    }

    public String getPigfarmId() {
        return pigfarmId;
    }

    public void setPigfarmId(String pigfarmId) {
        this.pigfarmId = pigfarmId;
    }

    public String getPigstyName() {
        return pigstyName;
    }

    public void setPigstyName(String pigstyName) {
        this.pigstyName = pigstyName;
    }

    public String getBaseStation() {
        return baseStation;
    }

    public void setBaseStation(String baseStation) {
        this.baseStation = baseStation;
    }
}
