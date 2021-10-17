package com.kingpivot.api.controller.ApiCartController;

import com.google.common.collect.Maps;
import com.kingpivot.api.dto.cart.CartGoodsListDto;
import com.kingpivot.api.dto.cart.GoodsListDto;
import com.kingpivot.base.cart.service.CartService;
import com.kingpivot.base.cartGoods.model.CartGoods;
import com.kingpivot.base.cartGoods.service.CartGoodsService;
import com.kingpivot.base.config.RedisKey;
import com.kingpivot.base.config.UserAgent;
import com.kingpivot.base.goodsShop.model.GoodsShop;
import com.kingpivot.base.goodsShop.service.GoodsShopService;
import com.kingpivot.base.member.model.Member;
import com.kingpivot.base.memberRank.service.MemberRankService;
import com.kingpivot.base.memberlog.model.Memberlog;
import com.kingpivot.base.objectFeatureData.model.ObjectFeatureData;
import com.kingpivot.base.objectFeatureData.service.ObjectFeatureDataService;
import com.kingpivot.base.rank.model.Rank;
import com.kingpivot.base.rank.service.RankService;
import com.kingpivot.base.shop.service.ShopService;
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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

@RequestMapping("/api")
@RestController
@Api(description = "购物车管理接口")
public class ApiCartController extends ApiBaseController {
    @Resource
    private SendMessageService sendMessageService;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private GoodsShopService goodsShopService;
    @Autowired
    private CartService cartService;
    @Autowired
    private CartGoodsService cartGoodsService;
    @Autowired
    private KingBase kingBase;
    @Autowired
    private ObjectFeatureDataService objectFeatureDataService;
    @Autowired
    private RankService rankService;
    @Autowired
    private ShopService shopService;

    @ApiOperation(value = "商品加入购物车", notes = "商品加入购物车")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "sessionID", value = "登录标识", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "goodsShopID", value = "店铺商品id", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "qty", value = "数量", dataType = "int"),
            @ApiImplicitParam(paramType = "query", name = "objectFeatureItemID1", value = "对象特征选项1", dataType = "String")})
    @RequestMapping(value = "/addGoodsShopToCart")
    public MessagePacket addGoodsShopToCart(HttpServletRequest request) {
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

        String cartID = cartService.getCartIdByMemberID(member.getId());
        if (StringUtils.isEmpty(cartID)) {
            cartID = kingBase.insertCart(member);
        }

        if (StringUtils.isEmpty(objectFeatureItemID1)) {
            objectFeatureItemID1 = null;
        }
        CartGoods cartGoods = cartGoodsService.getCartGoodsByCartIDAndObjectFeatureItemID(cartID, goodsShopID, objectFeatureItemID1);

        double price = goodsShop.getRealPrice();

        if (StringUtils.isNotBlank(objectFeatureItemID1)) {
            ObjectFeatureData val = objectFeatureDataService.getObjectFetureData(goodsShopID, objectFeatureItemID1);
            if (val != null) {
                price = val.getRealPrice();
            }
        }

        if (cartGoods != null) {
            cartGoods.setQty(cartGoods.getQty() == null ? Math.abs(Integer.parseInt(qty)) : Math.abs(Integer.parseInt(qty)) + cartGoods.getQty().intValue());
            cartGoods.setPriceTotal(NumberUtils.keepPrecision(cartGoods.getQty() * cartGoods.getPriceNow(), 2));
            cartGoodsService.save(cartGoods);
        } else {
            cartGoods = new CartGoods();
            cartGoods.setName(goodsShop.getName());
            cartGoods.setDescription(goodsShop.getDescription());
            if (StringUtils.isNotBlank(objectFeatureItemID1)) {
                cartGoods.setObjectFeatureItemID1(objectFeatureItemID1);
            }
            cartGoods.setQty(Integer.parseInt(qty));
            cartGoods.setGoodsShopID(goodsShopID);
            cartGoods.setCartID(cartID);
            cartGoods.setDiscountRate(0);
            cartGoods.setIsSelected(1);
            cartGoods.setStandPrice(price);
            cartGoods.setStandPriceTotal(NumberUtils.keepPrecision(cartGoods.getStandPrice() * Integer.parseInt(qty), 2));
            cartGoods.setPriceNow(price);
            cartGoods.setShopID(goodsShop.getShopID());
            cartGoods.setCompanyID(goodsShop.getCompanyID());
            cartGoods.setPriceTotal(NumberUtils.keepPrecision(cartGoods.getPriceNow() * Integer.parseInt(qty), 2));
            cartGoodsService.save(cartGoods);
        }
        String description = String.format("%s店铺商品加入购物车", member.getName());

        UserAgent userAgent = UserAgentUtil.getUserAgent(request.getHeader("user-agent"));
        MemberLogRequestBase base = MemberLogRequestBase.BALANCE()
                .sessionID(sessionID)
                .description(description)
                .userAgent(userAgent == null ? null : userAgent.getBrowserType())
                .operateType(Memberlog.MemberOperateType.ADDGOODSSHOPTOCART.getOname())
                .build();

        sendMessageService.sendMemberLogMessage(JacksonHelper.toJson(base));

        Map<String, Object> rsMap = Maps.newHashMap();
        rsMap.put("data", TimeTest.getNowDateFormat());
        rsMap.put("qty", cartGoods.getQty());
        double priceTotal = cartGoodsService.getPriceTotalByCartID(cartGoods.getCartID());
        rsMap.put("priceTotal", NumberUtils.keepPrecision(priceTotal, 2));

        return MessagePacket.newSuccess(rsMap, "addGoodsShopToCart success!");
    }

    @ApiOperation(value = "更改购物车商品数量", notes = "更改购物车商品数量")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "sessionID", value = "登录标识", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "cartGoodsID", value = "购物车商品id", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "qty", value = "数量", dataType = "int")})
    @RequestMapping(value = "/updateCartGoodsNumber")
    public MessagePacket updateCartGoodsNumber(HttpServletRequest request) {
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

        String cartGoodsID = request.getParameter("cartGoodsID");
        if (StringUtils.isEmpty(cartGoodsID)) {
            return MessagePacket.newFail(MessageHeader.Code.cartGoodsIDIsNull, "cartGoodsID不能为空");
        }

        CartGoods cartGoods = cartGoodsService.findById(cartGoodsID);
        if (cartGoods == null) {
            return MessagePacket.newFail(MessageHeader.Code.cartGoodsIDIsNull, "cartGoodsID不正确");
        }

        String qty = request.getParameter("qty");
        if (StringUtils.isEmpty(qty)) {
            return MessagePacket.newFail(MessageHeader.Code.qtyIsNull, "qty不能为空");
        }

        cartGoods.setQty(Integer.parseInt(qty));
        cartGoods.setPriceTotal(NumberUtils.keepPrecision(cartGoods.getQty() * cartGoods.getPriceNow(), 2));
        cartGoodsService.save(cartGoods);

        String description = String.format("%s更改购物车商品数量", member.getName());

        UserAgent userAgent = UserAgentUtil.getUserAgent(request.getHeader("user-agent"));
        MemberLogRequestBase base = MemberLogRequestBase.BALANCE()
                .sessionID(sessionID)
                .description(description)
                .userAgent(userAgent == null ? null : userAgent.getBrowserType())
                .operateType(Memberlog.MemberOperateType.UPDATECARTGOODSNUMBER.getOname())
                .build();

        sendMessageService.sendMemberLogMessage(JacksonHelper.toJson(base));

        Map<String, Object> rsMap = Maps.newHashMap();
        rsMap.put("data", TimeTest.getNowDateFormat());
        rsMap.put("qty", cartGoods.getQty());
        double priceTotal = cartGoodsService.getPriceTotalByCartID(cartGoods.getCartID());
        rsMap.put("priceTotal", NumberUtils.keepPrecision(priceTotal, 2));

        return MessagePacket.newSuccess(rsMap, "updateCartGoodsNumber success!");
    }

    @ApiOperation(value = "删除购物车商品", notes = "删除购物车商品")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "sessionID", value = "登录标识", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "cartGoodsID", value = "购物车商品id", dataType = "String")})
    @RequestMapping(value = "/removeCartGoods")
    public MessagePacket removeCartGoods(HttpServletRequest request) {
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

        String cartGoodsID = request.getParameter("cartGoodsID");
        if (StringUtils.isEmpty(cartGoodsID)) {
            return MessagePacket.newFail(MessageHeader.Code.cartGoodsIDIsNull, "cartGoodsID不能为空");
        }

        String[] cartGoodsIDs = cartGoodsID.split(",");
        for (int i = 0; i < cartGoodsIDs.length; i++) {
            cartGoodsService.del(cartGoodsIDs[i]);
        }

        String description = String.format("%s删除购物车商品", member.getName());

        UserAgent userAgent = UserAgentUtil.getUserAgent(request.getHeader("user-agent"));
        MemberLogRequestBase base = MemberLogRequestBase.BALANCE()
                .sessionID(sessionID)
                .description(description)
                .userAgent(userAgent == null ? null : userAgent.getBrowserType())
                .operateType(Memberlog.MemberOperateType.REMOVECARTGOODS.getOname())
                .build();

        sendMessageService.sendMemberLogMessage(JacksonHelper.toJson(base));

        Map<String, Object> rsMap = Maps.newHashMap();
        rsMap.put("data", TimeTest.getNowDateFormat());
        String cartID = cartService.getCartIdByMemberID(member.getId());
        if (StringUtils.isNotBlank(cartID)) {
            double priceTotal = cartGoodsService.getPriceTotalByCartID(cartID);
            rsMap.put("priceTotal", NumberUtils.keepPrecision(priceTotal, 2));
        } else {
            rsMap.put("priceTotal", 0.00d);
        }
        return MessagePacket.newSuccess(rsMap, "removeCartGoods success!");
    }

    @ApiOperation(value = "勾选购物车商品", notes = "勾选购物车商品")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "sessionID", value = "登录标识", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "cartGoodsID", value = "购物车商品id", dataType = "String"),
            @ApiImplicitParam(paramType = "isSelect", name = "isSelect", value = "是否勾选", dataType = "int")})
    @RequestMapping(value = "/selectCartGoods")
    public MessagePacket selectCartGoods(HttpServletRequest request) {
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

        String isSelect = request.getParameter("isSelect");

        if (StringUtils.isEmpty(isSelect)) {
            return MessagePacket.newFail(MessageHeader.Code.illegalParameter, "isSelect不能为空");
        }

        String cartGoodsID = request.getParameter("cartGoodsID");
        if (StringUtils.isEmpty(cartGoodsID)) {
            return MessagePacket.newFail(MessageHeader.Code.cartGoodsIDIsNull, "cartGoodsID不能为空");
        }

        String[] cartGoodsIDs = cartGoodsID.split(",");
        CartGoods cartGoods = null;
        for (int i = 0; i < cartGoodsIDs.length; i++) {
            cartGoods = cartGoodsService.findById(cartGoodsIDs[i]);
            cartGoods.setIsSelected(Integer.parseInt(isSelect));
            cartGoodsService.save(cartGoods);
        }

        String description = String.format("%s勾选购物车商品", member.getName());

        UserAgent userAgent = UserAgentUtil.getUserAgent(request.getHeader("user-agent"));
        MemberLogRequestBase base = MemberLogRequestBase.BALANCE()
                .sessionID(sessionID)
                .description(description)
                .userAgent(userAgent == null ? null : userAgent.getBrowserType())
                .operateType(Memberlog.MemberOperateType.SELECTCARTGOODS.getOname())
                .build();

        sendMessageService.sendMemberLogMessage(JacksonHelper.toJson(base));

        Map<String, Object> rsMap = Maps.newHashMap();
        rsMap.put("data", TimeTest.getNowDateFormat());
        double priceTotal = cartGoodsService.getPriceTotalByCartID(cartGoods.getCartID());
        rsMap.put("priceTotal", NumberUtils.keepPrecision(priceTotal, 2));

        return MessagePacket.newSuccess(rsMap, "selectCartGoods success!");
    }

    @ApiOperation(value = "获取购物车商品-单店铺版本", notes = "获取购物车商品-单店铺版本")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "sessionID", value = "登录标识", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "currentPage", value = "分页，页码从1开始", dataType = "int"),
            @ApiImplicitParam(paramType = "query", name = "pageNumber", value = "每一页大小", dataType = "int")})
    @RequestMapping(value = "/getCartGoodsList")
    public MessagePacket getCartGoodsList(HttpServletRequest request) {
        String sessionID = request.getParameter("sessionID");
        String isSelect = request.getParameter("isSelect");
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

        String cartID = cartService.getCartIdByMemberID(member.getId());
        if (StringUtils.isEmpty(cartID)) {
            cartID = kingBase.insertCart(member);
        }
        if (StringUtils.isEmpty(isSelect)) {
            isSelect = "-1";
        }

        TPage page = ApiPageUtil.makePage(null, null);
        List<CartGoods> cartGoodsList = cartGoodsService.getCartGoodsListByCartID(cartID, Integer.parseInt(isSelect));
        List<CartGoodsListDto> list = null;
        if (cartGoodsList != null && cartGoodsList.size() != 0) {
            list = BeanMapper.mapList(cartGoodsList, CartGoodsListDto.class);
            page.setTotalSize(cartGoodsList.size());
        } else {
            list = new ArrayList<>();
            page.setTotalSize(0);
        }

        String description = String.format("%s获取购物车商品", member.getName());
        UserAgent userAgent = UserAgentUtil.getUserAgent(request.getHeader("user-agent"));
        MemberLogRequestBase base = MemberLogRequestBase.BALANCE()
                .sessionID(sessionID)
                .description(description)
                .userAgent(userAgent == null ? null : userAgent.getBrowserType())
                .operateType(Memberlog.MemberOperateType.GETCARTGOODSLIST.getOname())
                .build();
        sendMessageService.sendMemberLogMessage(JacksonHelper.toJson(base));

        Map<String, Object> rsMap = Maps.newHashMap();
        MessagePage messagePage = new MessagePage(page, list);
        rsMap.put("data", messagePage);
        double priceTotal = cartGoodsService.getPriceTotalByCartID(cartID);
        rsMap.put("priceTotal", NumberUtils.keepPrecision(priceTotal, 2));
        double priceAfterDiscount = priceTotal;
        if (StringUtils.isNotBlank(member.getRankID())) {
            Rank rank = rankService.findById(member.getRankID());
            if (rank != null && rank.getDepositeRate() != null && rank.getDepositeRate() != 0) {
                priceAfterDiscount = priceTotal * rank.getDepositeRate().doubleValue();
            }
        }
        rsMap.put("priceAfterDiscount", NumberUtils.keepPrecision(priceAfterDiscount, 2));
        rsMap.put("discountPrice", NumberUtils.keepPrecision(priceTotal - priceAfterDiscount, 2));
        return MessagePacket.newSuccess(rsMap, "getCartGoodsList success!");
    }

    @ApiOperation(value = "获取购物车列表-多店铺版本", notes = "获取购物车列表-多店铺版本")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "sessionID", value = "登录标识", dataType = "String")})
    @RequestMapping(value = "/getCartList")
    public MessagePacket getCartList(HttpServletRequest request) {
        String sessionID = request.getParameter("sessionID");
        String isSelect = request.getParameter("isSelect");
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

        String cartID = cartService.getCartIdByMemberID(member.getId());
        if (StringUtils.isEmpty(cartID)) {
            cartID = kingBase.insertCart(member);
        }
        if (StringUtils.isEmpty(isSelect)) {
            isSelect = "-1";
        }

        List<String> shopList = cartGoodsService.getSelectCartGoodsShopList(cartID, isSelect);
        if (shopList == null || shopList.isEmpty()) {
            return MessagePacket.newFail(MessageHeader.Code.shopListIsNull, "购物车商品为空");
        }

        List<CartGoodsListDto> list = new LinkedList<>();
        List<GoodsListDto> goodsList = null;
        List<CartGoods> cartGoodsList = null;
        CartGoodsListDto cartGoodsListDto = null;
        for (String val : shopList) {
            cartGoodsListDto = new CartGoodsListDto();
            cartGoodsListDto.setShopName(shopService.getNameById(val));
            cartGoodsList = cartGoodsService.getCartGoodsListByCartID(cartID, val, Integer.parseInt(isSelect));
            if (cartGoodsList != null) {
                goodsList = BeanMapper.mapList(cartGoodsList, GoodsListDto.class);
            } else {
                goodsList = new ArrayList<>();
            }
            cartGoodsListDto.setGoodsList(goodsList);
            list.add(cartGoodsListDto);
        }

        String description = String.format("%s获取购物车商品", member.getName());
        UserAgent userAgent = UserAgentUtil.getUserAgent(request.getHeader("user-agent"));
        MemberLogRequestBase base = MemberLogRequestBase.BALANCE()
                .sessionID(sessionID)
                .description(description)
                .userAgent(userAgent == null ? null : userAgent.getBrowserType())
                .operateType(Memberlog.MemberOperateType.GETCARTGOODSLIST.getOname())
                .build();
        sendMessageService.sendMemberLogMessage(JacksonHelper.toJson(base));

        Map<String, Object> rsMap = Maps.newHashMap();
        rsMap.put("data", list);
        return MessagePacket.newSuccess(rsMap, "getCartGoodsList success!");
    }

    @ApiOperation(value = "获取会员购物车商品数量", notes = "获取会员购物车商品数量")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "sessionID", value = "登录标识", dataType = "String")})
    @RequestMapping(value = "/getMemberCartGoodsNum")
    public MessagePacket getMemberCartGoodsNum(HttpServletRequest request) {
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

        String description = String.format("%s获取会员购物车商品数量", member.getName());

        UserAgent userAgent = UserAgentUtil.getUserAgent(request.getHeader("user-agent"));
        MemberLogRequestBase base = MemberLogRequestBase.BALANCE()
                .sessionID(sessionID)
                .description(description)
                .userAgent(userAgent == null ? null : userAgent.getBrowserType())
                .operateType(Memberlog.MemberOperateType.GETMEMBERCARTGOODSNUM.getOname())
                .build();

        sendMessageService.sendMemberLogMessage(JacksonHelper.toJson(base));

        int num = cartGoodsService.getMemberCartGoodsNum(member.getId());
        Map<String, Object> rsMap = Maps.newHashMap();
        rsMap.put("num", num);
        return MessagePacket.newSuccess(rsMap, "getMemberCartGoodsNum success!");
    }
}
