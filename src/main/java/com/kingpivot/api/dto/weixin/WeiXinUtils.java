package com.kingpivot.api.dto.weixin;

import com.kingpivot.common.utils.JacksonHelper;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Ma-Company on 2016/10/27.
 * 微信工具类
 */
public class WeiXinUtils {

    public static final String CHARSET = "UTF-8";
    public static final String DEFAULT_APPID = "wx54a9fdc8e3903401";
    public static final String DEFAULT_APPSECRET = "a1946ea07aa4c109a379253efa6a765c";

    public static WeiXinToken getToken(String appID, String secret) {
        Map<String, String> param = new HashMap<>();
        param.put("grant_type", "client_credential");
        param.put("appid", appID == null ? DEFAULT_APPID : appID);
        param.put("secret", secret == null ? DEFAULT_APPSECRET : secret);
        WeiXinToken weiXinToken = null;
        try {
            String responseStr = HttpUtil.doGet(WeiXinUrlConstants.GET_TOKEN_URL, param);
            weiXinToken = JacksonHelper.fromJson(responseStr, WeiXinToken.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return weiXinToken;
    }

    //获取自定义二维码
    public static InputStream getwxacodeunlimit(String scene, String page, String appId, String secret) {
        WeiXinToken weixinToken = getToken(appId, secret);
        if (weixinToken == null) {
            return null;
        }
        String data = "{\"scene\": \"" + scene + "\", \"page\": \"" + page + "\", \"width\": \"200\"}";
        InputStream inputStream = HttpUtil.postWx(String.format("%s?access_token=%s",
                WeiXinUrlConstants.GET_WEIXINACODEUNLIMIT_URL,
                weixinToken.getAccess_token()), data);
        return inputStream;
    }

    /**
     * 随机16为数值
     *
     * @return
     */
    public static String buildRandom() {
        String currTime = TenpayUtil.getCurrTime();
        String strTime = currTime.substring(8, currTime.length());
        int num = 1;
        double random = Math.random();
        if (random < 0.1) {
            random = random + 0.1;
        }
        for (int i = 0; i < 4; i++) {
            num = num * 10;
        }
        return (int) ((random * num)) + strTime;
    }

    public static String getTimestamp() {
        return String.format("%s", System.currentTimeMillis()).substring(0, 10);
    }
}
