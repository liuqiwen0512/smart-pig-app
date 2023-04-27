package com.wuzi.pig.entity;

import java.util.List;

public class PigstyListEntity {

    private int total;
    private List<PigstyEntity> rows;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<PigstyEntity> getRows() {
        return rows;
    }

    public void setRows(List<PigstyEntity> rows) {
        this.rows = rows;
    }
}
