package com.kingpivot.api.controller.ApiMemberCardController;

import com.google.common.collect.Maps;
import com.kingpivot.api.dto.memberCard.MemberCardListDto;
import com.kingpivot.base.cardDefine.model.CardDefine;
import com.kingpivot.base.cardDefine.service.CardDefineService;
import com.kingpivot.base.config.RedisKey;
import com.kingpivot.base.config.UserAgent;
import com.kingpivot.base.member.model.Member;
import com.kingpivot.base.memberCard.model.MemberCard;
import com.kingpivot.base.memberCard.service.MemberCardService;
import com.kingpivot.base.memberRecharge.model.MemberRecharge;
import com.kingpivot.base.memberRecharge.service.MemberRechargeService;
import com.kingpivot.base.memberlog.model.Memberlog;
import com.kingpivot.base.support.MemberLogDTO;
import com.kingpivot.common.jms.SendMessageService;
import com.kingpivot.common.jms.dto.memberLog.MemberLogRequestBase;
import com.kingpivot.common.util.Constants;
import com.kingpivot.common.utils.*;
import com.kingpivot.protocol.ApiBaseController;
import com.kingpivot.protocol.MessageHeader;
import com.kingpivot.protocol.MessagePacket;
import com.kingpivot.protocol.MessagePage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequestMapping("/api")
@RestController
@Api(description = "会员卡管理接口")
public class ApiMemberRechargeController extends ApiBaseController {

    @Resource
    private SendMessageService sendMessageService;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private MemberRechargeService memberRechargeService;

    @ApiOperation(value = "submitOneMemberCharge", notes = "提交一个会员充值")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "sessionID", value = "登录标识", dataType = "String")})
    @RequestMapping(value = "/submitOneMemberCharge")
    public MessagePacket submitOneMemberCharge(HttpServletRequest request) {
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
        String amount = request.getParameter("amount");
        if (StringUtils.isEmpty(amount) || Double.parseDouble(amount) <= 0) {
            return MessagePacket.newFail(MessageHeader.Code.amountNotNull, "充值金额异常");
        }

        //创建会员充值记录
        MemberRecharge memberRecharge = new MemberRecharge();
        memberRecharge.setName(member.getName() + "充值");
        memberRecharge.setMemberID(member.getId());
        memberRecharge.setCompanyID(member.getCompanyID());
        memberRecharge.setMemberID(member.getId());
        memberRecharge.setAmount(Double.parseDouble(amount));
        memberRecharge.setStatus(1);
        memberRechargeService.save(memberRecharge);

        String desc = String.format("%s充值%s", member.getName(), amount);
        UserAgent userAgent = UserAgentUtil.getUserAgent(request.getHeader("user-agent"));
        MemberLogRequestBase base = MemberLogRequestBase.BALANCE()
                .sessionID(sessionID)
                .description(desc)
                .userAgent(userAgent == null ? null : userAgent.getBrowserType())
                .operateType(Memberlog.MemberOperateType.SUBMITONMEMBERCHARGE.getOname())
                .build();
        sendMessageService.sendMemberLogMessage(JacksonHelper.toJson(base));

        Map<String, Object> rsMap = Maps.newHashMap();
        rsMap.put("memberRechargeID", memberRecharge.getId());
        return MessagePacket.newSuccess(rsMap, "submitOneMemberCharge success!");
    }
}
