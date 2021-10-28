package com.kingpivot.api.controller.ApiMemberCardController;

import com.google.common.collect.Maps;
import com.kingpivot.api.dto.memberBalance.MemberBalanceListDto;
import com.kingpivot.api.dto.memberCard.MemberCardListDto;
import com.kingpivot.base.cardDefine.model.CardDefine;
import com.kingpivot.base.cardDefine.service.CardDefineService;
import com.kingpivot.base.config.RedisKey;
import com.kingpivot.base.config.UserAgent;
import com.kingpivot.base.member.model.Member;
import com.kingpivot.base.member.service.MemberService;
import com.kingpivot.base.memberBalance.model.MemberBalance;
import com.kingpivot.base.memberCard.model.MemberCard;
import com.kingpivot.base.memberCard.service.MemberCardService;
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
public class ApiMemberCardController extends ApiBaseController {

    @Resource
    private SendMessageService sendMessageService;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private MemberCardService memberCardService;
    @Autowired
    private CardDefineService cardDefineService;

    @ApiOperation(value = "getMemberCardList", notes = "获取会员卡列表")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "sessionID", value = "登录标识", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "currentPage", value = "分页，页码从1开始", dataType = "int"),
            @ApiImplicitParam(paramType = "query", name = "pageNumber", value = "每一页大小", dataType = "int")})
    @RequestMapping(value = "/getMemberCardList")
    public MessagePacket getMemberCardList(HttpServletRequest request) {
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

        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("memberID", member.getId());
        paramMap.put("status", 1);
        paramMap.put("isValid", Constants.ISVALID_YES);
        paramMap.put("isLock", Constants.ISLOCK_NO);

        List<Sort.Order> orders = new ArrayList<Sort.Order>();
        orders.add(new Sort.Order(Sort.Direction.DESC, "createdTime"));

        Object currentPage = request.getParameter("currentPage");
        Object pageNumber = request.getParameter("pageNumber");

        TPage page = ApiPageUtil.makePage(currentPage, pageNumber);

        Pageable pageable = new PageRequest(page.getOffset(), page.getPageSize(), new Sort(orders));

        Page<MemberCard> rs = memberCardService.list(paramMap, pageable);
        List<MemberCardListDto> list = null;
        if (rs != null && rs.getSize() != 0) {
            list = BeanMapper.mapList(rs.getContent(), MemberCardListDto.class);
            page.setTotalSize((int) rs.getTotalElements());
        }

        String desc = String.format("%s获取会员卡列表", member.getName());
        UserAgent userAgent = UserAgentUtil.getUserAgent(request.getHeader("user-agent"));
        MemberLogRequestBase base = MemberLogRequestBase.BALANCE()
                .sessionID(sessionID)
                .description(desc)
                .userAgent(userAgent == null ? null : userAgent.getBrowserType())
                .operateType(Memberlog.MemberOperateType.GETMEMBERCARDLIST.getOname())
                .build();
        sendMessageService.sendMemberLogMessage(JacksonHelper.toJson(base));

        Map<String, Object> rsMap = Maps.newHashMap();
        MessagePage messagePage = new MessagePage(page, list);
        rsMap.put("data", messagePage);
        return MessagePacket.newSuccess(rsMap, "getMemberCardList success!");
    }

    @ApiOperation(value = "buyMemberCard", notes = "购买会员卡")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "sessionID", value = "登录标识", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "cardDefineID", value = "卡定义id", dataType = "String")})
    @RequestMapping(value = "/buyMemberCard")
    public MessagePacket buyMemberCard(HttpServletRequest request) {
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
        String cardDefineID = request.getParameter("cardDefineID");
        if (StringUtils.isEmpty(cardDefineID)) {
            return MessagePacket.newFail(MessageHeader.Code.cardDefineIDIsNull, "卡定义id为空");
        }
        CardDefine cardDefine = cardDefineService.findById(cardDefineID);
        if (cardDefine == null) {
            return MessagePacket.newFail(MessageHeader.Code.cardDefineIDIsError, "卡定义id不正确");
        }
        int cardSize = memberCardService.getCountEffectiveMemberCardByCardDefineID(member.getId(), cardDefineID);
        if (cardSize != 0) {
            return MessagePacket.newFail(MessageHeader.Code.cardDefineIsValid, "存在未过期的卡券，无法再次购买");
        }
        //创建会员卡记录
        MemberCard memberCard = new MemberCard();
        memberCard.setApplicationID(member.getApplicationID());
        memberCard.setName(cardDefine.getName());
        memberCard.setFaceImage(cardDefine.getFaceImage());
        memberCard.setListImage(cardDefine.getListImage());
        memberCard.setMemberID(member.getId());
        memberCard.setCardDefineID(cardDefine.getId());
        memberCard.setBeginTime(TimeTest.timeToDate(new Timestamp(System.currentTimeMillis())));
        memberCard.setEndTime(TimeTest.timeToEndDate(TimeTest.timeAddDay(memberCard.getBeginTime(), cardDefine.getEffectiveDays())));
        memberCardService.save(memberCard);

        String desc = String.format("%s购买会员卡", member.getName());
        UserAgent userAgent = UserAgentUtil.getUserAgent(request.getHeader("user-agent"));
        MemberLogRequestBase base = MemberLogRequestBase.BALANCE()
                .sessionID(sessionID)
                .description(desc)
                .userAgent(userAgent == null ? null : userAgent.getBrowserType())
                .operateType(Memberlog.MemberOperateType.BUYMEMBERCARD.getOname())
                .build();
        sendMessageService.sendMemberLogMessage(JacksonHelper.toJson(base));

        Map<String, Object> rsMap = Maps.newHashMap();
        rsMap.put("memberCardID", memberCard.getMemberID());
        return MessagePacket.newSuccess(rsMap, "buyMemberCard success!");
    }
}
