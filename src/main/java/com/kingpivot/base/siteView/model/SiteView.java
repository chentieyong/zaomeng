package com.kingpivot.base.siteView.model;

import com.kingpivot.common.model.BaseModel;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 * 网站访问
 */
@Entity
@Table(name = "site_view")
public class SiteView extends BaseModel<String> {
    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    @Column(length = 36)
    private String id;//主键

    @Column(length = 36)
    private String applicationID;//应用id

    //IP地址
    @Column(length = 100)
    private String ip;

    //来路url
    @Column(length = 100)
    private String url;

    //操作系统
    @Column(length = 200)
    private String operatingSystem;//操作系统

    //浏览器
    @Column(length = 200)
    private String browserType;//浏览器类型

    //地址
    @Column(length = 200)
    private String address;

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

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getOperatingSystem() {
        return operatingSystem;
    }

    public void setOperatingSystem(String operatingSystem) {
        this.operatingSystem = operatingSystem;
    }

    public String getBrowserType() {
        return browserType;
    }

    public void setBrowserType(String browserType) {
        this.browserType = browserType;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
