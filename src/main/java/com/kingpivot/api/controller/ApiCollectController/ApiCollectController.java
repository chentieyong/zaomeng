package com.kingpivot.api.controller.ApiCollectController;

import com.google.common.collect.Maps;
import com.kingpivot.api.dto.collect.CollectGoodsShopListDto;
import com.kingpivot.base.collect.model.Collect;
import com.kingpivot.base.collect.service.CollectService;
import com.kingpivot.base.config.Config;
import com.kingpivot.base.config.RedisKey;
import com.kingpivot.base.config.UserAgent;
import com.kingpivot.base.goodsShop.model.GoodsShop;
import com.kingpivot.base.goodsShop.service.GoodsShopService;
import com.kingpivot.base.member.model.Member;
import com.kingpivot.base.memberRank.service.MemberRankService;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequestMapping("/api")
@RestController
@Api(description = "收藏管理接口")
public class ApiCollectController extends ApiBaseController {

    @Resource
    private SendMessageService sendMessageService;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private GoodsShopService goodsShopService;
    @Autowired
    private MemberRankService memberRankService;
    @Autowired
    private CollectService collectService;

    @ApiOperation(value = "加入收藏", notes = "加入收藏")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "sessionID", value = "登录标识", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "objectID", value = "对象id", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "objectName", value = "对象名", dataType = "int"),
            @ApiImplicitParam(paramType = "query", name = "objectDefineID", value = "对象定义id", dataType = "String")})
    @RequestMapping(value = "/addCollect")
    public MessagePacket addCollect(HttpServletRequest request) {
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
        if(StringUtils.isEmpty(objectID)){
            return MessagePacket.newFail(MessageHeader.Code.objectIdIsNull, "objectID不能为空");
        }
        String objectName = request.getParameter("objectName");
        if(StringUtils.isEmpty(objectName)){
            return MessagePacket.newFail(MessageHeader.Code.objectNameIsNull, "objectName不能为空");
        }
        String objectDefineID = request.getParameter("objectDefineID");
        if(StringUtils.isEmpty(objectDefineID)){
            return MessagePacket.newFail(MessageHeader.Code.objectDefineIDIsNull, "objectDefineID不能为空");
        }

        Collect collect = new Collect();
        collect.setApplicationID(member.getApplicationID());
        collect.setMemberID(member.getId());
        collect.setName(String.format("%s收藏%s",member.getName(),objectName));
        collect.setObjectDefineID(objectDefineID);
        collect.setObjectID(objectID);
        collect.setObjectName(objectName);
        collectService.save(collect);

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
        rsMap.put("collectID", collect.getId());

        return MessagePacket.newSuccess(rsMap, "addCollect success!");
    }

    @ApiOperation(value = "删除收藏记录", notes = "删除收藏记录")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "sessionID", value = "登录标识", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "collectID", value = "收藏id", dataType = "String")})
    @RequestMapping(value = "/removeCollect")
    public MessagePacket removeCollect(HttpServletRequest request) {
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

        String collectID = request.getParameter("collectID");
        if (StringUtils.isEmpty(collectID)) {
            return MessagePacket.newFail(MessageHeader.Code.collectIDIsNull, "collectID不能为空");
        }

        Collect collect = collectService.findById(collectID);
        if (collect == null) {
            return MessagePacket.newFail(MessageHeader.Code.collectIDIsError, "collectID不正确");
        }

        collectService.del(collect);

        String description = String.format("%s删除收藏", member.getName());

        UserAgent userAgent = UserAgentUtil.getUserAgent(request.getHeader("user-agent"));
        MemberLogRequestBase base = MemberLogRequestBase.BALANCE()
                .sessionID(sessionID)
                .description(description)
                .userAgent(userAgent == null ? null : userAgent.getBrowserType())
                .operateType(Memberlog.MemberOperateType.REMOVECOLLECT.getOname())
                .build();

        sendMessageService.sendMemberLogMessage(JacksonHelper.toJson(base));

        Map<String, Object> rsMap = Maps.newHashMap();
        rsMap.put("removeTime", TimeTest.getNowDateFormat());

        return MessagePacket.newSuccess(rsMap, "removeCollect success!");
    }

    @ApiOperation(value = "获取收藏列表", notes = "获取收藏列表")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "sessionID", value = "登录标识", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "objectDefineID", value = "对象定义id", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "currentPage", value = "分页，页码从1开始", dataType = "int"),
            @ApiImplicitParam(paramType = "query", name = "pageNumber", value = "每一页大小", dataType = "int")})
    @RequestMapping(value = "/getCollectList")
    public MessagePacket getCollectList(HttpServletRequest request) {
        String objectDefineID = request.getParameter("objectDefineID");

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

        if (StringUtils.isEmpty(objectDefineID)) {
            return MessagePacket.newFail(MessageHeader.Code.objectDefineIDIsNull, "objectDefineID不能为空");
        }

        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("isValid", Constants.ISVALID_YES);
        paramMap.put("isLock", Constants.ISLOCK_NO);
        paramMap.put("memberID", member.getId());
        paramMap.put("objectDefineID", objectDefineID);

        List<Sort.Order> orders = new ArrayList<Sort.Order>();
        orders.add(new Sort.Order(Sort.Direction.DESC, "createdTime"));

        Object currentPage = request.getParameter("currentPage");
        Object pageNumber = request.getParameter("pageNumber");

        TPage page = ApiPageUtil.makePage(currentPage, pageNumber);

        Pageable pageable = new PageRequest(page.getStart(), page.getPageSize(), new Sort(orders));

        Page<Collect> rs = collectService.list(paramMap, pageable);
        Map<String, Object> rsMap = Maps.newHashMap();
        MessagePage messagePage = null;
        if (rs != null && rs.getSize() != 0) {
            switch (objectDefineID) {
                case Config.GOODSSHOP_OBJECTDEFINEID://店铺商品
                    List<CollectGoodsShopListDto> goodsShopList = new ArrayList<>();
                    CollectGoodsShopListDto collectGoodsShopListDto = null;
                    GoodsShop goodsShop = null;
                    for (Collect collect : rs.getContent()) {
                        if (StringUtils.isNotBlank(collect.getObjectID())) {
                            goodsShop = goodsShopService.findById(collect.getObjectID());
                            if (goodsShop != null) {
                                collectGoodsShopListDto = new CollectGoodsShopListDto();
                                collectGoodsShopListDto.setCollectID(collect.getId());
                                collectGoodsShopListDto.setObjectID(goodsShop.getId());
                                collectGoodsShopListDto.setObjectName(goodsShop.getName());
                                collectGoodsShopListDto.setListImage(goodsShop.getLittleImage());
                                collectGoodsShopListDto.setShowPrice(goodsShop.getRealPrice());
                                collectGoodsShopListDto.setStockNumber(goodsShop.getStockNumber());
                                collectGoodsShopListDto.setStockOut(goodsShop.getStockOut());
                                double rate = memberRankService.getDepositeRateByMemberId(member.getId());
                                if(rate!=0d){
                                    collectGoodsShopListDto.setShowPrice(NumberUtils.keepPrecision(rate * goodsShop.getRealPrice(), 2));
                                }
                                goodsShopList.add(collectGoodsShopListDto);
                            }
                        }
                    }
                    messagePage = new MessagePage(page, goodsShopList);
                    break;
                default:
                    messagePage = new MessagePage(page, new ArrayList());
                    break;
            }
            page.setTotalSize(rs.getSize());
        }
        rsMap.put("data", messagePage);

        String description = String.format("%s获取收藏列表", member.getName());

        UserAgent userAgent = UserAgentUtil.getUserAgent(request.getHeader("user-agent"));
        MemberLogRequestBase base = MemberLogRequestBase.BALANCE()
                .sessionID(sessionID)
                .description(description)
                .userAgent(userAgent == null ? null : userAgent.getBrowserType())
                .operateType(Memberlog.MemberOperateType.GETMESSAGELIST.getOname())
                .build();

        sendMessageService.sendMemberLogMessage(JacksonHelper.toJson(base));

        return MessagePacket.newSuccess(rsMap, "getCollectList success!");
    }
}
