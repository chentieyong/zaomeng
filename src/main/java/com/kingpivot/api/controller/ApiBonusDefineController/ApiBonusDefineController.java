package com.kingpivot.api.controller.ApiBonusDefineController;

import com.google.common.collect.Maps;
import com.kingpivot.api.dto.bonusDefine.CanGetBonusDefineListDto;
import com.kingpivot.base.bonusDefine.model.BonusDefine;
import com.kingpivot.base.bonusDefine.service.BonusDefineService;
import com.kingpivot.base.config.RedisKey;
import com.kingpivot.base.config.UserAgent;
import com.kingpivot.base.member.model.Member;
import com.kingpivot.base.memberBonus.service.MemberBonusService;
import com.kingpivot.base.memberlog.model.Memberlog;
import com.kingpivot.base.support.MemberLogDTO;
import com.kingpivot.common.jms.SendMessageService;
import com.kingpivot.common.jms.dto.getMemberBonus.GetMemberBonusDto;
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
@Api(description = "红包定义管理接口")
public class ApiBonusDefineController extends ApiBaseController {
    @Resource
    private SendMessageService sendMessageService;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private BonusDefineService bonusDefineService;
    @Autowired
    private MemberBonusService memberBonusService;

    @ApiOperation(value = "获取可领红包定义列表", notes = "获取可领红包定义列表")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "sessionID", value = "登录标识", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "currentPage", value = "分页，页码从1开始", dataType = "int"),
            @ApiImplicitParam(paramType = "query", name = "pageNumber", value = "每一页大小", dataType = "int")})
    @RequestMapping(value = "/getCanGetBonusDefineList")
    public MessagePacket getCanGetBonusDefineList(HttpServletRequest request) {
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
        paramMap.put("isValid", Constants.ISVALID_YES);
        paramMap.put("isLock", Constants.ISLOCK_NO);
        paramMap.put("startDate:lte", new Timestamp(System.currentTimeMillis()));
        paramMap.put("endDate:gte", new Timestamp(System.currentTimeMillis()));

        List<Sort.Order> orders = new ArrayList<Sort.Order>();
        orders.add(new Sort.Order(Sort.Direction.DESC, "createdTime"));

        Object currentPage = request.getParameter("currentPage");
        Object pageNumber = request.getParameter("pageNumber");

        TPage page = ApiPageUtil.makePage(currentPage, pageNumber);

        Pageable pageable = new PageRequest(page.getOffset(), page.getPageSize(), new Sort(orders));

        Page<BonusDefine> rs = bonusDefineService.list(paramMap, pageable);
        List<CanGetBonusDefineListDto> list = null;
        if (rs != null && rs.getSize() != 0) {
            list = BeanMapper.mapList(rs.getContent(), CanGetBonusDefineListDto.class);
            for (CanGetBonusDefineListDto obj : list) {
                obj.setMyGetStatus(memberBonusService.getMyBonusByBonusId(member.getId(), obj.getId()) == 0 ? 0 : 1);
            }
            page.setTotalSize((int) rs.getTotalElements());
        }

        String description = String.format("%s获取可领红包定义列表", member.getName());

        UserAgent userAgent = UserAgentUtil.getUserAgent(request.getHeader("user-agent"));
        MemberLogRequestBase base = MemberLogRequestBase.BALANCE()
                .sessionID(sessionID)
                .description(description)
                .userAgent(userAgent == null ? null : userAgent.getBrowserType())
                .operateType(Memberlog.MemberOperateType.GETCANGETBONUSDEFINELIST.getOname())
                .build();

        sendMessageService.sendMemberLogMessage(JacksonHelper.toJson(base));

        Map<String, Object> rsMap = Maps.newHashMap();
        MessagePage messagePage = new MessagePage(page, list);
        rsMap.put("data", messagePage);
        return MessagePacket.newSuccess(rsMap, "getCanGetBonusDefineList success!");
    }


    @ApiOperation(value = "会员领红包", notes = "会员领红包")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "sessionID", value = "登录标识", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "bonusDefineID", value = "红包定义id", dataType = "String")})
    @RequestMapping(value = "/memberGetBonus")
    public MessagePacket memberGetBonus(HttpServletRequest request) {
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

        String bonusDefineID = request.getParameter("bonusDefineID");

        if (StringUtils.isEmpty(bonusDefineID)) {
            return MessagePacket.newFail(MessageHeader.Code.bonusIDIsNull, "bonusDefineID不能为空");
        }

        BonusDefine bonusDefine = bonusDefineService.findById(bonusDefineID);

        if (bonusDefine == null) {
            return MessagePacket.newFail(MessageHeader.Code.bonusIDIsError, "bonusDefineID不正确");
        }

        int myBonus = memberBonusService.getMyBonusByBonusId(member.getId(), bonusDefineID);
        if (myBonus != 0) {
            return MessagePacket.newFail(MessageHeader.Code.illegalParameter, "红包不可重复领取~");
        }

        if ((bonusDefine.getCanGetNumber().intValue() + 1) < bonusDefine.getMaxNumber()) {
            return MessagePacket.newFail(MessageHeader.Code.illegalParameter, "红包已领完");
        }

        /**
         * 发送队列生成红包
         */
        sendMessageService.getMemberBonusMessage(JacksonHelper.toJson(new GetMemberBonusDto(member.getId(), bonusDefineID)));

        String description = String.format("%s会员领红包", member.getName());

        UserAgent userAgent = UserAgentUtil.getUserAgent(request.getHeader("user-agent"));
        MemberLogRequestBase base = MemberLogRequestBase.BALANCE()
                .sessionID(sessionID)
                .description(description)
                .userAgent(userAgent == null ? null : userAgent.getBrowserType())
                .operateType(Memberlog.MemberOperateType.MEMBERGETBONUS.getOname())
                .build();

        sendMessageService.sendMemberLogMessage(JacksonHelper.toJson(base));

        Map<String, Object> rsMap = Maps.newHashMap();
        rsMap.put("data", TimeTest.getTimeStr());
        return MessagePacket.newSuccess(rsMap, "memberGetBonus success!");
    }
}
