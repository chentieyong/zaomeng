package com.kingpivot.api.controller.ApiMemberOrderGoodsReturnController;

import com.google.common.collect.Maps;
import com.kingpivot.api.dto.memberOrderGoodsReturn.MemberOrderGoodsReturnDetailDto;
import com.kingpivot.api.dto.memberOrderGoodsReturn.MemberOrderGoodsReturnListDto;
import com.kingpivot.base.config.RedisKey;
import com.kingpivot.base.config.UserAgent;
import com.kingpivot.base.member.model.Member;
import com.kingpivot.base.memberOrder.model.MemberOrder;
import com.kingpivot.base.memberOrder.service.MemberOrderService;
import com.kingpivot.base.memberOrderGoods.model.MemberOrderGoods;
import com.kingpivot.base.memberOrderGoods.service.MemberOrderGoodsService;
import com.kingpivot.base.memberOrderReturnGoods.model.MemberOrderGoodsReturn;
import com.kingpivot.base.memberOrderReturnGoods.service.MemberOrderGoodsReturnService;
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
@Api(description = "会员订单商品退货管理接口")
public class ApiMemberOrderGoodsReturnController extends ApiBaseController {
    @Resource
    private SendMessageService sendMessageService;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private MemberOrderGoodsReturnService memberOrderGoodsReturnService;
    @Autowired
    private MemberOrderGoodsService memberOrderGoodsService;
    @Autowired
    private MemberOrderService memberOrderService;

    /**
     * 申请订单商品退货
     *
     * @param request
     * @return
     */
    @ApiOperation(value = "申请订单商品退货", notes = "申请订单商品退货")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "sessionID", value = "登录标识", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "memberOrderGoodsID", value = "会员订单商品id", dataType = "String")})
    @RequestMapping(value = "/applyMemberOrderGoodsReturn")
    public MessagePacket applyMemberOrderGoodsReturn(HttpServletRequest request) {
        String sessionID = request.getParameter("sessionID");
        String memberOrderGoodsID = request.getParameter("memberOrderGoodsID");
        String description = request.getParameter("description");
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
        if (StringUtils.isEmpty(description)) {
            return MessagePacket.newFail(MessageHeader.Code.illegalParameter, "请输入退款理由");
        }
        if (StringUtils.isEmpty(memberOrderGoodsID)) {
            return MessagePacket.newFail(MessageHeader.Code.memberOrderGoodsIDIsNull, "memberOrderGoodsID不能为空");
        }
        MemberOrderGoods memberOrderGoods = memberOrderGoodsService.findById(memberOrderGoodsID);
        if (memberOrderGoods == null) {
            return MessagePacket.newFail(MessageHeader.Code.memberOrderIDIsError, "memberOrderGoodsID不正确");
        }
        if (memberOrderGoods.getStatus() != 4) {
            return MessagePacket.newFail(MessageHeader.Code.statusIsError, "状态不正确");
        }
        if (StringUtils.isEmpty(memberOrderGoods.getMemberOrderID())) {
            return MessagePacket.newFail(MessageHeader.Code.memberOrderIDIsNull, "订单id为空");
        }
        MemberOrder memberOrder = memberOrderService.findById(memberOrderGoods.getMemberOrderID());
        if (memberOrder == null) {
            return MessagePacket.newFail(MessageHeader.Code.memberOrderIDIsError, "订单不存在");
        }
        if (StringUtils.isEmpty(memberOrder.getPaywayID())) {
            return MessagePacket.newFail(MessageHeader.Code.passwordIsNull, "支付机构不存在，请联系管理员");
        }
        memberOrderGoodsReturnService.memberOrderGoodsReturn(memberOrderGoods, member, memberOrder, description);

        String des = String.format("%s申请订单商品退货", member.getName());
        UserAgent userAgent = UserAgentUtil.getUserAgent(request.getHeader("user-agent"));
        MemberLogRequestBase base = MemberLogRequestBase.BALANCE()
                .sessionID(sessionID)
                .description(des)
                .userAgent(userAgent == null ? null : userAgent.getBrowserType())
                .operateType(Memberlog.MemberOperateType.APPLYMEMBERORDERGOODSRETURN.getOname())
                .build();
        sendMessageService.sendMemberLogMessage(JacksonHelper.toJson(base));

        Map<String, Object> rsMap = Maps.newHashMap();
        rsMap.put("data", TimeTest.toDateTimeFormat(new Timestamp(System.currentTimeMillis())));

        return MessagePacket.newSuccess(rsMap, "applyMemberOrderGoodsReturn success!");
    }

    @ApiOperation(value = "获取会员订单商品退货列表", notes = "获取会员订单商品退货列表")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "sessionID", value = "登录标识", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "currentPage", value = "分页，页码从1开始", dataType = "int"),
            @ApiImplicitParam(paramType = "query", name = "pageNumber", value = "每一页大小", dataType = "int")})
    @RequestMapping(value = "/getMemberOrderGoodsReturnList")
    public MessagePacket getMemberOrderGoodsReturnList(HttpServletRequest request) {
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
        paramMap.put("isValid", Constants.ISVALID_YES);
        paramMap.put("isLock", Constants.ISLOCK_NO);
        List<Sort.Order> orders = new ArrayList<Sort.Order>();
        orders.add(new Sort.Order(Sort.Direction.DESC, "createdTime"));
        Object currentPage = request.getParameter("currentPage");
        Object pageNumber = request.getParameter("pageNumber");

        TPage page = ApiPageUtil.makePage(currentPage, pageNumber);

        Pageable pageable = new PageRequest(page.getStart(), page.getPageSize(), new Sort(orders));

        Page<MemberOrderGoodsReturn> rs = memberOrderGoodsReturnService.list(paramMap, pageable);

        List<MemberOrderGoodsReturnListDto> list = null;
        if (rs != null && rs.getSize() != 0) {
            list = BeanMapper.mapList(rs.getContent(), MemberOrderGoodsReturnListDto.class);
            page.setTotalSize((int) rs.getTotalElements());
        }

        String description = String.format("%s获取会员订单商品退货列表", member.getName());

        UserAgent userAgent = UserAgentUtil.getUserAgent(request.getHeader("user-agent"));
        MemberLogRequestBase base = MemberLogRequestBase.BALANCE()
                .sessionID(sessionID)
                .description(description)
                .userAgent(userAgent == null ? null : userAgent.getBrowserType())
                .operateType(Memberlog.MemberOperateType.GETMEMBERORDERGOODSRETURNLIST.getOname())
                .build();

        sendMessageService.sendMemberLogMessage(JacksonHelper.toJson(base));

        Map<String, Object> rsMap = Maps.newHashMap();
        MessagePage messagePage = new MessagePage(page, list);
        rsMap.put("data", messagePage);
        return MessagePacket.newSuccess(rsMap, "getMemberOrderGoodsReturnList success!");
    }

    @ApiOperation(value = "获取会员订单商品退货详情", notes = "获取会员订单商品退货详情")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "sessionID", value = "登录标识", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "memberOrderGoodsReturnID", value = "会员订单商品退货id", dataType = "String")})
    @RequestMapping(value = "/getMemberOrderGoodsReturnDetail")
    public MessagePacket getMemberOrderGoodsReturnDetail(HttpServletRequest request) {
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

        String memberOrderGoodsReturnID = request.getParameter("memberOrderGoodsReturnID");
        if (StringUtils.isEmpty(memberOrderGoodsReturnID)) {
            return MessagePacket.newFail(MessageHeader.Code.memberOrderGoodsReturnIDIsNull, "memberOrderGoodsReturnID为空");
        }

        MemberOrderGoodsReturn memberOrderGoodsReturn = memberOrderGoodsReturnService.findById(memberOrderGoodsReturnID);

        if (memberOrderGoodsReturn == null) {
            return MessagePacket.newFail(MessageHeader.Code.memberOrderGoodsReturnIDIsError, "memberOrderGoodsReturnID不正确");
        }

        MemberOrderGoodsReturnDetailDto memberOrderGoodsReturnDetailDto = BeanMapper.map(memberOrderGoodsReturn, MemberOrderGoodsReturnDetailDto.class);

        String description = String.format("%s获取会员订单商品退货详情", member.getName());

        UserAgent userAgent = UserAgentUtil.getUserAgent(request.getHeader("user-agent"));
        MemberLogRequestBase base = MemberLogRequestBase.BALANCE()
                .sessionID(sessionID)
                .description(description)
                .userAgent(userAgent == null ? null : userAgent.getBrowserType())
                .operateType(Memberlog.MemberOperateType.GETMEMBERORDERGOODSRETURNDETAIL.getOname())
                .build();
        sendMessageService.sendMemberLogMessage(JacksonHelper.toJson(base));

        Map<String, Object> rsMap = Maps.newHashMap();
        rsMap.put("data", memberOrderGoodsReturnDetailDto);
        return MessagePacket.newSuccess(rsMap, "getMemberOrderGoodsReturnDetail success!");
    }

}
