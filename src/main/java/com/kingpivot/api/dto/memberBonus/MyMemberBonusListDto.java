package com.kingpivot.api.dto.memberBonus;

import com.kingpivot.common.utils.TimeTest;

import java.sql.Timestamp;

public class MyMemberBonusListDto {
    private String id;//主键
    private String name;
    private Double amount = 0.0;
    private Timestamp startDate;
    private String startDateStr;
    private Timestamp endDate;
    private String endDateStr;
    private Timestamp useTime;
    private String useTimeStr;
    private String printCode;
    private Integer status = 1;//1未使用，2已使用，3已过期

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

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Timestamp getStartDate() {
        return startDate;
    }

    public void setStartDate(Timestamp startDate) {
        this.startDate = startDate;
        if(startDate!=null){
            this.startDateStr = TimeTest.toDateFormat(startDate);
        }
    }

    public Timestamp getEndDate() {
        return endDate;
    }

    public void setEndDate(Timestamp endDate) {
        this.endDate = endDate;
        if(endDate!=null){
            this.endDateStr = TimeTest.toDateFormat(endDate);
        }
    }

    public Timestamp getUseTime() {
        return useTime;
    }

    public void setUseTime(Timestamp useTime) {
        this.useTime = useTime;
        if(useTime!=null){
            this.useTimeStr = TimeTest.toDateTimeFormat(useTime);
        }
    }

    public String getPrintCode() {
        return printCode;
    }

    public void setPrintCode(String printCode) {
        this.printCode = printCode;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getStartDateStr() {
        return startDateStr;
    }

    public void setStartDateStr(String startDateStr) {
        this.startDateStr = startDateStr;
    }

    public String getEndDateStr() {
        return endDateStr;
    }

    public void setEndDateStr(String endDateStr) {
        this.endDateStr = endDateStr;
    }

    public String getUseTimeStr() {
        return useTimeStr;
    }

    public void setUseTimeStr(String useTimeStr) {
        this.useTimeStr = useTimeStr;
    }
}
