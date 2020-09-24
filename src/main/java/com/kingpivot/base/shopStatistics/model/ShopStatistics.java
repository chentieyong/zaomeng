package com.kingpivot.base.shopStatistics.model;

import com.kingpivot.common.model.BaseModel;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Table(name = "shopStatistics")
public class ShopStatistics extends BaseModel<String> {
    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    @Column(length = 36)
    private String id;//主键

    @Column(length = 36)
    private String applicationID;//应用ID

    @Column(length = 100)
    private String name;//名称

    @Column(length = 36)
    private String shopID;//店铺ID

    private Double cashBalance = 0d;//现金余额"

    private Double cashTotalRecharge = 0d;//累计充值"

    @Override
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getApplicationID() {
        return applicationID;
    }

    public void setApplicationID(String applicationID) {
        this.applicationID = applicationID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getShopID() {
        return shopID;
    }

    public void setShopID(String shopID) {
        this.shopID = shopID;
    }

    public Double getCashBalance() {
        return cashBalance;
    }

    public void setCashBalance(Double cashBalance) {
        this.cashBalance = cashBalance;
    }

    public Double getCashTotalRecharge() {
        return cashTotalRecharge;
    }

    public void setCashTotalRecharge(Double cashTotalRecharge) {
        this.cashTotalRecharge = cashTotalRecharge;
    }
}
