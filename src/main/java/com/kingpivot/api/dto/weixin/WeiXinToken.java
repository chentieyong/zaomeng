package com.kingpivot.api.dto.weixin;

/**
 * Created by Ma-Company on 2016/9/5.
 */
public class WeiXinToken {

    private String access_token;//获取到的凭证

    private Long expires_in;//凭证有效时间，单位：秒

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public Long getExpires_in() {
        return expires_in;
    }

    public void setExpires_in(Long expires_in) {
        this.expires_in = expires_in;
    }
}
