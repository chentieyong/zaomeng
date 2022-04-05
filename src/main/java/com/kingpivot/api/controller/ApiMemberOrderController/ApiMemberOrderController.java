package com.kingpivot.api.controller.ApiMemberOrderController;

import com.google.common.collect.Maps;
import com.kingpivot.api.dto.memberOrder.MemberOrderDetailDto;
import com.kingpivot.api.dto.memberOrder.MemberOrderDetailGoodsListDto;
import com.kingpivot.api.dto.memberOrder.MemberOrderListDto;
import com.kingpivot.api.dto.memberOrder.MemberOrderListGoodsListDto;
import com.kingpivot.base.cart.service.CartService;
import com.kingpivot.base.cartGoods.model.CartGoods;
import com.kingpivot.base.cartGoods.service.CartGoodsService;
import com.kingpivot.base.config.Config;
import com.kingpivot.base.config.RedisKey;
import com.kingpivot.base.config.UserAgent;
import com.kingpivot.base.discuss.model.Discuss;
import com.kingpivot.base.discuss.service.DiscussService;
import com.kingpivot.base.goodsShop.model.GoodsShop;
import com.kingpivot.base.goodsShop.service.GoodsShopService;
import com.kingpivot.base.member.model.Member;
import com.kingpivot.base.memberBonus.model.MemberBonus;
import com.kingpivot.base.memberBonus.service.MemberBonusService;
import com.kingpivot.base.memberOrder.model.MemberOrder;
import com.kingpivot.base.memberOrder.service.MemberOrderService;
import com.kingpivot.base.memberOrderGoods.model.MemberOrderGoods;
import com.kingpivot.base.memberOrderGoods.service.MemberOrderGoodsService;
import com.kingpivot.base.memberlog.model.Memberlog;
import com.kingpivot.base.support.MemberLogDTO;
import com.kingpivot.common.KingBase;
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
import java.util.*;

@RequestMapping("/api")
@RestController
@Api(description = "会员订单管理接口")
public class ApiMemberOrderController extends ApiBaseController {
    @Resource
    private SendMessageService sendMessageService;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private GoodsShopService goodsShopService;
    @Autowired
    private MemberOrderService memberOrderService;
    @Autowired
    private CartService cartService;
    @Autowired
    private KingBase kingBase;
    @Autowired
    private CartGoodsService cartGoodsService;
    @Autowired
    private MemberBonusService memberBonusService;
    @Autowired
    private MemberOrderGoodsService memberOrderGoodsService;
    @Autowired
    private DiscussService discussService;

    @ApiOperation(value = "店铺商品生成订单", notes = "店铺商品生成订单")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "sessionID", value = "登录标识", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "goodsShopID", value = "店铺商品id", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "qty", value = "数量", dataType = "int"),
            @ApiImplicitParam(paramType = "query", name = "objectFeatureItemID1", value = "对象特征选项1", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "contactName", value = "联系人", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "contactPhone", value = "联系电话", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "address", value = "地址", dataType = "String")})
    @RequestMapping(value = "/createMemberOrder")
    public MessagePacket createMemberOrder(HttpServletRequest request) {
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

        String sendType = request.getParameter("sendType");
        if(StringUtils.isEmpty(sendType)){
            sendType = "1";
        }

        String contactName = request.getParameter("contactName");
        if (sendType.equals("1") && StringUtils.isEmpty(contactName)) {
            return MessagePacket.newFail(MessageHeader.Code.contactNameIsNull, "联系人不能为空");
        }
        String contactPhone = request.getParameter("contactPhone");
        if (sendType.equals("1") && StringUtils.isEmpty(contactPhone)) {
            return MessagePacket.newFail(MessageHeader.Code.contactPhoneIsNull, "联系电话不能为空");
        }
        String address = request.getParameter("address");
        if (sendType.equals("1") && StringUtils.isEmpty(address)) {
            return MessagePacket.newFail(MessageHeader.Code.addressIsNull, "地址不能为空");
        }

        String goodsShopID = request.getParameter("goodsShopID");
        if (StringUtils.isEmpty(goodsShopID)) {
            return MessagePacket.newFail(MessageHeader.Code.goodsShopIdIsNull, "goodsShopID不能为空");
        }

        GoodsShop goodsShop = goodsShopService.findById(goodsShopID);
        if (goodsShop == null) {
            return MessagePacket.newFail(MessageHeader.Code.goodsShopIdIsError, "goodsShopID不正确");
        }

        String qty = request.getParameter("qty");

        if (StringUtils.isEmpty(qty)) {
            qty = "1";
        }

        String objectFeatureItemID1 = request.getParameter("objectFeatureItemID1");
        String memberBonusID = request.getParameter("memberBonusID");
        String orderType = request.getParameter("orderType");

        String memberOrderID = memberOrderService.createMemberOrder(member, goodsShop, objectFeatureItemID1,
                Integer.parseInt(qty), contactName, contactPhone, address, memberBonusID, orderType, sendType);

        String description = String.format("%s店铺商品生成订单", member.getName());

        UserAgent userAgent = UserAgentUtil.getUserAgent(request.getHeader("user-agent"));
        MemberLogRequestBase base = MemberLogRequestBase.BALANCE()
                .sessionID(sessionID)
                .description(description)
                .userAgent(userAgent == null ? null : userAgent.getBrowserType())
                .operateType(Memberlog.MemberOperateType.CREATEMEMBERORDER.getOname())
                .build();

        sendMessageService.sendMemberLogMessage(JacksonHelper.toJson(base));

        Map<String, Object> rsMap = Maps.newHashMap();
        rsMap.put("data", memberOrderID);

        return MessagePacket.newSuccess(rsMap, "createMemberOrder success!");
    }

    @ApiOperation(value = "购物车生成订单-单店铺版本", notes = "购物车生成订单-单店铺版本")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "sessionID", value = "登录标识", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "contactName", value = "联系人", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "contactPhone", value = "联系电话", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "address", value = "地址", dataType = "String")})
    @RequestMapping(value = "/createMemberOrderFromCart")
    public MessagePacket createMemberOrderFromCart(HttpServletRequest request) {
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

        String sendType = request.getParameter("sendType");
        if(StringUtils.isEmpty(sendType)){
            sendType = "1";
        }
        String contactName = request.getParameter("contactName");
        if (sendType.equals("1") && StringUtils.isEmpty(contactName)) {
            return MessagePacket.newFail(MessageHeader.Code.contactNameIsNull, "联系人不能为空");
        }
        String contactPhone = request.getParameter("contactPhone");
        if (sendType.equals("1") && StringUtils.isEmpty(contactPhone)) {
            return MessagePacket.newFail(MessageHeader.Code.contactPhoneIsNull, "联系电话不能为空");
        }
        String address = request.getParameter("address");
        if (sendType.equals("1") && StringUtils.isEmpty(address)) {
            return MessagePacket.newFail(MessageHeader.Code.addressIsNull, "地址不能为空");
        }

        String cartID = cartService.getCartIdByMemberID(member.getId());
        if (cartID == null) {
            kingBase.insertCart(member);
            return MessagePacket.newFail(MessageHeader.Code.illegalParameter, "购物车为空");
        }

        String memberBonusID = request.getParameter("memberBonusID");
        List<CartGoods> cartGoodsList = cartGoodsService.getCartGoodsListByCartID(cartID, 1);
        if (cartGoodsList.isEmpty()) {
            return MessagePacket.newFail(MessageHeader.Code.illegalParameter, "购物车为空");
        }
        String memberOrderID = memberOrderService.createMemberOrderFromCart(cartGoodsList, member, contactName, contactPhone, address, memberBonusID, sendType);

        String description = String.format("%s店铺商品生成订单", member.getName());

        UserAgent userAgent = UserAgentUtil.getUserAgent(request.getHeader("user-agent"));
        MemberLogRequestBase base = MemberLogRequestBase.BALANCE()
                .sessionID(sessionID)
                .description(description)
                .userAgent(userAgent == null ? null : userAgent.getBrowserType())
                .operateType(Memberlog.MemberOperateType.CREATEMEMBERORDER.getOname())
                .build();

        sendMessageService.sendMemberLogMessage(JacksonHelper.toJson(base));

        Map<String, Object> rsMap = Maps.newHashMap();
        rsMap.put("data", memberOrderID);

        return MessagePacket.newSuccess(rsMap, "createMemberOrder success!");
    }

    @ApiOperation(value = "获取会员订单列表", notes = "获取会员订单列表")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "sessionID", value = "登录标识", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "currentPage", value = "分页，页码从1开始", dataType = "int"),
            @ApiImplicitParam(paramType = "query", name = "pageNumber", value = "每一页大小", dataType = "int"),
            @ApiImplicitParam(paramType = "query", name = "status", value = "状态", dataType = "int")})
    @RequestMapping(value = "/getMemberOrderList")
    public MessagePacket getMemberOrderList(HttpServletRequest request) {
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
        paramMap.put("orderType", 1);
        paramMap.put("status:ne", 2);
        String status = request.getParameter("status");
        if (StringUtils.isNotBlank(status)) {
            paramMap.put("status", Integer.parseInt(status));
        }
        List<Sort.Order> orders = new ArrayList<Sort.Order>();
        orders.add(new Sort.Order(Sort.Direction.DESC, "createdTime"));
        Object currentPage = request.getParameter("currentPage");
        Object pageNumber = request.getParameter("pageNumber");

        TPage page = ApiPageUtil.makePage(currentPage, pageNumber);

        Pageable pageable = new PageRequest(page.getOffset(), page.getPageSize(), new Sort(orders));

        Page<MemberOrder> rs = memberOrderService.list(paramMap, pageable);

        List<MemberOrderListDto> list = null;
        if (rs != null && rs.getSize() != 0) {
            list = BeanMapper.mapList(rs.getContent(), MemberOrderListDto.class);

            List<MemberOrderListGoodsListDto> memberOrderGoodsListDtoList = null;
            List<MemberOrderGoods> memberOrderGoodsList = null;
            for (MemberOrderListDto memberOrderListDto : list) {
                memberOrderGoodsList = memberOrderGoodsService.getMemberOrderGoodsByMemberOrderID(memberOrderListDto.getId());
                if (memberOrderGoodsList != null) {
                    memberOrderGoodsListDtoList = BeanMapper.mapList(memberOrderGoodsList, MemberOrderListGoodsListDto.class);
                    memberOrderListDto.setGoodsList(memberOrderGoodsListDtoList);
                }
            }

            page.setTotalSize((int) rs.getTotalElements());
        }

        String description = String.format("%s获取会员订单列表", member.getName());

        UserAgent userAgent = UserAgentUtil.getUserAgent(request.getHeader("user-agent"));
        MemberLogRequestBase base = MemberLogRequestBase.BALANCE()
                .sessionID(sessionID)
                .description(description)
                .userAgent(userAgent == null ? null : userAgent.getBrowserType())
                .operateType(Memberlog.MemberOperateType.GETMEMBERORDERLIST.getOname())
                .build();

        sendMessageService.sendMemberLogMessage(JacksonHelper.toJson(base));

        Map<String, Object> rsMap = Maps.newHashMap();
        MessagePage messagePage = new MessagePage(page, list);
        rsMap.put("data", messagePage);
        return MessagePacket.newSuccess(rsMap, "getMemberOrderList success!");
    }

    @ApiOperation(value = "获取会员订单详情", notes = "获取会员订单详情")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "sessionID", value = "登录标识", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "memberOrderID", value = "会员订单id", dataType = "String")})
    @RequestMapping(value = "/getMemberOrderDetail")
    public MessagePacket getMemberOrderDetail(HttpServletRequest request) {
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

        String memberOrderID = request.getParameter("memberOrderID");
        if (StringUtils.isEmpty(memberOrderID)) {
            return MessagePacket.newFail(MessageHeader.Code.memberOrderIDIsNull, "memberOrderID为空");
        }

        MemberOrder memberOrder = memberOrderService.findById(memberOrderID);

        if (memberOrder == null) {
            return MessagePacket.newFail(MessageHeader.Code.memberOrderIDIsError, "memberOrderID不正确");
        }

        MemberOrderDetailDto memberOrderDetailDto = BeanMapper.map(memberOrder, MemberOrderDetailDto.class);
        List<MemberOrderGoods> memberOrderGoodsList = memberOrderGoodsService.getMemberOrderGoodsByMemberOrderID(memberOrderDetailDto.getId());
        if (memberOrderGoodsList != null && !memberOrderGoodsList.isEmpty()) {
            List<MemberOrderDetailGoodsListDto> memberOrderDetailGoodsListDtos = BeanMapper.mapList(memberOrderGoodsList, MemberOrderDetailGoodsListDto.class);
            memberOrderDetailDto.setGoodsList(memberOrderDetailGoodsListDtos);
        }

        String description = String.format("%s获取会员订单详情", member.getName());

        UserAgent userAgent = UserAgentUtil.getUserAgent(request.getHeader("user-agent"));
        MemberLogRequestBase base = MemberLogRequestBase.BALANCE()
                .sessionID(sessionID)
                .description(description)
                .userAgent(userAgent == null ? null : userAgent.getBrowserType())
                .operateType(Memberlog.MemberOperateType.GETMEMBERORDERDETAIL.getOname())
                .build();

        sendMessageService.sendMemberLogMessage(JacksonHelper.toJson(base));

        Map<String, Object> rsMap = Maps.newHashMap();
        rsMap.put("data", memberOrderDetailDto);
        return MessagePacket.newSuccess(rsMap, "getMemberOrderDetail success!");
    }

    /**
     * 取消订单
     *
     * @param request
     * @return
     */
    @ApiOperation(value = "取消订单", notes = "获取会员订单详情")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "sessionID", value = "登录标识", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "memberOrderID", value = "会员订单id", dataType = "String")})
    @RequestMapping(value = "/cancelMemberOrder")
    public MessagePacket cancelMemberOrder(HttpServletRequest request) {
        String sessionID = request.getParameter("sessionID");
        String memberOrderID = request.getParameter("memberOrderID");
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
        if (StringUtils.isEmpty(memberOrderID)) {
            return MessagePacket.newFail(MessageHeader.Code.memberOrderIDIsNull, "memberOrderID不能为空");
        }
        MemberOrder memberOrder = memberOrderService.findById(memberOrderID);
        if (memberOrder == null) {
            return MessagePacket.newFail(MessageHeader.Code.memberOrderIDIsError, "memberOrderID不正确");
        }
        memberOrder.setCancelTime(new Timestamp(System.currentTimeMillis()));
        memberOrder.setStatus(2);
        memberOrderService.save(memberOrder);

        //红包还原
        memberBonusService.initMemberBonusByMemberOrderID(memberOrderID);

        //商品数据库存还原
        GoodsShop goodsShop = null;
        List<MemberOrderGoods> memberOrderGoodsList = memberOrderGoodsService.getMemberOrderGoodsByMemberOrderID(memberOrderID);
        for (MemberOrderGoods memberOrderGoods : memberOrderGoodsList) {
            if (memberOrderGoods.getGoodsShop() != null) {
                goodsShop = memberOrderGoods.getGoodsShop();
                goodsShop.setStockOut(goodsShop.getStockOut() - memberOrderGoods.getQTY());
                goodsShop.setStockNumber(goodsShop.getStockNumber() + memberOrderGoods.getQTY());
                goodsShopService.save(goodsShop);
            }
        }

        String description = String.format("%s取消订单", member.getName());
        UserAgent userAgent = UserAgentUtil.getUserAgent(request.getHeader("user-agent"));
        MemberLogRequestBase base = MemberLogRequestBase.BALANCE()
                .sessionID(sessionID)
                .description(description)
                .userAgent(userAgent == null ? null : userAgent.getBrowserType())
                .operateType(Memberlog.MemberOperateType.CANCELMEMBERORDER.getOname())
                .build();
        sendMessageService.sendMemberLogMessage(JacksonHelper.toJson(base));

        Map<String, Object> rsMap = Maps.newHashMap();
        rsMap.put("data", TimeTest.toDateTimeFormat(memberOrder.getCancelTime()));

        return MessagePacket.newSuccess(rsMap, "cancelMemberOrder success!");
    }


    /**
     * 申请订单退单
     *
     * @param request
     * @return
     */
    @ApiOperation(value = "申请订单退单", notes = "申请订单退单")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "sessionID", value = "登录标识", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "memberOrderID", value = "会员订单id", dataType = "String")})
    @RequestMapping(value = "/applyReturnMemberOrder")
    public MessagePacket applyReturnMemberOrder(HttpServletRequest request) {
        String sessionID = request.getParameter("sessionID");
        String memberOrderID = request.getParameter("memberOrderID");
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
        if (StringUtils.isEmpty(memberOrderID)) {
            return MessagePacket.newFail(MessageHeader.Code.memberOrderIDIsNull, "memberOrderID不能为空");
        }
        MemberOrder memberOrder = memberOrderService.findById(memberOrderID);
        if (memberOrder == null) {
            return MessagePacket.newFail(MessageHeader.Code.memberOrderIDIsError, "memberOrderID不正确");
        }
        if (memberOrder.getStatus() != 4) {
            return MessagePacket.newFail(MessageHeader.Code.statusIsError, "状态不正确");
        }
        memberOrder.setStatus(14);
        memberOrderService.save(memberOrder);

        String description = String.format("%s申请订单退单", member.getName());
        UserAgent userAgent = UserAgentUtil.getUserAgent(request.getHeader("user-agent"));
        MemberLogRequestBase base = MemberLogRequestBase.BALANCE()
                .sessionID(sessionID)
                .description(description)
                .userAgent(userAgent == null ? null : userAgent.getBrowserType())
                .operateType(Memberlog.MemberOperateType.APPLYRETURNMEMBERORDER.getOname())
                .build();
        sendMessageService.sendMemberLogMessage(JacksonHelper.toJson(base));

        Map<String, Object> rsMap = Maps.newHashMap();
        rsMap.put("data", TimeTest.toDateTimeFormat(new Timestamp(System.currentTimeMillis())));

        return MessagePacket.newSuccess(rsMap, "applyReturnMemberOrder success!");
    }

    /**
     * 确认收货
     *
     * @param request
     * @return
     */
    @ApiOperation(value = "确认收货", notes = "确认收货")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "sessionID", value = "登录标识", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "memberOrderID", value = "会员订单id", dataType = "String")})
    @RequestMapping(value = "/confirmMemberOrder")
    public MessagePacket confirmMemberOrder(HttpServletRequest request) {
        String sessionID = request.getParameter("sessionID");
        String memberOrderID = request.getParameter("memberOrderID");
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
        if (StringUtils.isEmpty(memberOrderID)) {
            return MessagePacket.newFail(MessageHeader.Code.memberOrderIDIsNull, "memberOrderID不能为空");
        }
        MemberOrder memberOrder = memberOrderService.findById(memberOrderID);
        if (memberOrder == null) {
            return MessagePacket.newFail(MessageHeader.Code.memberOrderIDIsError, "memberOrderID不正确");
        }
        if (memberOrder.getStatus() != 6) {
            return MessagePacket.newFail(MessageHeader.Code.statusIsError, "状态不正确");
        }
        memberOrder.setStatus(8);
        memberOrder.setGetGoodsTime(new Timestamp(System.currentTimeMillis()));
        memberOrderService.save(memberOrder);

        String description = String.format("%s确认收货", member.getName());
        UserAgent userAgent = UserAgentUtil.getUserAgent(request.getHeader("user-agent"));
        MemberLogRequestBase base = MemberLogRequestBase.BALANCE()
                .sessionID(sessionID)
                .description(description)
                .userAgent(userAgent == null ? null : userAgent.getBrowserType())
                .operateType(Memberlog.MemberOperateType.CONFIRMMEMBERORDER.getOname())
                .build();
        sendMessageService.sendMemberLogMessage(JacksonHelper.toJson(base));

        Map<String, Object> rsMap = Maps.newHashMap();
        rsMap.put("data", TimeTest.toDateTimeFormat(memberOrder.getGetGoodsTime()));

        return MessagePacket.newSuccess(rsMap, "confirmMemberOrder success!");
    }

    /**
     * 评价一个订单商品
     *
     * @param request
     * @return
     */
    @ApiOperation(value = "评价一个订单商品", notes = "评价一个订单商品")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "sessionID", value = "登录标识", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "memberOrderID", value = "会员订单id", dataType = "String")})
    @RequestMapping(value = "/discussMemberOrderGoods")
    public MessagePacket discussMemberOrderGoods(HttpServletRequest request) {
        String sessionID = request.getParameter("sessionID");
        String memberOrderGoodsID = request.getParameter("memberOrderGoodsID");
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
        String content = request.getParameter("content");
        if (StringUtils.isEmpty(content)) {
            return MessagePacket.newFail(MessageHeader.Code.contentIsNull, "评论内容不能为空");
        }
        if (StringUtils.isEmpty(memberOrderGoodsID)) {
            return MessagePacket.newFail(MessageHeader.Code.memberOrderIDIsNull, "memberOrderID不能为空");
        }
        MemberOrderGoods memberOrderGoods = memberOrderGoodsService.findById(memberOrderGoodsID);
        if (memberOrderGoods == null) {
            return MessagePacket.newFail(MessageHeader.Code.memberOrderGoodsIDIsError, "memberOrderGoodsID不正确");
        }
        if (memberOrderGoods.getStatus() != 4) {
            return MessagePacket.newFail(MessageHeader.Code.statusIsError, "状态不正确");
        }

        Discuss discuss = new Discuss();
        discuss.setDescription(content);
        discuss.setApplicationID(member.getApplicationID());
        discuss.setMemberID(member.getId());
        discuss.setName(String.format("%s评论%s", member.getName(), memberOrderGoods.getName()));
        discuss.setObjectDefineID(Config.GOODSSHOP_OBJECTDEFINEID);
        discuss.setObjectID(memberOrderGoods.getGoodsShopID());
        discuss.setObjectName(memberOrderGoods.getName());
        discussService.save(discuss);

        memberOrderGoods.setStatus(5);
        memberOrderGoodsService.save(memberOrderGoods);

        int unFinishCount = memberOrderGoodsService.getUnFinishCount(memberOrderGoods.getMemberOrderID());
        if (unFinishCount == 0) {
            memberOrderService.updateMemberOrderStatus(memberOrderGoods.getMemberOrderID(), 9);
        }

        String description = String.format("%s评价一个订单商品", member.getName());
        UserAgent userAgent = UserAgentUtil.getUserAgent(request.getHeader("user-agent"));
        MemberLogRequestBase base = MemberLogRequestBase.BALANCE()
                .sessionID(sessionID)
                .description(description)
                .userAgent(userAgent == null ? null : userAgent.getBrowserType())
                .operateType(Memberlog.MemberOperateType.DISCUSSMEMBERORDERGOODS.getOname())
                .build();
        sendMessageService.sendMemberLogMessage(JacksonHelper.toJson(base));

        Map<String, Object> rsMap = Maps.newHashMap();
        rsMap.put("data", discuss.getId());

        return MessagePacket.newSuccess(rsMap, "discussMemberOrderGoods success!");
    }
}
