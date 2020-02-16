package com.kingpivot.base.smsTemplate.model;

import com.kingpivot.common.model.BaseModel;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 * Created by Administrator on 2016/9/18.
 */
@Entity
@Table(name = "smsTemplate")
public class SmsTemplate extends BaseModel<String> {
    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    @Column(length = 36)
    private String id;//主键

    @Column(length = 36)
    private String smsWayID;

    @Column(length = 100)
    private String name;//名称

    @Column(length = 100)
    private String accountID;

    @Column(length = 100)
    private String accountToken;

    @Column(length = 100)
    private String appId;

    @Column(length = 20)
    private String templateValue;

    @Column(length = 100)
    private String templateCode;//模板编号

    @Column(length = 200)
    private String textDefine;

    @Column(length = 100)
    private String description;//描述

    @Column(name = "parameterLength", columnDefinition = "int default 1")
    private int parameterLength = 1;

    @Column(length = 100)
    private String parameter1;//参数1

    @Column(length = 100)
    private String parameter2;//参数2

    @Column(length = 100)
    private String parameter3;//参数3

    @Override
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSmsWayID() {
        return smsWayID;
    }

    public void setSmsWayID(String smsWayID) {
        this.smsWayID = smsWayID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAccountID() {
        return accountID;
    }

    public void setAccountID(String accountID) {
        this.accountID = accountID;
    }

    public String getAccountToken() {
        return accountToken;
    }

    public void setAccountToken(String accountToken) {
        this.accountToken = accountToken;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getTemplateValue() {
        return templateValue;
    }

    public void setTemplateValue(String templateValue) {
        this.templateValue = templateValue;
    }

    public String getTemplateCode() {
        return templateCode;
    }

    public void setTemplateCode(String templateCode) {
        this.templateCode = templateCode;
    }

    public String getTextDefine() {
        return textDefine;
    }

    public void setTextDefine(String textDefine) {
        this.textDefine = textDefine;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getParameterLength() {
        return parameterLength;
    }

    public void setParameterLength(int parameterLength) {
        this.parameterLength = parameterLength;
    }

    public String getParameter1() {
        return parameter1;
    }

    public void setParameter1(String parameter1) {
        this.parameter1 = parameter1;
    }

    public String getParameter2() {
        return parameter2;
    }

    public void setParameter2(String parameter2) {
        this.parameter2 = parameter2;
    }

    public String getParameter3() {
        return parameter3;
    }

    public void setParameter3(String parameter3) {
        this.parameter3 = parameter3;
    }
}
