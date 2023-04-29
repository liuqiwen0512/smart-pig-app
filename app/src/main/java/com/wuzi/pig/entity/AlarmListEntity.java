package com.wuzi.pig.entity;

import java.util.List;

public class AlarmListEntity {

    private int total;
    private List<AlarmEntity> rows;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<AlarmEntity> getRows() {
        return rows;
    }

    public void setRows(List<AlarmEntity> rows) {
        this.rows = rows;
    }
}
