package com.wuzi.pig.entity;

/*
{
            "earTag": "FF,FF,FF,EE",
            "createTime": "2023-04-25 12:51",
            "level": "低烧",
            "pigstyName": "fdsfds"
         }
* */
public class AlarmEntity {

    private String earTag;
    private String createTime;
    private String level;
    private String pigstyName;

    public String getEarTag() {
        return earTag;
    }

    public void setEarTag(String earTag) {
        this.earTag = earTag;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getPigstyName() {
        return pigstyName;
    }

    public void setPigstyName(String pigstyName) {
        this.pigstyName = pigstyName;
    }
}
