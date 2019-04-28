package com.kingpivot.api.controller.ApiFeedController;

import com.google.common.collect.Maps;
import com.kingpivot.base.config.RedisKey;
import com.kingpivot.base.config.UserAgent;
import com.kingpivot.base.feed.model.Feed;
import com.kingpivot.base.feed.service.FeedService;
import com.kingpivot.base.member.model.Member;
import com.kingpivot.base.memberlog.model.Memberlog;
import com.kingpivot.base.support.MemberLogDTO;
import com.kingpivot.common.jms.SendMessageService;
import com.kingpivot.common.jms.dto.memberLog.MemberLogRequestBase;
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
@Api(description = "意见反馈管理接口")
public class ApiFeedController extends ApiBaseController {

    @Resource
    private SendMessageService sendMessageService;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private FeedService feedService;

    @ApiOperation(value = "提交一个意见反馈", notes = "提交一个意见反馈")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "sessionID", value = "登录标识", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "contact", value = "联系人", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "contant", value = "内容", dataType = "int"),
            @ApiImplicitParam(paramType = "query", name = "title", value = "标题", dataType = "int")})
    @RequestMapping(value = "/submitOneFee")
    public MessagePacket submitOneFee(HttpServletRequest request) {
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

        String contant = request.getParameter("contant");
        if (StringUtils.isEmpty(contant)) {
            return MessagePacket.newFail(MessageHeader.Code.contantIsNull, "contant不能为空");
        }
        String title = request.getParameter("title");
        String contact = request.getParameter("contact");

        Feed feed = new Feed();
        feed.setApplicationID(member.getApplicationID());
        feed.setMemberID(member.getId());
        feed.setContant(contant);
        if (StringUtils.isNotBlank(contact)) {
            feed.setContact(contact);
        }
        if (StringUtils.isNotBlank(title)) {
            feed.setTitle(title);
        }
        feedService.save(feed);
        String description = String.format("%s提交一个意见反馈", member.getName());

        UserAgent userAgent = UserAgentUtil.getUserAgent(request.getHeader("user-agent"));
        MemberLogRequestBase base = MemberLogRequestBase.BALANCE()
                .sessionID(sessionID)
                .description(description)
                .userAgent(userAgent == null ? null : userAgent.getBrowserType())
                .operateType(Memberlog.MemberOperateType.SUBMITONEFEE.getOname())
                .build();

        sendMessageService.sendMemberLogMessage(JacksonHelper.toJson(base));

        Map<String, Object> rsMap = Maps.newHashMap();
        rsMap.put("data", feed.getId());

        return MessagePacket.newSuccess(rsMap, "submitOneFee success!");
    }
}
