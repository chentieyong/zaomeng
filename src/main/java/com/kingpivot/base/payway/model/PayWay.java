package com.kingpivot.base.payway.model;

import com.kingpivot.common.model.BaseModel;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Table(name = "payway")
public class PayWay extends BaseModel<String> {
    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    @Column(length = 36)
    private String id;//主键
    @Column(length = 100)
    private String name;//名称
    @Column(length = 20)
    private String shortName;//简称
    @Column(length = 20)
    private String account;//公司账户
    @Column(length = 100)
    private String partnerID;//商户编号
    @Column(length = 100)
    private String serverPassword;//连接密码
    @Column(length = 2000)
    private String commonKey;//银通公钥
    @Column(length = 2000)
    private String privateKey;//商户私钥
    @Column(length = 10)
    private String signType;//签名方式
    @Column(length = 100)
    private String returnURL;//返回地址
    @Column(length = 100)
    private String applicationID;//应用ID
    @Column(length = 100)
    private String orderNotifyURL;//订单通知地址
    @Column(length = 100)
    private String rechargeNotifyURL;//充值通知地址
    @Column()
    private Integer supportType;//供应商类型
    @Column(length=20)
    private String securityType;//加密方式

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

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPartnerID() {
        return partnerID;
    }

    public void setPartnerID(String partnerID) {
        this.partnerID = partnerID;
    }

    public String getServerPassword() {
        return serverPassword;
    }

    public void setServerPassword(String serverPassword) {
        this.serverPassword = serverPassword;
    }

    public String getCommonKey() {
        return commonKey;
    }

    public void setCommonKey(String commonKey) {
        this.commonKey = commonKey;
    }

    public String getPrivateKey() {
        return privateKey;
    }

    public void setPrivateKey(String privateKey) {
        this.privateKey = privateKey;
    }

    public String getSignType() {
        return signType;
    }

    public void setSignType(String signType) {
        this.signType = signType;
    }

    public String getReturnURL() {
        return returnURL;
    }

    public void setReturnURL(String returnURL) {
        this.returnURL = returnURL;
    }

    public String getApplicationID() {
        return applicationID;
    }

    public void setApplicationID(String applicationID) {
        this.applicationID = applicationID;
    }

    public String getOrderNotifyURL() {
        return orderNotifyURL;
    }

    public void setOrderNotifyURL(String orderNotifyURL) {
        this.orderNotifyURL = orderNotifyURL;
    }

    public String getRechargeNotifyURL() {
        return rechargeNotifyURL;
    }

    public void setRechargeNotifyURL(String rechargeNotifyURL) {
        this.rechargeNotifyURL = rechargeNotifyURL;
    }

    public Integer getSupportType() {
        return supportType;
    }

    public void setSupportType(Integer supportType) {
        this.supportType = supportType;
    }

    public String getSecurityType() {
        return securityType;
    }

    public void setSecurityType(String securityType) {
        this.securityType = securityType;
    }
}
