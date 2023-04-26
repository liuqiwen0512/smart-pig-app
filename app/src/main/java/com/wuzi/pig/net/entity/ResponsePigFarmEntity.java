package com.wuzi.pig.net.entity;

import com.wuzi.pig.entity.PigFarmEntity;

import java.util.List;

public class ResponsePigFarmEntity {

    private int code;
    private String msg;
    private int total;
    private List<PigFarmEntity> rows;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<PigFarmEntity> getRows() {
        return rows;
    }

    public void setRows(List<PigFarmEntity> rows) {
        this.rows = rows;
    }
}
