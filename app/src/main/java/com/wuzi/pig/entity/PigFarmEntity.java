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

    public void setPigFarmEntity(PigFarmEntity resEntity) {
        setPigfarmId(resEntity.getPigfarmId());
        setPigfarmName(resEntity.getPigfarmName());
    }

    public PigFarmEntity clone() {
        PigFarmEntity entity = new PigFarmEntity();
        entity.setPigfarmId(pigfarmId);
        entity.setPigfarmName(pigfarmName);
        return entity;
    }

    public static PigFarmEntity copy(PigFarmEntity resEntity, PigFarmEntity dstEntity) {
        if (resEntity == null) {
            return null;
        }
        if (dstEntity == null) {
            dstEntity = new PigFarmEntity();
        }
        dstEntity.setPigFarmEntity(resEntity);
        return dstEntity;
    }

    public static boolean equals(PigFarmEntity entity1, PigFarmEntity entity2) {
        if (entity1 == null || entity2 == null) {
            return false;
        }
        if (entity1 == entity2) {
            return true;
        }
        return StringUtils.equals(entity1.getPigfarmId(), entity2.getPigfarmId());
    }
}
