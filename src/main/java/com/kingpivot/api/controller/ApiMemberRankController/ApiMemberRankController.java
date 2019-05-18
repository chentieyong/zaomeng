package com.kingpivot.api.controller.ApiMemberRankController;

import com.google.common.collect.Maps;
import com.kingpivot.api.dto.memberRank.ApiMemberRankDto;
import com.kingpivot.base.config.RedisKey;
import com.kingpivot.base.config.UserAgent;
import com.kingpivot.base.member.model.Member;
import com.kingpivot.base.member.service.MemberService;
import com.kingpivot.base.memberlog.model.Memberlog;
import com.kingpivot.base.rank.model.Rank;
import com.kingpivot.base.rank.service.RankService;
import com.kingpivot.base.support.MemberLogDTO;
import com.kingpivot.common.jms.SendMessageService;
import com.kingpivot.common.jms.dto.memberLog.MemberLogRequestBase;
import com.kingpivot.common.utils.BeanMapper;
import com.kingpivot.common.utils.JacksonHelper;
import com.kingpivot.common.utils.UserAgentUtil;
import com.kingpivot.protocol.ApiBaseController;
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
@Api(description = "会员等级管理接口")
public class ApiMemberRankController extends ApiBaseController {

    @Resource
    private SendMessageService sendMessageService;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private MemberService memberService;
    @Autowired
    private RankService rankService;


    @ApiOperation(value = "获取会员等级信息", notes = "获取会员等级信息")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "sessionID", value = "登录标识", dataType = "String")})
    @RequestMapping(value = "/getMemberRankInfo")
    public MessagePacket getMemberRankInfo(HttpServletRequest request) {
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
        ApiMemberRankDto rankDto = new ApiMemberRankDto();
        Member mb = memberService.findById(member.getId());
        if (StringUtils.isNotBlank(mb.getRankID())) {
            Rank rank = rankService.findById(mb.getRankID());
            if (rank != null) {
                rankDto = BeanMapper.map(rank, ApiMemberRankDto.class);
            }
        }

        String description = String.format("%s获取会员等级信息", member.getName());

        UserAgent userAgent = UserAgentUtil.getUserAgent(request.getHeader("user-agent"));
        MemberLogRequestBase base = MemberLogRequestBase.BALANCE()
                .sessionID(sessionID)
                .description(description)
                .userAgent(userAgent == null ? null : userAgent.getBrowserType())
                .operateType(Memberlog.MemberOperateType.GETMEMBERRANKINFO.getOname())
                .build();
        sendMessageService.sendMemberLogMessage(JacksonHelper.toJson(base));

        Map<String, Object> rsMap = Maps.newHashMap();
        rsMap.put("data", rankDto);
        return MessagePacket.newSuccess(rsMap, "getMemberRankInfo success!");
    }
}
