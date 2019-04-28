package com.kingpivot.base.smsWay.model;

import com.kingpivot.common.model.BaseModel;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 * Created by Administrator on 2016/9/18.
 */
@Entity
@Table(name = "smsWay")
public class SmsWay extends BaseModel<String> {
    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    @Column(length = 36)
    private String id;//主键

    @Column(length = 100)
    private String name;//名称

    @Column(length = 100)
    private String shortName;//简称

    @Column(length = 36)
    private String companyID;

    @Column(name = "dayNumberTimes", columnDefinition = "int default 5")
    private Integer dayNumberTimes;

    @Column(name = "intervalMinute", columnDefinition = "int default 5")
    private Integer intervalMinute;

    @Column(length = 50)
    private String account;

    @Column(length = 50)
    private String pwd;

    @Column(length = 20)
    private String signName;

    @Column(name = "isTest", columnDefinition = "int default 0")
    private Integer isTest;

    @Column(name = "smsType", columnDefinition = "int default 1")
    private Integer smsType;//1阿里云

    @Override
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

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public String getCompanyID() {
        return companyID;
    }

    public void setCompanyID(String companyID) {
        this.companyID = companyID;
    }

    public Integer getDayNumberTimes() {
        return dayNumberTimes;
    }

    public void setDayNumberTimes(Integer dayNumberTimes) {
        this.dayNumberTimes = dayNumberTimes;
    }

    public Integer getIntervalMinute() {
        return intervalMinute;
    }

    public void setIntervalMinute(Integer intervalMinute) {
        this.intervalMinute = intervalMinute;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public Integer getIsTest() {
        return isTest;
    }

    public void setIsTest(Integer isTest) {
        this.isTest = isTest;
    }

    public Integer getSmsType() {
        return smsType;
    }

    public void setSmsType(Integer smsType) {
        this.smsType = smsType;
    }

    public String getSignName() {
        return signName;
    }

    public void setSignName(String signName) {
        this.signName = signName;
    }
}
