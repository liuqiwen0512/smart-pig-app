package com.wuzi.pig.entity;

/*
    "today": 0,
    "yesterday": 1,
    "befyesterday": 0,
    "healths": 1,
    "equipments": 0
* */
public class Statis72HourEntity {
    private int today;
    private int yesterday;
    private int befyesterday;
    private int healths;
    private int equipments;

    public int getToday() {
        return today;
    }

    public void setToday(int today) {
        this.today = today;
    }

    public int getYesterday() {
        return yesterday;
    }

    public void setYesterday(int yesterday) {
        this.yesterday = yesterday;
    }

    public int getBefyesterday() {
        return befyesterday;
    }

    public void setBefyesterday(int befyesterday) {
        this.befyesterday = befyesterday;
    }

    public int getHealths() {
        return healths;
    }

    public void setHealths(int healths) {
        this.healths = healths;
    }

    public int getEquipments() {
        return equipments;
    }

    public void setEquipments(int equipments) {
        this.equipments = equipments;
    }
}
