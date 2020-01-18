package com.kingpivot.api.dto.weixin;

import com.kingpivot.common.utils.JacksonHelper;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.util.*;

/**
 * Created by Ma-Company on 2016/10/27.
 * 微信分享工具类
 */
public class WeiXinJSUtils {

    public static final String CHARSET = "UTF-8";

    public static WeiXinJsApiTicket getTicket(String access_token){
        Map<String,String> param = new HashMap<>();
        param.put("type","jsapi");
        param.put("access_token",access_token);
        WeiXinJsApiTicket weiXinJsApiTicket=null;
        try {
            String responseStr = HttpUtil.doGet(WeiXinUrlConstants.GET_TICKET_URL, param);
            weiXinJsApiTicket = JacksonHelper.fromJson(responseStr, WeiXinJsApiTicket.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return weiXinJsApiTicket;
    }

    /**
     * 创建md5摘要,规则是:按参数名称a-z排序,遇到空值的参数不参加签名。 sign
     */
    public static String createSign(Map<String, Object> map) {
        map.remove("sign");
        SortedMap<String, String> packageParams = new TreeMap<>();
        for (Map.Entry<String, Object> m : map.entrySet()) {
            if (null != m.getValue()) {
                packageParams.put(m.getKey(), m.getValue().toString());
            }
        }

        StringBuffer sb = new StringBuffer();
        Set<?> es = packageParams.entrySet();
        Iterator<?> it = es.iterator();
        while (it.hasNext()) {
            Map.Entry entry = (Map.Entry) it.next();
            String k = (String) entry.getKey();
            String v = (String) entry.getValue();
            if (!StringUtils.isEmpty(v) && !"sign".equals(k) && !"key".equals(k)) {
                sb.append(k + "=" + v + "&");
            }
        }

        String sbString = sb.toString();

        if(sbString.lastIndexOf("&")>0){
            sbString = sbString.substring(0,sbString.length()-1);
        }

        System.out.println("sb.toString()="+sbString);
        String sign = SHA1Util.SHA1Encode(sbString);
        System.out.println("sign="+sign);
        return sign;
    }

    public static String[] getJSApiList(){
        return new String[]{
                "checkJsApi",
                "onMenuShareTimeline",
                "onMenuShareAppMessage",
                "onMenuShareQQ",
                "onMenuShareWeibo",
                "hideMenuItems",
                "showMenuItems",
                "hideAllNonBaseMenuItem",
                "showAllNonBaseMenuItem",
                "translateVoice",
                "startRecord",
                "stopRecord",
                "onRecordEnd",
                "playVoice",
                "pauseVoice",
                "stopVoice",
                "uploadVoice",
                "downloadVoice",
                "chooseImage",
                "previewImage",
                "uploadImage",
                "downloadImage",
                "getNetworkType",
                "openLocation",
                "getLocation",
                "hideOptionMenu",
                "showOptionMenu",
                "closeWindow",
                "scanQRCode",
                "chooseWXPay",
                "openProductSpecificView",
                "addCard",
                "chooseCard",
                "openCard"};
    }

}
