package com.kingpivot.api.controller.ApiCollectController;

import com.google.common.collect.Maps;
import com.kingpivot.api.dto.collect.CollectGoodsShopListDto;
import com.kingpivot.api.dto.collect.CollectListDto;
import com.kingpivot.api.dto.collect.CollectMemberListDto;
import com.kingpivot.api.dto.collect.ObjectCollectDto;
import com.kingpivot.api.dto.goodsShop.GoodsShopListDto;
import com.kingpivot.base.collect.model.Collect;
import com.kingpivot.base.collect.service.CollectService;
import com.kingpivot.base.config.Config;
import com.kingpivot.base.config.RedisKey;
import com.kingpivot.base.config.UserAgent;
import com.kingpivot.base.goodsShop.model.GoodsShop;
import com.kingpivot.base.goodsShop.service.GoodsShopService;
import com.kingpivot.base.member.model.Member;
import com.kingpivot.base.member.service.MemberService;
import com.kingpivot.base.memberRank.service.MemberRankService;
import com.kingpivot.base.memberlog.model.Memberlog;
import com.kingpivot.base.objectFeatureData.model.ObjectFeatureData;
import com.kingpivot.base.objectFeatureData.service.ObjectFeatureDataService;
import com.kingpivot.base.objectFeatureItem.service.ObjectFeatureItemService;
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
    private CollectService collectService;
    @Autowired
    private ObjectFeatureItemService objectFeatureItemService;
    @Autowired
    private ObjectFeatureDataService objectFeatureDataService;
    @Autowired
    private MemberService memberService;

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
        if (StringUtils.isEmpty(objectID)) {
            return MessagePacket.newFail(MessageHeader.Code.objectIdIsNull, "objectID不能为空");
        }

        String collectID = collectService.getCollectByObjectIDAndMemberID(objectID, member.getId());

        if (StringUtils.isNotBlank(collectID)) {
            return MessagePacket.newFail(MessageHeader.Code.memberIsCollect, "请勿重复收藏");
        }
        String objectName = request.getParameter("objectName");
        if (StringUtils.isEmpty(objectName)) {
            return MessagePacket.newFail(MessageHeader.Code.objectNameIsNull, "objectName不能为空");
        }
        String objectDefineID = request.getParameter("objectDefineID");
        if (StringUtils.isEmpty(objectDefineID)) {
            return MessagePacket.newFail(MessageHeader.Code.objectDefineIDIsNull, "objectDefineID不能为空");
        }

        Collect collect = new Collect();
        collect.setApplicationID(member.getApplicationID());
        collect.setMemberID(member.getId());
        collect.setName(String.format("%s收藏%s", member.getName(), objectName));
        collect.setObjectDefineID(objectDefineID);
        collect.setObjectID(objectID);
        collect.setObjectName(objectName);
        collectService.save(collect);

        String description = String.format("%s收藏%s", member.getName(), objectName);

        UserAgent userAgent = UserAgentUtil.getUserAgent(request.getHeader("user-agent"));
        MemberLogRequestBase base = MemberLogRequestBase.BALANCE()
                .sessionID(sessionID)
                .description(description)
                .userAgent(userAgent == null ? null : userAgent.getBrowserType())
                .operateType(Memberlog.MemberOperateType.ADDCOLLECT.getOname())
                .build();

        sendMessageService.sendMemberLogMessage(JacksonHelper.toJson(base));

        Map<String, Object> rsMap = Maps.newHashMap();
        rsMap.put("data", collect.getId());

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
        rsMap.put("data", TimeTest.getTimeStr());

        return MessagePacket.newSuccess(rsMap, "removeCollect success!");
    }

    @ApiOperation(value = "获取会员收藏列表", notes = "获取会员收藏列表")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "sessionID", value = "登录标识", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "objectDefineID", value = "对象定义id", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "currentPage", value = "分页，页码从1开始", dataType = "int"),
            @ApiImplicitParam(paramType = "query", name = "pageNumber", value = "每一页大小", dataType = "int")})
    @RequestMapping(value = "/getCollectList")
    public MessagePacket getCollectList(HttpServletRequest request) {
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
        paramMap.put("memberID", member.getId());
        String objectDefineID = request.getParameter("objectDefineID");
        if (StringUtils.isNotBlank(objectDefineID)) {
            paramMap.put("objectDefineID", objectDefineID);
        }

        List<Sort.Order> orders = new ArrayList<Sort.Order>();
        orders.add(new Sort.Order(Sort.Direction.DESC, "createdTime"));

        Object currentPage = request.getParameter("currentPage");
        Object pageNumber = request.getParameter("pageNumber");

        TPage page = ApiPageUtil.makePage(currentPage, pageNumber);

        Pageable pageable = new PageRequest(page.getOffset(), page.getPageSize(), new Sort(orders));

        Page<Collect> rs = collectService.list(paramMap, pageable);
        Map<String, Object> rsMap = Maps.newHashMap();
        MessagePage messagePage = null;
        if (rs != null && rs.getSize() != 0) {
            if (StringUtils.isNotBlank(objectDefineID)) {
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
                                    collectGoodsShopListDto.setUnitDescription(goodsShop.getUnitDescription());

                                    Object itemOjb = objectFeatureItemService.getDefaultFeatureItem(goodsShop.getId());
                                    if (itemOjb != null) {
                                        Object[] obj = (Object[]) itemOjb;
                                        if (obj != null) {
                                            collectGoodsShopListDto.setUnitDescription((String) obj[0]);
                                            collectGoodsShopListDto.setObjectFeatureItemID1((String) obj[1]);
                                            ObjectFeatureData objectFetureData = objectFeatureDataService.getObjectFetureData(goodsShop.getId(), (String) obj[1]);
                                            if (objectFetureData != null) {
                                                collectGoodsShopListDto.setShowPrice(objectFetureData.getRealPrice());
                                                collectGoodsShopListDto.setMemberPrice(objectFetureData.getMemberPrice());
                                            }
                                        }
                                    }
                                    goodsShopList.add(collectGoodsShopListDto);
                                }
                            }
                        }
                        page.setTotalSize((int) rs.getTotalElements());
                        messagePage = new MessagePage(page, goodsShopList);
                        break;
                    case Config.MEMBER_OBJECTDEFINEID:
                        page.setTotalSize((int) rs.getTotalElements());
                        List<CollectMemberListDto> collectMemberList = BeanMapper.mapList(rs.getContent(), CollectMemberListDto.class);
                        Member collectMember = null;
                        for (CollectMemberListDto obj : collectMemberList) {
                            collectMember = memberService.findById(obj.getObjectID());
                            if (collectMember != null) {
                                obj.setMemberID(collectMember.getId());
                                obj.setAvatarURL(collectMember.getAvatarURL());
                                obj.setMemberName(collectMember.getName());
                                obj.setCompanyName(collectMember.getCompanyName());
                                obj.setJobName(collectMember.getJobName());
                            }
                        }
                        messagePage = new MessagePage(page, collectMemberList);
                        break;
                    default:
                        page.setTotalSize((int) rs.getTotalElements());
                        List<CollectListDto> list = BeanMapper.mapList(rs.getContent(), CollectListDto.class);
                        messagePage = new MessagePage(page, list);
                        break;
                }
            } else {
                page.setTotalSize((int) rs.getTotalElements());
                List<CollectListDto> list = BeanMapper.mapList(rs.getContent(), CollectListDto.class);
                messagePage = new MessagePage(page, list);
            }
        } else {
            page.setTotalSize(0);
            messagePage = new MessagePage(page, new ArrayList());
        }
        rsMap.put("data", messagePage);

        String description = String.format("%s获取会员收藏列表", member.getName());

        UserAgent userAgent = UserAgentUtil.getUserAgent(request.getHeader("user-agent"));
        MemberLogRequestBase base = MemberLogRequestBase.BALANCE()
                .sessionID(sessionID)
                .description(description)
                .userAgent(userAgent == null ? null : userAgent.getBrowserType())
                .operateType(Memberlog.MemberOperateType.GETCOLLECTLIST.getOname())
                .build();

        sendMessageService.sendMemberLogMessage(JacksonHelper.toJson(base));

        return MessagePacket.newSuccess(rsMap, "getCollectList success!");
    }

    @ApiOperation(value = "获取对象收藏列表", notes = "获取对象收藏列表")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "sessionID", value = "登录标识", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "objectID", value = "对象id", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "currentPage", value = "分页，页码从1开始", dataType = "int"),
            @ApiImplicitParam(paramType = "query", name = "pageNumber", value = "每一页大小", dataType = "int")})
    @RequestMapping(value = "/getObjectCollectList")
    public MessagePacket getObjectCollectList(HttpServletRequest request) {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("isValid", Constants.ISVALID_YES);
        paramMap.put("isLock", Constants.ISLOCK_NO);
        String objectID = request.getParameter("objectID");
        if (StringUtils.isEmpty(objectID)) {
            return MessagePacket.newFail(MessageHeader.Code.objectIdIsNull, "对象id不能为空");
        }
        paramMap.put("objectID", objectID);
        List<Sort.Order> orders = new ArrayList<Sort.Order>();
        orders.add(new Sort.Order(Sort.Direction.DESC, "createdTime"));

        Object currentPage = request.getParameter("currentPage");
        Object pageNumber = request.getParameter("pageNumber");

        TPage page = ApiPageUtil.makePage(currentPage, pageNumber);

        Pageable pageable = new PageRequest(page.getOffset(), page.getPageSize(), new Sort(orders));

        Page<Collect> rs = collectService.list(paramMap, pageable);
        Map<String, Object> rsMap = Maps.newHashMap();
        MessagePage messagePage = null;
        if (rs != null && rs.getSize() != 0) {
            page.setTotalSize((int) rs.getTotalElements());
            List<ObjectCollectDto> list = BeanMapper.mapList(rs.getContent(), ObjectCollectDto.class);
            messagePage = new MessagePage(page, list);
        } else {
            page.setTotalSize(0);
            messagePage = new MessagePage(page, new ArrayList());
        }
        rsMap.put("data", messagePage);
        return MessagePacket.newSuccess(rsMap, "getObjectCollectList success!");
    }
}
