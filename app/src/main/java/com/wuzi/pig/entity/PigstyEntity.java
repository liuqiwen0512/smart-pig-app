package com.wuzi.pig.entity;

/*
{
 猪栏创建
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
    监测数据
    "pigfarmId": "1",
    "num": 0,
    "temperature": 46.67,
    "pigstyName": "fdsfds",
    "movement": 637

* */
public class PigstyEntity {

    private String pigstyId;
    private String pigfarmId;
    private String pigstyName;
    private String baseStation;
    private int num;
    private float temperature;
    private float movement;

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

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public float getTemperature() {
        return temperature;
    }

    public void setTemperature(float temperature) {
        this.temperature = temperature;
    }

    public float getMovement() {
        return movement;
    }

    public void setMovement(float movement) {
        this.movement = movement;
    }
}
