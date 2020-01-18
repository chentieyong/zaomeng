package com.kingpivot.api.dto.weixin;

import com.kingpivot.common.utils.JacksonHelper;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Ma-Company on 2016/10/27.
 * 微信工具类
 */
public class WeiXinUtils {

    public static final String CHARSET = "UTF-8";

    public static WeiXinToken getToken(String appID,String secret){
        Map<String,String> param = new HashMap<>();
        param.put("grant_type","client_credential");
        param.put("appid",appID);
        param.put("secret",secret);
        WeiXinToken weiXinToken = null;
        try {
            String responseStr = HttpUtil.doGet(WeiXinUrlConstants.GET_TOKEN_URL, param);
            weiXinToken = JacksonHelper.fromJson(responseStr, WeiXinToken.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return weiXinToken;
    }

    /**
     * 随机16为数值
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

    public static String getTimestamp(){
        return String.format("%s", System.currentTimeMillis()).substring(0, 10);
    }
}
