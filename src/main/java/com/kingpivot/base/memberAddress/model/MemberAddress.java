package com.kingpivot.base.memberAddress.model;

import com.kingpivot.base.city.model.City;
import com.kingpivot.common.model.BaseModel;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Table(name = "memberAddress")
public class MemberAddress extends BaseModel<String> {
    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    @Column(length = 36)
    private String id;//会员地址ID

    @Column(length = 36)
    private String applicationID;//应用ID

    @Column(length = 100)
    private String name;//详细地址

    @Column(length = 20)
    private String shortName;//地址别名

    @Column(length = 36)
    private String shengID;//省id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shengID", insertable = false, updatable = false)  //不能保存和修改
    private City shengCity;

    @Column(length = 36)
    private String shiID;//市id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shiID", insertable = false, updatable = false)  //不能保存和修改
    private City shiCity;

    @Column(length = 36)
    private String xianID;//县id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "xianID", insertable = false, updatable = false)  //不能保存和修改
    private City xianCity;

    @Column(name = "addTye", columnDefinition = "int default 0")
    private int addTye = 0;//地址类别

    @Column(length = 36)
    private String memberID;//会员ID

    @Column(name = "orderSeq", columnDefinition = "int default 0")
    private int orderSeq;//排序

    @Column(length = 100)
    private String contactName;//联系人

    @Column(length = 100)
    private String phone;//联系电话

    @Column(name = "isDefault", columnDefinition = "int default 0")
    private int isDefault = 0;//是否默认

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

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public String getShengID() {
        return shengID;
    }

    public void setShengID(String shengID) {
        this.shengID = shengID;
    }

    public City getShengCity() {
        return shengCity;
    }

    public void setShengCity(City shengCity) {
        this.shengCity = shengCity;
    }

    public String getShiID() {
        return shiID;
    }

    public void setShiID(String shiID) {
        this.shiID = shiID;
    }

    public City getShiCity() {
        return shiCity;
    }

    public void setShiCity(City shiCity) {
        this.shiCity = shiCity;
    }

    public String getXianID() {
        return xianID;
    }

    public void setXianID(String xianID) {
        this.xianID = xianID;
    }

    public City getXianCity() {
        return xianCity;
    }

    public void setXianCity(City xianCity) {
        this.xianCity = xianCity;
    }

    public int getAddTye() {
        return addTye;
    }

    public void setAddTye(int addTye) {
        this.addTye = addTye;
    }

    public String getMemberID() {
        return memberID;
    }

    public void setMemberID(String memberID) {
        this.memberID = memberID;
    }

    public int getOrderSeq() {
        return orderSeq;
    }

    public void setOrderSeq(int orderSeq) {
        this.orderSeq = orderSeq;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getIsDefault() {
        return isDefault;
    }

    public void setIsDefault(int isDefault) {
        this.isDefault = isDefault;
    }
}

