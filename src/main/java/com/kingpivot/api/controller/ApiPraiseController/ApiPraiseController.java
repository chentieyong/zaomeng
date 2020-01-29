package com.kingpivot.api.controller.ApiPraiseController;

import com.google.common.collect.Maps;
import com.kingpivot.base.config.RedisKey;
import com.kingpivot.base.config.UserAgent;
import com.kingpivot.base.member.model.Member;
import com.kingpivot.base.memberlog.model.Memberlog;
import com.kingpivot.base.praise.model.Praise;
import com.kingpivot.base.praise.service.PraiseService;
import com.kingpivot.base.support.MemberLogDTO;
import com.kingpivot.common.jms.SendMessageService;
import com.kingpivot.common.jms.dto.memberLog.MemberLogRequestBase;
import com.kingpivot.common.utils.JacksonHelper;
import com.kingpivot.common.utils.TimeTest;
import com.kingpivot.common.utils.UserAgentUtil;
import com.kingpivot.protocol.MessageHeader;
import com.kingpivot.protocol.MessagePacket;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RequestMapping("/api")
@RestController
@Api(description = "赞管理接口")
public class ApiPraiseController {

    @Resource
    private SendMessageService sendMessageService;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private PraiseService praiseService;

    @ApiOperation(value = "加入赞", notes = "加入赞")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "sessionID", value = "登录标识", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "objectID", value = "对象id", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "objectName", value = "对象名", dataType = "int"),
            @ApiImplicitParam(paramType = "query", name = "objectDefineID", value = "对象定义id", dataType = "String")})
    @RequestMapping(value = "/addPraise")
    public MessagePacket addPraise(HttpServletRequest request) {
        String sessionID = request.getParameter("sessionID");
        if (StringUtils.isEmpty(sessionID)) {
            return MessagePacket.newFail(MessageHeader.Code.unauth, "请先登录");
        }
        Member member = (Member) redisTemplate.opsForValue().get(String.format("%s%s", RedisKey.Key.MEMBER_KEY.key, sessionID));
        if (member == null) {
            return MessagePacket.newFail(MessageHeader.Code.unauth, "请先登录");
        }
        MemberLogDTO memberLogDTO = (MemberLogDTO) redisTemplate.opsForValue().get(String.format("%s%s", RedisKey.Key.MEMBERLOG_KEY.key, sessionID));
        if (memberLogDTO == null) {
            return MessagePacket.newFail(MessageHeader.Code.unauth, "请先登录");
        }

        String objectID = request.getParameter("objectID");
        if (StringUtils.isEmpty(objectID)) {
            return MessagePacket.newFail(MessageHeader.Code.objectIdIsNull, "objectID不能为空");
        }

        String praiseID = praiseService.getPraiseByObjectIDAndMemberID(objectID, member.getId());

        if (StringUtils.isNotBlank(praiseID)) {
            return MessagePacket.newFail(MessageHeader.Code.memberIsCollect, "会员已收藏");
        }
        String objectName = request.getParameter("objectName");
        if (StringUtils.isEmpty(objectName)) {
            return MessagePacket.newFail(MessageHeader.Code.objectNameIsNull, "objectName不能为空");
        }
        String objectDefineID = request.getParameter("objectDefineID");
        if (StringUtils.isEmpty(objectDefineID)) {
            return MessagePacket.newFail(MessageHeader.Code.objectDefineIDIsNull, "objectDefineID不能为空");
        }

        Praise praise = new Praise();
        praise.setApplicationID(member.getApplicationID());
        praise.setMemberID(member.getId());
        praise.setName(String.format("%s赞%s", member.getName(), objectName));
        praise.setObjectDefineID(objectDefineID);
        praise.setObjectID(objectID);
        praise.setObjectName(objectName);
        praiseService.save(praise);

        String description = String.format("%s赞%s", member.getName(), objectName);

        UserAgent userAgent = UserAgentUtil.getUserAgent(request.getHeader("user-agent"));
        MemberLogRequestBase base = MemberLogRequestBase.BALANCE()
                .sessionID(sessionID)
                .description(description)
                .userAgent(userAgent == null ? null : userAgent.getBrowserType())
                .operateType(Memberlog.MemberOperateType.ADDPRAISE.getOname())
                .build();

        sendMessageService.sendMemberLogMessage(JacksonHelper.toJson(base));

        Map<String, Object> rsMap = Maps.newHashMap();
        rsMap.put("data", praise.getId());

        return MessagePacket.newSuccess(rsMap, "addPraise success!");
    }

    @ApiOperation(value = "删除赞记录", notes = "删除赞记录")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "sessionID", value = "登录标识", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "praiseID", value = "收藏id", dataType = "String")})
    @RequestMapping(value = "/removePraise")
    public MessagePacket removePraise(HttpServletRequest request) {
        String sessionID = request.getParameter("sessionID");
        if (StringUtils.isEmpty(sessionID)) {
            return MessagePacket.newFail(MessageHeader.Code.unauth, "请先登录");
        }
        Member member = (Member) redisTemplate.opsForValue().get(String.format("%s%s", RedisKey.Key.MEMBER_KEY.key, sessionID));
        if (member == null) {
            return MessagePacket.newFail(MessageHeader.Code.unauth, "请先登录");
        }
        MemberLogDTO memberLogDTO = (MemberLogDTO) redisTemplate.opsForValue().get(String.format("%s%s", RedisKey.Key.MEMBERLOG_KEY.key, sessionID));
        if (memberLogDTO == null) {
            return MessagePacket.newFail(MessageHeader.Code.unauth, "请先登录");
        }

        String praiseID = request.getParameter("praiseID");
        if (StringUtils.isEmpty(praiseID)) {
            return MessagePacket.newFail(MessageHeader.Code.praiseIDIsNull, "praiseID不能为空");
        }

        Praise praise = praiseService.findById(praiseID);
        if (praise == null) {
            return MessagePacket.newFail(MessageHeader.Code.praiseIDIsError, "praiseID不正确");
        }

        praiseService.del(praise);

        String description = String.format("%s删除赞", member.getName());

        UserAgent userAgent = UserAgentUtil.getUserAgent(request.getHeader("user-agent"));
        MemberLogRequestBase base = MemberLogRequestBase.BALANCE()
                .sessionID(sessionID)
                .description(description)
                .userAgent(userAgent == null ? null : userAgent.getBrowserType())
                .operateType(Memberlog.MemberOperateType.REMOVEPRAISE.getOname())
                .build();
        sendMessageService.sendMemberLogMessage(JacksonHelper.toJson(base));

        Map<String, Object> rsMap = Maps.newHashMap();
        rsMap.put("data", TimeTest.getTimeStr());

        return MessagePacket.newSuccess(rsMap, "removePraise success!");
    }
}
