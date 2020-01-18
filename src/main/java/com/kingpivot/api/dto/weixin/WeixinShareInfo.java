package com.kingpivot.api.dto.weixin;

/**
 * Created by jinshu on 2016/11/9.
 */
public class WeixinShareInfo {

    private String url;
    private String appId;
    private String timestamp;
    private String nonceStr;
    private String signature;
    private String jsApiList;
    private String ticket;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getNonceStr() {
        return nonceStr;
    }

    public void setNonceStr(String nonceStr) {
        this.nonceStr = nonceStr;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getJsApiList() {
        return jsApiList;
    }

    public void setJsApiList(String jsApiList) {
        this.jsApiList = jsApiList;
    }

    public String getTicket() {
        return ticket;
    }

    public void setTicket(String ticket) {
        this.ticket = ticket;
    }

}
