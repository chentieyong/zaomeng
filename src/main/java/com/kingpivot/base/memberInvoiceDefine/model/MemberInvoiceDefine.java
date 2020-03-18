package com.kingpivot.base.memberInvoiceDefine.model;

import com.kingpivot.base.member.model.Member;
import com.kingpivot.common.model.BaseModel;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Table(name = "memberInvoiceDefine")
public class MemberInvoiceDefine extends BaseModel<String> {
    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    @Column(length = 36)
    private String id;//主键

    @Column(length = 100)
    private String name;//发票名称

    @Column(length = 20)
    private String shortName;//发票别名

    @Column(length = 36)
    private String memberID;//会员ID
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "memberID", insertable = false, updatable = false)  //不能保存和修改
    private Member member;

    @Column(length = 36)
    private String applicationID;//应用ID

    @Column(name = "invoiceType", columnDefinition = "int default 1")
    private int invoiceType = 1;//发票类型 1个人2单位

    @Column(name = "taxType", columnDefinition = "int default 1")
    private int taxType = 1;//发票税种 1增值税普通发票2增值税专用发票

    @Column(name = "taxRate", columnDefinition = "int default 1")
    private int taxRate = 1;//发票税点

    @Column(name = "orderSeq", columnDefinition = "int default 1")
    private int orderSeq = 1;//排序

    @Column(length = 100)
    private String taxCode;//纳税人识别号

    @Column(length = 100)
    private String address;//地址

    @Column(length = 100)
    private String phone;//联系电话

    @Column(length = 100)
    private String bankName;//开户银行

    @Column(length = 100)
    private String bankAccount;//银行账户

    @Column(name = "isDefault", columnDefinition = "int default 0")
    private int isDefault = 0;//是否默认

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

    public String getMemberID() {
        return memberID;
    }

    public void setMemberID(String memberID) {
        this.memberID = memberID;
    }

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    public String getApplicationID() {
        return applicationID;
    }

    public void setApplicationID(String applicationID) {
        this.applicationID = applicationID;
    }

    public int getInvoiceType() {
        return invoiceType;
    }

    public void setInvoiceType(int invoiceType) {
        this.invoiceType = invoiceType;
    }

    public int getTaxType() {
        return taxType;
    }

    public void setTaxType(int taxType) {
        this.taxType = taxType;
    }

    public int getTaxRate() {
        return taxRate;
    }

    public void setTaxRate(int taxRate) {
        this.taxRate = taxRate;
    }

    public int getOrderSeq() {
        return orderSeq;
    }

    public void setOrderSeq(int orderSeq) {
        this.orderSeq = orderSeq;
    }

    public String getTaxCode() {
        return taxCode;
    }

    public void setTaxCode(String taxCode) {
        this.taxCode = taxCode;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getBankAccount() {
        return bankAccount;
    }

    public void setBankAccount(String bankAccount) {
        this.bankAccount = bankAccount;
    }

    public int getIsDefault() {
        return isDefault;
    }

    public void setIsDefault(int isDefault) {
        this.isDefault = isDefault;
    }
}
