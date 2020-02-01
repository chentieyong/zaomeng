package com.kingpivot.api.controller.ApiWechartController;

import com.google.common.collect.Maps;
import com.kingpivot.Result;
import com.kingpivot.api.dto.weixin.*;
import com.kingpivot.base.config.Config;
import com.kingpivot.base.wechart.model.Wechart;
import com.kingpivot.base.wechart.service.WechartService;
import com.kingpivot.client.FileClient;
import com.kingpivot.common.util.JsonUtil;
import com.kingpivot.common.util.MapUtil;
import com.kingpivot.common.utils.FileUtil;
import com.kingpivot.common.utils.IdGenerator;
import com.kingpivot.common.utils.JacksonHelper;
import com.kingpivot.protocol.ApiBaseController;
import com.kingpivot.protocol.MessageHeader;
import com.kingpivot.protocol.MessagePacket;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

@RequestMapping("/api")
@RestController
@Api(description = "微信管理接口")
public class ApiWechartController extends ApiBaseController {


    @Autowired
    private WechartService wechartService;

    /**
     * 获取微信参数
     *
     * @param request
     * @return
     */
    @ApiOperation(value = "getWeixinConfig", notes = "获取微信参数")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "appId", value = "公众号appid", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "locationUrl", value = "当前页面url", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "appSecret", value = "私钥", dataType = "String")})
    @RequestMapping(value = "/getWeixinConfig")
    public MessagePacket weixinShareConfig(HttpServletRequest request) {
        String url = request.getParameter("url");
        String publicNo = request.getParameter("publicNo");

        if (StringUtils.isEmpty(url)) {
            return MessagePacket.newFail(MessageHeader.Code.weixinurlIsEmpty, "url不能为空");
        }
        if (StringUtils.isEmpty(publicNo)) {
            return MessagePacket.newFail(MessageHeader.Code.publicNoIsEmpty, "publicNo不能为空");
        }
        Wechart wechart = wechartService.getWechartByPublicNo(publicNo);
        if (wechart == null) {
            return MessagePacket.newFail(MessageHeader.Code.publicNoIsError, "publicNo不正确");
        }
        WeiXinToken token = WeiXinUtils.getToken(wechart.getAPPid(), wechart.getAPPsecret());
        String ticket = WeiXinJSUtils.getTicket(token.getAccess_token()).getTicket();

        WeiXinJS weiXinJS = new WeiXinJS();
        weiXinJS.setNoncestr(WeiXinUtils.buildRandom());
        weiXinJS.setJsapi_ticket(ticket);
        weiXinJS.setTimestamp(WeiXinUtils.getTimestamp());
        weiXinJS.setUrl(url);
        weiXinJS.setSign(WeiXinJSUtils.createSign(MapUtil.beanToMap(weiXinJS)));

        Map<String, String> configMap = Maps.newConcurrentMap();
        configMap.put("url", weiXinJS.getUrl());
        configMap.put("appId", wechart.getAPPid());
        configMap.put("timestamp", weiXinJS.getTimestamp());
        configMap.put("nonceStr", weiXinJS.getNoncestr());
        configMap.put("signature", weiXinJS.getSign());
        configMap.put("jsApiList", JsonUtil.writeValueAsString(WeiXinJSUtils.getJSApiList()));
        configMap.put("ticket", ticket);
        return MessagePacket.newSuccess(configMap, "getWeixinConfig success");
    }

    /**
     * 小程序获取微信openoid
     *
     * @param request
     * @return
     */
    @ApiOperation(value = "小程序获取微信openoid", notes = "小程序获取微信openoid")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "publicNo", value = "公众号id", dataType = "String", required = true),
            @ApiImplicitParam(paramType = "query", name = "js_code", value = "code", dataType = "String", required = true),
    })
    @RequestMapping(value = "/getWeiXinAppOpenId")
    public MessagePacket getWeiXinAppOpenId(HttpServletRequest request) throws Exception {
        String publicNo = request.getParameter("publicNo");
        String js_code = request.getParameter("js_code");
        Wechart wechart = wechartService.getWechartByPublicNo(publicNo);
        if (wechart == null) {
            return MessagePacket.newFail(MessageHeader.Code.illegalParameter, "微信记录不存在");
        }
        Map<String, String> param = new HashMap<>();
        param.put("appid", wechart.getAPPid());
        param.put("secret", wechart.getAPPsecret());
        param.put("js_code", js_code);
        param.put("grant_type", "authorization_code");
        String info = HttpUtil.doGet(WeiXinUrlConstants.GET_WEIXINAPPCODE_URL, param);
        Map<String, Object> rsMap = Maps.newHashMap();
        if (StringUtils.hasText(info)) {
            JSONObject jsonObject = JSONObject.fromObject(info);
            rsMap.put("openID", jsonObject.get("openid"));
        } else {
            System.out.println("error:" + info);
            return MessagePacket.newFail(MessageHeader.Code.weixinFormID, "获取openid异常，请联系管理员");
        }
        return MessagePacket.newSuccess(rsMap, "getWeiXinAppOpenId success");
    }

    @ApiOperation(value = "独立接口：获取自定义小程序二维码", notes = "独立接口：获取自定义小程序二维码")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "page", value = "页面路径", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "scene", value = "参数", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "publicNo", value = "公众号标识", dataType = "String"),
    })
    @RequestMapping(value = "/getWeixinAppQrcode")
    public MessagePacket getWeixinAppQrcode(HttpServletRequest request) {
        String page = request.getParameter("page");
        String scene = request.getParameter("scene");
        String publicNo = request.getParameter("publicNo");
        if (StringUtils.isEmpty(page)) {
            return MessagePacket.newFail(MessageHeader.Code.pageIsNull, "page不能为空");
        }
        if (StringUtils.isEmpty(scene)) {
            return MessagePacket.newFail(MessageHeader.Code.sceneIsNull, "scene不能为空");
        }
        if (StringUtils.isEmpty(publicNo)) {
            return MessagePacket.newFail(MessageHeader.Code.publicNoIsNull, "publicNo不能为空");
        }
        Wechart wechart = wechartService.getWechartByPublicNo(publicNo);
        if (wechart == null) {
            return MessagePacket.newFail(MessageHeader.Code.publicNoIsError, "publicNo不正确");
        }
        InputStream inputStream = WeiXinUtils.getwxacodeunlimit(scene, page, wechart.getAPPid(), wechart.getAPPsecret());
        if (null != inputStream) {
            String fileName = String.format("%s.jpg", IdGenerator.uuid32());
            File file = new File(fileName);
            FileUtil.inputStreamToFile(inputStream, file);
            String url = FileClient.uploadFile(file, fileName, ".jpg", Config.LOCAL_FILE_SERVER_BUCKET, false);
            file.delete();
            if (StringUtils.hasText(url)) {
                Result result = JacksonHelper.fromJson(url, Result.class);
                if (result != null) {
                    Map<String, Object> rsMap = Maps.newHashMap();
                    rsMap.put("url", result.getFilePath());
                    return MessagePacket.newSuccess(rsMap, "getWeixinAppQrcode success");
                }
            }
            return MessagePacket.newFail(MessageHeader.Code.illegalParameter, "文件生成失败，请联系管理员");
        } else {
            return MessagePacket.newFail(MessageHeader.Code.illegalParameter, "文件为空");
        }
    }
}
