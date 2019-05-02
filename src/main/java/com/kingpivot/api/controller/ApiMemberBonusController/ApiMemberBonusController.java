package com.kingpivot.api.controller.ApiMemberBonusController;

import com.google.common.collect.Maps;
import com.kingpivot.api.dto.memberBonus.MyMemberBonusListDto;
import com.kingpivot.base.config.RedisKey;
import com.kingpivot.base.config.UserAgent;
import com.kingpivot.base.member.model.Member;
import com.kingpivot.base.memberBonus.model.MemberBonus;
import com.kingpivot.base.memberBonus.service.MemberBonusService;
import com.kingpivot.base.memberOrder.model.MemberOrder;
import com.kingpivot.base.memberOrder.service.MemberOrderService;
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
@Api(description = "会员红包管理接口")
public class ApiMemberBonusController extends ApiBaseController {

    @Resource
    private SendMessageService sendMessageService;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private MemberBonusService memberBonusService;
    @Autowired
    private MemberOrderService memberOrderService;

    @ApiOperation(value = "获取我的会员红包", notes = "获取我的会员红包")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "sessionID", value = "登录标识", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "currentPage", value = "分页，页码从1开始", dataType = "int"),
            @ApiImplicitParam(paramType = "query", name = "pageNumber", value = "每一页大小", dataType = "int")})
    @RequestMapping(value = "/getMyMemberBonusList")
    public MessagePacket getMyMemberBonusList(HttpServletRequest request) {
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

        /**
         * 1订单id为空，开始时间大于当前，结束时间小于当前
         */
        String sortType = request.getParameter("sortType");

        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("isValid", Constants.ISVALID_YES);
        paramMap.put("isLock", Constants.ISLOCK_NO);
        if (StringUtils.isNotBlank(sortType)) {
            switch (sortType) {
                case "1":
                    paramMap.put("memberOrderID", null);
                    paramMap.put("startDate:gte", new Timestamp(System.currentTimeMillis()));
                    paramMap.put("endDate:lte", new Timestamp(System.currentTimeMillis()));
                    break;
            }
        }

        List<Sort.Order> orders = new ArrayList<Sort.Order>();
        orders.add(new Sort.Order(Sort.Direction.DESC, "createdTime"));

        Object currentPage = request.getParameter("currentPage");
        Object pageNumber = request.getParameter("pageNumber");

        TPage page = ApiPageUtil.makePage(currentPage, pageNumber);

        Pageable pageable = new PageRequest(page.getStart(), page.getPageSize(), new Sort(orders));

        Page<MemberBonus> rs = memberBonusService.list(paramMap, pageable);
        Timestamp nowTime = TimeTest.strToDate(TimeTest.getNowDateFormat());
        List<MyMemberBonusListDto> list = null;
        if (rs != null && rs.getSize() != 0) {
            list = BeanMapper.mapList(rs.getContent(), MyMemberBonusListDto.class);
            for (MyMemberBonusListDto myMemberBonusListDto : list) {
                if (myMemberBonusListDto.getUseTime() != null) {
                    myMemberBonusListDto.setStatus(2);
                } else if (myMemberBonusListDto.getEndDate() != null
                        && nowTime.getTime() > myMemberBonusListDto.getEndDate().getTime()) {
                    myMemberBonusListDto.setStatus(3);
                }
            }
            page.setTotalSize((int) rs.getTotalElements());
        }

        String description = String.format("%s获取我的会员红包", member.getName());

        UserAgent userAgent = UserAgentUtil.getUserAgent(request.getHeader("user-agent"));
        MemberLogRequestBase base = MemberLogRequestBase.BALANCE()
                .sessionID(sessionID)
                .description(description)
                .userAgent(userAgent == null ? null : userAgent.getBrowserType())
                .operateType(Memberlog.MemberOperateType.GETMYMEMBERBONUSLIST.getOname())
                .build();

        sendMessageService.sendMemberLogMessage(JacksonHelper.toJson(base));

        Map<String, Object> rsMap = Maps.newHashMap();
        MessagePage messagePage = new MessagePage(page, list);
        rsMap.put("data", messagePage);
        return MessagePacket.newSuccess(rsMap, "getMyMemberBonusList success!");
    }

    @ApiOperation(value = "使用会员红包", notes = "使用会员红包")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "sessionID", value = "登录标识", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "memberBonusID", value = "会员红包id", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "memberOrderID", value = "会员订单id", dataType = "int")})
    @RequestMapping(value = "/useMemberBonus")
    public MessagePacket useMemberBonus(HttpServletRequest request) {
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

        String memberBonusID = request.getParameter("memberBonusID");
        String memberOrderID = request.getParameter("memberOrderID");

        if (StringUtils.isEmpty(memberBonusID)) {
            return MessagePacket.newFail(MessageHeader.Code.memberBonusIdIsNull, "memberBonusID不能为空");
        }

        if (StringUtils.isEmpty(memberOrderID)) {
            return MessagePacket.newFail(MessageHeader.Code.memberOrderIDIsNull, "memberOrderID不能为空");
        }

        MemberBonus memberBonus = memberBonusService.findById(memberBonusID);

        if (memberBonus == null) {
            return MessagePacket.newFail(MessageHeader.Code.memberBonusIdIsError, "memberBonusID不正确");
        }

        if (memberBonus.getUseTime() != null) {
            return MessagePacket.newFail(MessageHeader.Code.memberbonusIsUsed, "会员红包已使用");
        }

        Timestamp nowTime = TimeTest.strToDate(TimeTest.getNowDateFormat());
        if (memberBonus.getEndDate() != null && nowTime.getTime() > memberBonus.getEndDate().getTime()) {
            return MessagePacket.newFail(MessageHeader.Code.memberbonusIsTimeOut, "会员红包已过期");
        }

        MemberOrder memberOrder = memberOrderService.findById(memberBonusID);

        if (memberOrder == null) {
            return MessagePacket.newFail(MessageHeader.Code.memberOrderIDIsError, "memberBonusID不正确");
        }

        memberBonus.setMemberOrderID(memberOrderID);
        memberBonusService.save(memberBonus);

        String description = String.format("%s使用会员红包", member.getName());

        UserAgent userAgent = UserAgentUtil.getUserAgent(request.getHeader("user-agent"));
        MemberLogRequestBase base = MemberLogRequestBase.BALANCE()
                .sessionID(sessionID)
                .description(description)
                .userAgent(userAgent == null ? null : userAgent.getBrowserType())
                .operateType(Memberlog.MemberOperateType.USEMEMBERBONUS.getOname())
                .build();

        sendMessageService.sendMemberLogMessage(JacksonHelper.toJson(base));

        Map<String, Object> rsMap = Maps.newHashMap();
        rsMap.put("data", TimeTest.getTimeStr());
        return MessagePacket.newSuccess(rsMap, "useMemberBonus success!");
    }
}
