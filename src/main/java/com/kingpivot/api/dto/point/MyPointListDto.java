package com.kingpivot.api.dto.point;

import com.kingpivot.common.utils.TimeTest;

import java.sql.Timestamp;

public class MyPointListDto {
    private Timestamp operateTime;//操作时间
    private String operateTimeStr;//操作时间字符串
    private String action;//动作
    private int actionType = 0;//方向 1获取 2消费 10别人赠我 11我赠别人
    private int number = 0;//个数

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

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public int getActionType() {
        return actionType;
    }

    public void setActionType(int actionType) {
        this.actionType = actionType;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }
}
