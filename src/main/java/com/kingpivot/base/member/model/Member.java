package com.kingpivot.base.member.model;

import com.kingpivot.base.site.model.Site;
import com.kingpivot.common.model.BaseModel;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "member")
public class Member extends BaseModel<String> implements Serializable {

    private static final long serialVersionUID = -1300306668752973060L;

    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    @Column(length = 36)
    private String id;//主键

    @Column(length = 36)
    private String applicationID;//应用ID

    @Column(length = 36)
    private String companyID;//公司ID 未实现

    @Column(length = 100)
    private String name;//名称

    @Column(length = 50)
    private String shortName;//简称

    @Column(length = 50)
    private String shortDescription;

    @Column(length = 500)
    private String description;

    @Column(length = 50)
    private String loginName;//登录名

    @Column(length = 50)
    private String loginPassword;//登录密码

    @Column(length = 100)
    private String weixin;

    @Column(length = 100)
    private String companyName;

    @Column(length = 100)
    private String jobName;

    @Column(length = 36)
    private String phone;//手机号码

    @Column(length = 50)
    private String weixinToken;//微信unionid

    @Column(length = 36)
    private String weixinUnionID; //微信唯一ID

    @Column(length = 36)
    private String recommandID;//推荐人

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recommandID", insertable = false, updatable = false)
    private Member recommandMember;

    @Column(length = 36)
    private String recommandCode;//推荐码

    @Column(length = 2000)
    private String recommandChain;//推荐链

    @Column(length = 36)
    private String siteID;//站点ID
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "siteID", insertable = false, updatable = false)
    private Site site;

    @Column(length = 100)
    private String avatarURL;//头像URL

    @Column(length = 36)
    private String rankID;

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

    public String getCompanyID() {
        return companyID;
    }

    public void setCompanyID(String companyID) {
        this.companyID = companyID;
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

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getLoginPassword() {
        return loginPassword;
    }

    public void setLoginPassword(String loginPassword) {
        this.loginPassword = loginPassword;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getWeixinToken() {
        return weixinToken;
    }

    public void setWeixinToken(String weixinToken) {
        this.weixinToken = weixinToken;
    }

    public String getWeixinUnionID() {
        return weixinUnionID;
    }

    public void setWeixinUnionID(String weixinUnionID) {
        this.weixinUnionID = weixinUnionID;
    }

    public String getRecommandID() {
        return recommandID;
    }

    public void setRecommandID(String recommandID) {
        this.recommandID = recommandID;
    }

    public Member getRecommandMember() {
        return recommandMember;
    }

    public void setRecommandMember(Member recommandMember) {
        this.recommandMember = recommandMember;
    }

    public String getRecommandCode() {
        return recommandCode;
    }

    public void setRecommandCode(String recommandCode) {
        this.recommandCode = recommandCode;
    }

    public String getRecommandChain() {
        return recommandChain;
    }

    public void setRecommandChain(String recommandChain) {
        this.recommandChain = recommandChain;
    }

    public String getSiteID() {
        return siteID;
    }

    public void setSiteID(String siteID) {
        this.siteID = siteID;
    }

    public Site getSite() {
        return site;
    }

    public void setSite(Site site) {
        this.site = site;
    }

    public String getAvatarURL() {
        return avatarURL;
    }

    public void setAvatarURL(String avatarURL) {
        this.avatarURL = avatarURL;
    }

    public String getRankID() {
        return rankID;
    }

    public void setRankID(String rankID) {
        this.rankID = rankID;
    }

    public String getWeixin() {
        return weixin;
    }

    public void setWeixin(String weixin) {
        this.weixin = weixin;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
