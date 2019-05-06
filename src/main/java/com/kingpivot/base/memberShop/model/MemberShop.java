package com.kingpivot.base.memberShop.model;

import com.kingpivot.base.member.model.Member;
import com.kingpivot.common.model.BaseModel;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Table(name = "memberShop")
public class MemberShop extends BaseModel<String> {
    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    @Column(length = 36)
    private String id;//主键
    @Column(length = 36)
    private String memberID;
    @Column(length = 36)
    private String shopCategoryID;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "memberID", insertable = false, updatable = false)  //不能保存和修改
    private Member member;
    @Column(length = 36)
    private String applicationID;
    @Column(length = 100)
    private String name;//名称
    @Column()
    private Integer shopType = 0;//店铺类型: 0= 实体店铺,1= 个人微店,2=个人网店,3=O2O店铺
    @Column()
    private Integer bizStatus = 0;//1=正常2=休业3=停业4=准备中
    @Column(length = 100)
    private String logoURL;//logoURL
    @Column(length = 100)
    private String shopFaceImage;//店铺正面照
    @Column(length = 100)
    private String businessImage;//营业执照
    @Column(length = 50)
    private String address;//联系地址
    @Column(length = 20)
    private String contact;//联系人
    @Column(length = 20)
    private String contactPhone;//联系人手机
    @Column(length = 100)
    private String contactIdCardFaceImage;//联系人身份证正面
    @Column(length = 100)
    private String contactIdCardBackImage;//联系人身份证反面
    @Column
    private Integer verifyStatus = 0;//0未审核，1审核通过，2审核拒绝

    @Override
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMemberID() {
        return memberID;
    }

    public void setMemberID(String memberID) {
        this.memberID = memberID;
    }

    public String getShopCategoryID() {
        return shopCategoryID;
    }

    public void setShopCategoryID(String shopCategoryID) {
        this.shopCategoryID = shopCategoryID;
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

    public Integer getShopType() {
        return shopType;
    }

    public void setShopType(Integer shopType) {
        this.shopType = shopType;
    }

    public Integer getBizStatus() {
        return bizStatus;
    }

    public void setBizStatus(Integer bizStatus) {
        this.bizStatus = bizStatus;
    }

    public String getLogoURL() {
        return logoURL;
    }

    public void setLogoURL(String logoURL) {
        this.logoURL = logoURL;
    }

    public String getShopFaceImage() {
        return shopFaceImage;
    }

    public void setShopFaceImage(String shopFaceImage) {
        this.shopFaceImage = shopFaceImage;
    }

    public String getBusinessImage() {
        return businessImage;
    }

    public void setBusinessImage(String businessImage) {
        this.businessImage = businessImage;
    }

    public String getContactIdCardFaceImage() {
        return contactIdCardFaceImage;
    }

    public void setContactIdCardFaceImage(String contactIdCardFaceImage) {
        this.contactIdCardFaceImage = contactIdCardFaceImage;
    }

    public String getContactIdCardBackImage() {
        return contactIdCardBackImage;
    }

    public void setContactIdCardBackImage(String contactIdCardBackImage) {
        this.contactIdCardBackImage = contactIdCardBackImage;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getContactPhone() {
        return contactPhone;
    }

    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone;
    }

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    public Integer getVerifyStatus() {
        return verifyStatus;
    }

    public void setVerifyStatus(Integer verifyStatus) {
        this.verifyStatus = verifyStatus;
    }
}

