package com.kingpivot.api.dto.signin;

import com.kingpivot.common.utils.TimeTest;

import java.sql.Timestamp;

public class SigninListDto {
    private String id;//主键
    private String name;//名称
    private Timestamp operateTime;
    private String operateTimeStr;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Timestamp getOperateTime() {
        return operateTime;
    }

    public void setOperateTime(Timestamp operateTime) {
        this.operateTime = operateTime;
        if(operateTime!=null){
            this.operateTimeStr = TimeTest.toDateTimeFormat(operateTime);
        }
    }

    public String getOperateTimeStr() {
        return operateTimeStr;
    }

    public void setOperateTimeStr(String operateTimeStr) {
        this.operateTimeStr = operateTimeStr;
    }
}
