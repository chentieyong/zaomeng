package com.kingpivot.common.utils;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.cloopen.rest.sdk.CCPRestSmsSDK;
import com.kingpivot.base.smsTemplate.model.SmsTemplate;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;

public class SmsSendUtil {

    public static String aliyunSmsSend(SmsTemplate smsTemplate, String phone, String templateParam, String signName) throws ClientException {
        //设置超时时间-可自行调整
        System.setProperty("sun.net.client.defaultConnectTimeout", "10000");
        System.setProperty("sun.net.client.defaultReadTimeout", "10000");
        //初始化ascClient需要的几个参数
        final String product = "Dysmsapi";//短信API产品名称（短信产品名固定，无需修改）
        final String domain = "dysmsapi.aliyuncs.com";//短信API产品域名（接口地址固定，无需修改）
        //初始化ascClient,暂时不支持多region（请勿修改）
        IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", smsTemplate.getAccountID(),
                smsTemplate.getAccountToken());
        DefaultProfile.addEndpoint("cn-hangzhou", "cn-hangzhou", product, domain);
        IAcsClient acsClient = new DefaultAcsClient(profile);
        //组装请求对象
        SendSmsRequest request = new SendSmsRequest();
        //使用post提交
        request.setMethod(MethodType.POST);
        //必填:待发送手机号。支持以逗号分隔的形式进行批量调用，批量上限为1000个手机号码,批量调用相对于单条调用及时性稍有延迟,
        // 验证码类型的短信推荐使用单条调用的方式；发送国际/港澳台消息时，接收号码格式为国际区号+号码，如“85200000000”
        request.setPhoneNumbers(phone);
        //必填:短信签名-可在短信控制台中找到
        request.setSignName(signName);
        //必填:短信模板-可在短信控制台中找到，发送国际/港澳台消息时，请使用国际/港澳台短信模版
        request.setTemplateCode(smsTemplate.getTemplateCode());
        //友情提示:如果JSON中需要带换行符,请参照标准的JSON协议对换行符的要求,比如短信内容中包含\r\n的情况在JSON中需要表示成\\r\\n,否则会导致JSON在服务端解析失败
        request.setTemplateParam(templateParam);
        //请求失败这里会抛ClientException异常
        SendSmsResponse sendSmsResponse = acsClient.getAcsResponse(request);
        if (sendSmsResponse.getCode() != null && sendSmsResponse.getCode().equals("OK")) {
            //请求成功
            return "";
        } else {
            return sendSmsResponse.getMessage();
        }
    }

    public static String smsSendRongLian(SmsTemplate smsTemplate, String phone, String code) {
        HashMap result = null;
        String[] datas = new String[smsTemplate.getParameterLength()];
        datas[0] = code;
        if (StringUtils.isNotBlank(smsTemplate.getParameter1())) {
            datas[1] = smsTemplate.getParameter1();
        } else if (StringUtils.isNotBlank(smsTemplate.getParameter1())) {
            datas[2] = smsTemplate.getParameter2();
        } else if (StringUtils.isNotBlank(smsTemplate.getParameter1())) {
            datas[3] = smsTemplate.getParameter3();
        }
        CCPRestSmsSDK restAPI = new CCPRestSmsSDK();
        // 初始化服务器地址和端口，沙盒环境配置成sandboxapp.cloopen.com，生产环境配置成app.cloopen.com，端口都是8883.
        restAPI.init("app.cloopen.com", "8883");
        restAPI.setAccount(smsTemplate.getAccountID(), smsTemplate.getAccountToken());
        restAPI.setAppId(smsTemplate.getAppId());
        result = restAPI.sendTemplateSMS(phone, smsTemplate.getTemplateCode(), datas);
        System.out.println(result);
        if ("000000".equals(result.get("statusCode"))) {
            return "";
        } else {
            return result.get("statusMsg").toString();
        }
    }
}
