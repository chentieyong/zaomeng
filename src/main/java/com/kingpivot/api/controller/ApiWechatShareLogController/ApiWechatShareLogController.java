package com.kingpivot.api.controller.ApiWechatShareLogController;

import com.google.common.collect.Maps;
import com.kingpivot.base.member.model.Member;
import com.kingpivot.base.member.service.MemberService;
import com.kingpivot.base.wechart.model.Wechart;
import com.kingpivot.base.wechart.service.WechartService;
import com.kingpivot.base.wechatShareLog.model.WechatShareLog;
import com.kingpivot.base.wechatShareLog.service.WechatShareLogService;
import com.kingpivot.protocol.ApiBaseController;
import com.kingpivot.protocol.MessageHeader;
import com.kingpivot.protocol.MessagePacket;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RequestMapping("/api")
@RestController
@Api(description = "微信分享记录接口")
public class ApiWechatShareLogController extends ApiBaseController {

    @Autowired
    private WechartService wechartService;
    @Autowired
    private WechatShareLogService wechatShareLogService;
    @Autowired
    private MemberService memberService;

    @ApiOperation(value = "submitOneWechatShareLog", notes = "提交一个微信分享")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "memberID", value = "微信id", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "publicNo", value = "公众号", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "url", value = "url", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "type", value = "type", dataType = "String")
    })
    @RequestMapping(value = "/submitOneWechatShareLog")
    public MessagePacket submitOneWechatShareLog(HttpServletRequest request) {
        String memberID = request.getParameter("memberID");
        if (StringUtils.isEmpty(memberID)) {
            return MessagePacket.newFail(MessageHeader.Code.nameIsNull, "会员不能为空");
        }
        Member member = memberService.findById(memberID);
        if (member == null) {
            return MessagePacket.newFail(MessageHeader.Code.illegalParameter, "会员不存在");
        }
        String publicNo = request.getParameter("publicNo");//公众号
        if (StringUtils.isEmpty(publicNo)) {
            return MessagePacket.newFail(MessageHeader.Code.publicNoIsEmpty, "公众号不能为空");
        }
        Wechart wechart = wechartService.getWechartByPublicNo(publicNo);
        if (wechart == null) {
            return MessagePacket.newFail(MessageHeader.Code.publicNoIsError, "公众号不存在");
        }
        String url = request.getParameter("url");//url
        if (StringUtils.isEmpty(url)) {
            return MessagePacket.newFail(MessageHeader.Code.viewUrlIsNull, "url不能为空");
        }
        String type = request.getParameter("type");//type
        WechatShareLog wechatShareLog = new WechatShareLog();
        wechatShareLog.setApplicationID(wechart.getApplicationId());
        wechatShareLog.setWechartID(wechart.getId());
        wechatShareLog.setName(member.getName() + "分享");
        wechatShareLog.setMemberID(memberID);
        wechatShareLog.setUrl(url);
        wechatShareLog.setType(type);
        wechatShareLogService.save(wechatShareLog);
        Map<String, Object> rsMap = Maps.newHashMap();
        rsMap.put("data", wechatShareLog.getId());
        return MessagePacket.newSuccess(rsMap, "submitOneJobNeed success!");
    }
}
