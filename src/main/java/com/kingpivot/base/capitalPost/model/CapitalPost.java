package com.kingpivot.base.capitalPost.model;

import com.kingpivot.base.member.model.Member;
import com.kingpivot.common.model.BaseModel;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "capitalPost")
public class CapitalPost extends BaseModel<String> {
    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    @Column(length = 36)
    private String id;

    @Column()
    private String applicationID ;//应用ID

    @Column(length = 100)
    private String name;//名称

    @Column(length = 100)
    private String shortName;//简称

    @Column(length = 2000)
    private String description;//说明

    @Column(length = 36)
    private String memberID;//发布人
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "memberID", insertable = false, updatable = false)  //不能保存和修改
    private Member member;

    @Column()
    private Timestamp beginDate;//开始日期

    @Column()
    private Timestamp endDate;//结束日期

    @Column(name = "fromType", columnDefinition = "int default 1")
    private int fromType = 1;//资金类型 1股权投资2金融投资3其他投资

    @Column(name = "stageType", columnDefinition = "int default 1")
    private int stageType = 1;//发展阶段 1种子期 2初创期 3成长期 4稳健期

    @Column(name = "amountType", columnDefinition = "int default 50")
    private int amountType = 50;//投资资金 1:小余50w 2:50w-100w 3:100w-500w 4:500w以上

    @Column(length = 100)
    private String industrialName;//投资行业

    @Column(length = 100)
    private String zoneName;//投资地区

    @Column(name = "status", columnDefinition = "int default 1")
    private int status = 1;//1新

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

    public void setAmountType(int amountType) {
        this.amountType = amountType;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    public String getMemberID() {
        return memberID;
    }

    public void setMemberID(String memberID) {
        this.memberID = memberID;
    }

    public Timestamp getBeginDate() {
        return beginDate;
    }

    public void setBeginDate(Timestamp beginDate) {
        this.beginDate = beginDate;
    }

    public Timestamp getEndDate() {
        return endDate;
    }

    public void setEndDate(Timestamp endDate) {
        this.endDate = endDate;
    }

    public int getFromType() {
        return fromType;
    }

    public void setFromType(int fromType) {
        this.fromType = fromType;
    }

    public int getStageType() {
        return stageType;
    }

    public void setStageType(int stageType) {
        this.stageType = stageType;
    }

    public Integer getAmountType() {
        return amountType;
    }

    public void setAmountType(Integer amountType) {
        this.amountType = amountType;
    }

    public String getIndustrialName() {
        return industrialName;
    }

    public void setIndustrialName(String industrialName) {
        this.industrialName = industrialName;
    }

    public String getZoneName() {
        return zoneName;
    }

    public void setZoneName(String zoneName) {
        this.zoneName = zoneName;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
