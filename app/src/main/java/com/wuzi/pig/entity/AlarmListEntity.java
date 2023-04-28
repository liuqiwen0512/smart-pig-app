package com.wuzi.pig.entity;

import java.util.List;

public class AlarmListEntity {

    private int total;
    private List<PigFarmEntity> rows;

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
