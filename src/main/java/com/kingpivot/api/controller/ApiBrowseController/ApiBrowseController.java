package com.kingpivot.api.controller.ApiBrowseController;

import com.google.common.collect.Maps;
import com.kingpivot.api.dto.browse.BrowseGoodsShopListDto;
import com.kingpivot.api.dto.browse.ObjectBrowseDto;
import com.kingpivot.api.dto.collect.CollectListDto;
import com.kingpivot.base.browse.model.Browse;
import com.kingpivot.base.browse.service.BrowseService;
import com.kingpivot.base.config.Config;
import com.kingpivot.base.config.RedisKey;
import com.kingpivot.base.config.UserAgent;
import com.kingpivot.base.goodsShop.model.GoodsShop;
import com.kingpivot.base.goodsShop.service.GoodsShopService;
import com.kingpivot.base.member.model.Member;
import com.kingpivot.base.member.service.MemberService;
import com.kingpivot.base.memberlog.model.Memberlog;
import com.kingpivot.base.objectFeatureData.model.ObjectFeatureData;
import com.kingpivot.base.objectFeatureData.service.ObjectFeatureDataService;
import com.kingpivot.base.objectFeatureItem.service.ObjectFeatureItemService;
import com.kingpivot.base.support.MemberLogDTO;
import com.kingpivot.common.jms.SendMessageService;
import com.kingpivot.common.jms.dto.memberLog.MemberLogRequestBase;
import com.kingpivot.common.util.Constants;
import com.kingpivot.common.utils.*;
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
@Api(description = "浏览管理接口")
public class ApiBrowseController {

    @Resource
    private SendMessageService sendMessageService;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private GoodsShopService goodsShopService;
    @Autowired
    private BrowseService browseService;
    @Autowired
    private ObjectFeatureItemService objectFeatureItemService;
    @Autowired
    private ObjectFeatureDataService objectFeatureDataService;

    @ApiOperation(value = "加入浏览", notes = "加入浏览")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "sessionID", value = "登录标识", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "objectID", value = "对象id", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "objectName", value = "对象名", dataType = "int"),
            @ApiImplicitParam(paramType = "query", name = "objectDefineID", value = "对象定义id", dataType = "String")})
    @RequestMapping(value = "/addBrowse")
    public MessagePacket addBrowse(HttpServletRequest request) {
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
        String objectName = request.getParameter("objectName");
        if (StringUtils.isEmpty(objectName)) {
            return MessagePacket.newFail(MessageHeader.Code.objectNameIsNull, "objectName不能为空");
        }
        String objectDefineID = request.getParameter("objectDefineID");
        if (StringUtils.isEmpty(objectDefineID)) {
            return MessagePacket.newFail(MessageHeader.Code.objectDefineIDIsNull, "objectDefineID不能为空");
        }

        Browse browse = new Browse();
        browse.setApplicationID(member.getApplicationID());
        browse.setMemberID(member.getId());
        browse.setName(String.format("%s浏览%s", member.getName(), objectName));
        browse.setObjectDefineID(objectDefineID);
        browse.setObjectID(objectID);
        browse.setObjectName(objectName);
        browseService.save(browse);

        String description = String.format("%s浏览%s", member.getName(), objectName);

        UserAgent userAgent = UserAgentUtil.getUserAgent(request.getHeader("user-agent"));
        MemberLogRequestBase base = MemberLogRequestBase.BALANCE()
                .sessionID(sessionID)
                .description(description)
                .userAgent(userAgent == null ? null : userAgent.getBrowserType())
                .operateType(Memberlog.MemberOperateType.ADDBROWSE.getOname())
                .build();

        sendMessageService.sendMemberLogMessage(JacksonHelper.toJson(base));

        Map<String, Object> rsMap = Maps.newHashMap();
        rsMap.put("data", browse.getId());

        return MessagePacket.newSuccess(rsMap, "addBrowse success!");
    }

    @ApiOperation(value = "删除浏览记录", notes = "删除浏览记录")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "sessionID", value = "登录标识", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "collectID", value = "浏览id", dataType = "String")})
    @RequestMapping(value = "/removeBrowse")
    public MessagePacket removeBrowse(HttpServletRequest request) {
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

        String browseID = request.getParameter("browseID");
        if (StringUtils.isEmpty(browseID)) {
            return MessagePacket.newFail(MessageHeader.Code.collectIDIsNull, "browseID不能为空");
        }

        Browse browse = browseService.findById(browseID);
        if (browse == null) {
            return MessagePacket.newFail(MessageHeader.Code.collectIDIsError, "browseID不正确");
        }

        browseService.del(browse);

        String description = String.format("%s删除浏览", member.getName());

        UserAgent userAgent = UserAgentUtil.getUserAgent(request.getHeader("user-agent"));
        MemberLogRequestBase base = MemberLogRequestBase.BALANCE()
                .sessionID(sessionID)
                .description(description)
                .userAgent(userAgent == null ? null : userAgent.getBrowserType())
                .operateType(Memberlog.MemberOperateType.REMOVEBROWSE.getOname())
                .build();

        sendMessageService.sendMemberLogMessage(JacksonHelper.toJson(base));

        Map<String, Object> rsMap = Maps.newHashMap();
        rsMap.put("data", TimeTest.getTimeStr());

        return MessagePacket.newSuccess(rsMap, "removeBrowse success!");
    }

    @ApiOperation(value = "获取会员浏览列表", notes = "获取会员浏览列表")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "sessionID", value = "登录标识", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "objectDefineID", value = "对象定义id", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "currentPage", value = "分页，页码从1开始", dataType = "int"),
            @ApiImplicitParam(paramType = "query", name = "pageNumber", value = "每一页大小", dataType = "int")})
    @RequestMapping(value = "/getBrowseList")
    public MessagePacket getBrowseList(HttpServletRequest request) {
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

        Page<Browse> rs = browseService.list(paramMap, pageable);
        Map<String, Object> rsMap = Maps.newHashMap();
        MessagePage messagePage = null;
        if (rs != null && rs.getSize() != 0) {
            if (StringUtils.isNotBlank(objectDefineID)) {
                switch (objectDefineID) {
                    case Config.GOODSSHOP_OBJECTDEFINEID://店铺商品
                        List<BrowseGoodsShopListDto> browseGoodsShopListDtoList = new ArrayList<>();
                        BrowseGoodsShopListDto browseGoodsShopListDto = null;
                        GoodsShop goodsShop = null;
                        for (Browse browse : rs.getContent()) {
                            if (StringUtils.isNotBlank(browse.getObjectID())) {
                                goodsShop = goodsShopService.findById(browse.getObjectID());
                                if (goodsShop != null) {
                                    browseGoodsShopListDto = new BrowseGoodsShopListDto();
                                    browseGoodsShopListDto.setBrowseID(browse.getId());
                                    browseGoodsShopListDto.setObjectID(goodsShop.getId());
                                    browseGoodsShopListDto.setObjectName(goodsShop.getName());
                                    browseGoodsShopListDto.setListImage(goodsShop.getLittleImage());
                                    browseGoodsShopListDto.setShowPrice(goodsShop.getRealPrice());
                                    browseGoodsShopListDto.setMemberPrice(goodsShop.getMemberPrice());
                                    browseGoodsShopListDto.setStockNumber(goodsShop.getStockNumber());
                                    browseGoodsShopListDto.setStockOut(goodsShop.getStockOut());
                                    browseGoodsShopListDto.setUnitDescription(goodsShop.getUnitDescription());

                                    Object itemOjb = objectFeatureItemService.getDefaultFeatureItem(goodsShop.getId());
                                    if (itemOjb != null) {
                                        Object[] obj = (Object[]) itemOjb;
                                        if (obj != null) {
                                            browseGoodsShopListDto.setUnitDescription((String) obj[0]);
                                            browseGoodsShopListDto.setObjectFeatureItemID1((String) obj[1]);
                                            ObjectFeatureData objectFetureData = objectFeatureDataService.getObjectFetureData(goodsShop.getId(), (String) obj[1]);
                                            if (objectFetureData != null) {
                                                browseGoodsShopListDto.setShowPrice(objectFetureData.getRealPrice());
                                                browseGoodsShopListDto.setMemberPrice(objectFetureData.getMemberPrice());
                                            }
                                        }
                                    }
                                    browseGoodsShopListDtoList.add(browseGoodsShopListDto);
                                }
                            }
                        }
                        page.setTotalSize((int) rs.getTotalElements());
                        messagePage = new MessagePage(page, browseGoodsShopListDtoList);
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

        String description = String.format("%s获取会员浏览列表", member.getName());

        UserAgent userAgent = UserAgentUtil.getUserAgent(request.getHeader("user-agent"));
        MemberLogRequestBase base = MemberLogRequestBase.BALANCE()
                .sessionID(sessionID)
                .description(description)
                .userAgent(userAgent == null ? null : userAgent.getBrowserType())
                .operateType(Memberlog.MemberOperateType.GETBROWSELIST.getOname())
                .build();

        sendMessageService.sendMemberLogMessage(JacksonHelper.toJson(base));

        return MessagePacket.newSuccess(rsMap, "getBrowseList success!");
    }

    @ApiOperation(value = "获取对象浏览列表", notes = "获取对象浏览列表")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "sessionID", value = "登录标识", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "objectID", value = "对象id", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "currentPage", value = "分页，页码从1开始", dataType = "int"),
            @ApiImplicitParam(paramType = "query", name = "pageNumber", value = "每一页大小", dataType = "int")})
    @RequestMapping(value = "/getObjectBrowseList")
    public MessagePacket getObjectBrowseList(HttpServletRequest request) {
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

        Page<Browse> rs = browseService.list(paramMap, pageable);
        Map<String, Object> rsMap = Maps.newHashMap();
        MessagePage messagePage = null;
        if (rs != null && rs.getSize() != 0) {
            page.setTotalSize((int) rs.getTotalElements());
            List<ObjectBrowseDto> list = BeanMapper.mapList(rs.getContent(), ObjectBrowseDto.class);
            messagePage = new MessagePage(page, list);
        } else {
            page.setTotalSize(0);
            messagePage = new MessagePage(page, new ArrayList());
        }
        rsMap.put("data", messagePage);
        return MessagePacket.newSuccess(rsMap, "getObjectBrowseList success!");
    }
}
