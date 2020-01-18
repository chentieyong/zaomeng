package com.kingpivot.base.wechart.model;

import com.kingpivot.base.application.model.Application;
import com.kingpivot.base.site.model.Site;
import com.kingpivot.common.model.BaseModel;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Table(name = "wechart")
public class Wechart extends BaseModel<String> {
    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    @Column(length = 36)
    private String id;//主键

    @Column(length = 100)
    private String name;

    @Column(length = 20)
    private String shortname;

    @Column(length = 200)
    private String description;

    @Column(length = 36)
    private String companyID;

    @Column(length = 36)
    private String applicationId;//应用ID"
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "applicationId", insertable = false, updatable = false)  //不能保存和修改
    private Application application;

    @Column(name = "type", columnDefinition = "int default 0")
    private int type = 0; //是否锁定 0公众号1小程序

    @Column(length = 36)
    private String siteID;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "siteID", insertable = false, updatable = false)  //不能保存和修改
    private Site site;

    @Column(length = 36)
    private String loginName;

    @Column
    private String loginPwd;//访问密码

    @Column(length = 32)
    private String PUBLIC_NO;

    @Column(length = 32)
    private String APPid;

    @Column(length = 100)
    private String APPsecret;

    @Column(length = 200)
    private String QRCODEPATH;

    @Column
    private String access_token;

    @Column(length = 36)
    private String token;

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

    public String getShortname() {
        return shortname;
    }

    public void setShortname(String shortname) {
        this.shortname = shortname;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCompanyID() {
        return companyID;
    }

    public void setCompanyID(String companyID) {
        this.companyID = companyID;
    }

    public String getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(String applicationId) {
        this.applicationId = applicationId;
    }

    public Application getApplication() {
        return application;
    }

    public void setApplication(Application application) {
        this.application = application;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
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

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getLoginPwd() {
        return loginPwd;
    }

    public void setLoginPwd(String loginPwd) {
        this.loginPwd = loginPwd;
    }

    public String getPUBLIC_NO() {
        return PUBLIC_NO;
    }

    public void setPUBLIC_NO(String PUBLIC_NO) {
        this.PUBLIC_NO = PUBLIC_NO;
    }

    public String getAPPid() {
        return APPid;
    }

    public void setAPPid(String APPid) {
        this.APPid = APPid;
    }

    public String getAPPsecret() {
        return APPsecret;
    }

    public void setAPPsecret(String APPsecret) {
        this.APPsecret = APPsecret;
    }

    public String getQRCODEPATH() {
        return QRCODEPATH;
    }

    public void setQRCODEPATH(String QRCODEPATH) {
        this.QRCODEPATH = QRCODEPATH;
    }

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
