package com.kingpivot.api.controller.ApiGoodsChangeController;

import com.google.common.collect.Maps;
import com.kingpivot.api.dto.goodsChange.GoodsChangeDetailDto;
import com.kingpivot.api.dto.goodsChange.GoodsChangeListDto;
import com.kingpivot.base.config.Config;
import com.kingpivot.base.config.RedisKey;
import com.kingpivot.base.config.UserAgent;
import com.kingpivot.base.goodsChange.service.GoodsChangeService;
import com.kingpivot.base.member.model.Member;
import com.kingpivot.base.memberlog.model.Memberlog;
import com.kingpivot.base.pointapplication.service.PointApplicationService;
import com.kingpivot.base.goodsChange.model.GoodsChange;
import com.kingpivot.base.support.MemberLogDTO;
import com.kingpivot.common.KingBase;
import com.kingpivot.common.jms.SendMessageService;
import com.kingpivot.common.jms.dto.attachment.AddAttachmentDto;
import com.kingpivot.common.jms.dto.memberLog.MemberLogRequestBase;
import com.kingpivot.common.jms.dto.point.UsePointRequest;
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
@Api(description = "商品交换管理接口")
public class ApiGoodsChangeController extends ApiBaseController {
    @Resource
    private SendMessageService sendMessageService;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private GoodsChangeService goodsChangeService;
    @Autowired
    private KingBase kingBase;
    @Autowired
    private PointApplicationService pointApplicationService;

    @ApiOperation(value = "submitOneGoodsChange", notes = "提交一个产品互换")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "sessionID", value = "登录标识", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "name", value = "名称", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "description", value = "说明", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "beginDate", value = "开始日期", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "endDate", value = "结束日期", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "amount", value = "产品互换价格", dataType = "double"),
            @ApiImplicitParam(paramType = "query", name = "priceUnit", value = "价格单位", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "deliveryFeeType", value = "邮费类型", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "faceImage", value = "押题图", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "listImage", value = "列表图", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "address", value = "地址", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "urls", value = "附件图", dataType = "String"),
    })
    @RequestMapping(value = "/submitOneGoodsChange")
    public MessagePacket submitOneGoodsChange(HttpServletRequest request) {
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

        //积分是否足够
        if (!kingBase.pointLess(member, pointApplicationService.getNumberByAppIdAndPointName(
                member.getApplicationID(), Config.GOODSCHANGE_POINT_USENAME))) {
            return MessagePacket.newFail(MessageHeader.Code.pointNumberLess, "积分个数不足");
        }

        String name = request.getParameter("name");
        if (StringUtils.isEmpty(name)) {
            return MessagePacket.newFail(MessageHeader.Code.nameIsNull, "name不能为空");
        }
        String shortName = request.getParameter("shortName");//简称
        String description = request.getParameter("description");//说明
        String beginDate = request.getParameter("beginDate");//开始日期
        String endDate = request.getParameter("endDate");//结束日期
        String days = request.getParameter("days");//发布在线天数
        String amount = request.getParameter("amount");//价格
        String priceUnit = request.getParameter("priceUnit");//价格规格
        String faceImage = request.getParameter("faceImage");//押题图
        String listImage = request.getParameter("listImage");//列表图

        GoodsChange goodsChange = new GoodsChange();
        goodsChange.setApplicationID(member.getApplicationID());
        goodsChange.setName(name);
        goodsChange.setShortName(shortName);
        goodsChange.setDescription(description);
        goodsChange.setMemberID(member.getId());
        if (StringUtils.isEmpty(beginDate)) {
            goodsChange.setBeginDate(new Timestamp(System.currentTimeMillis()));
        } else {
            goodsChange.setBeginDate(TimeTest.strToDate(beginDate));
        }
        if (StringUtils.isNotBlank(endDate)) {
            goodsChange.setEndDate(TimeTest.strToDate(endDate));
        } else if (StringUtils.isNotBlank(days)) {
            goodsChange.setEndDate(TimeTest.timeAddDay(new Timestamp(System.currentTimeMillis()), Integer.parseInt(days)));
        } else {
            goodsChange.setEndDate(TimeTest.timeAddDay(new Timestamp(System.currentTimeMillis()), 7));
        }
        if (StringUtils.isNotBlank(amount)) {
            goodsChange.setAmount(Double.parseDouble(amount));
        }
        if (StringUtils.isNotBlank(faceImage)) {
            goodsChange.setFaceImage(faceImage);
        }
        if (StringUtils.isNotBlank(listImage)) {
            goodsChange.setListImage(listImage);
        }
        if(StringUtils.isNotBlank(priceUnit)){
            goodsChange.setPriceUnit(priceUnit);
        }
        goodsChangeService.save(goodsChange);

        //发送消耗积分
        sendMessageService.sendUsePointMessage(JacksonHelper.toJson(new UsePointRequest.Builder()
                .objectDefineID(Config.GOODSCHANGE_OBJECTDEFIPOST)
                .memberID(member.getId())
                .pointName(Config.GOODSCHANGE_POINT_USENAME)
                .build()));

        String desc = String.format("%s提交一个产品互换", member.getName());
        UserAgent userAgent = UserAgentUtil.getUserAgent(request.getHeader("user-agent"));
        MemberLogRequestBase base = MemberLogRequestBase.BALANCE()
                .sessionID(sessionID)
                .description(desc)
                .userAgent(userAgent == null ? null : userAgent.getBrowserType())
                .operateType(Memberlog.MemberOperateType.SUBMITONEGOODSCHANGE.getOname())
                .build();
        sendMessageService.sendMemberLogMessage(JacksonHelper.toJson(base));

        Map<String, Object> rsMap = Maps.newHashMap();
        rsMap.put("data", goodsChange.getId());

        return MessagePacket.newSuccess(rsMap, "submitOneGoodsChange success!");
    }

    @ApiOperation(value = "updateOneGoodsChange", notes = "修改一个产品互换")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "sessionID", value = "登录标识", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "goodsChangeID", value = "商品置换id", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "name", value = "名称", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "description", value = "说明", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "beginDate", value = "开始日期", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "endDate", value = "结束日期", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "amount", value = "产品互换价格", dataType = "double"),
            @ApiImplicitParam(paramType = "query", name = "priceUnit", value = "价格单位", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "deliveryFeeType", value = "邮费类型", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "faceImage", value = "押题图", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "listImage", value = "列表图", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "address", value = "地址", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "urls", value = "附件图", dataType = "String"),
    })
    @RequestMapping(value = "/updateOneGoodsChange")
    public MessagePacket updateOneGoodsChange(HttpServletRequest request) {
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

        String goodsChangeID = request.getParameter("goodsChangeID");
        if (StringUtils.isEmpty(goodsChangeID)) {
            return MessagePacket.newFail(MessageHeader.Code.goodsChangeIDIsNull, "goodsChangeID不能为空");
        }
        GoodsChange goodsChange = goodsChangeService.findById(goodsChangeID);
        if (goodsChange == null) {
            return MessagePacket.newFail(MessageHeader.Code.goodsChangeIDIsError, "goodsChangeID不正确");
        }

        String name = request.getParameter("name");
        if (StringUtils.isEmpty(name)) {
            return MessagePacket.newFail(MessageHeader.Code.nameIsNull, "name不能为空");
        }
        String shortName = request.getParameter("shortName");//简称
        String description = request.getParameter("description");//说明
        String amount = request.getParameter("amount");//价格
        String priceUnit = request.getParameter("priceUnit");//价格规格
        String faceImage = request.getParameter("faceImage");//押题图
        String listImage = request.getParameter("listImage");//列表图

        if(StringUtils.isNotBlank(name)){
            goodsChange.setName(name);
        }
        if(StringUtils.isNotBlank(shortName)){
            goodsChange.setShortName(shortName);
        }
        if(StringUtils.isNotBlank(description)){
            goodsChange.setDescription(description);
        }
        if (StringUtils.isNotBlank(amount)) {
            goodsChange.setAmount(Double.parseDouble(amount));
        }
        if (StringUtils.isNotBlank(faceImage)) {
            goodsChange.setFaceImage(faceImage);
        }
        if (StringUtils.isNotBlank(listImage)) {
            goodsChange.setListImage(listImage);
        }
        if(StringUtils.isNotBlank(priceUnit)){
            goodsChange.setPriceUnit(priceUnit);
        }
        goodsChangeService.save(goodsChange);

        String desc = String.format("%s修改一个产品互换", member.getName());
        UserAgent userAgent = UserAgentUtil.getUserAgent(request.getHeader("user-agent"));
        MemberLogRequestBase base = MemberLogRequestBase.BALANCE()
                .sessionID(sessionID)
                .description(desc)
                .userAgent(userAgent == null ? null : userAgent.getBrowserType())
                .operateType(Memberlog.MemberOperateType.UPDATEONEGOODSCHANGE.getOname())
                .build();
        sendMessageService.sendMemberLogMessage(JacksonHelper.toJson(base));
        Map<String, Object> rsMap = Maps.newHashMap();
        rsMap.put("data", goodsChange.getId());
        return MessagePacket.newSuccess(rsMap, "updateOneGoodsChange success!");
    }

    @ApiOperation(value = "getGoodsChangeList", notes = "获取产品互换列表")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "applicationID", value = "应用id", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "currentPage", value = "分页，页码从1开始", dataType = "int"),
            @ApiImplicitParam(paramType = "query", name = "pageNumber", value = "每一页大小", dataType = "int")})
    @RequestMapping(value = "/getGoodsChangeList")
    public MessagePacket getGoodsChangeList(HttpServletRequest request) {
        String applicationID = request.getParameter("applicationID");
        if (StringUtils.isEmpty(applicationID)) {
            return MessagePacket.newFail(MessageHeader.Code.applicationIdIsNull, "applicationID不能为空");
        }
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("applicationID", applicationID);
        paramMap.put("endDate:gte", new Timestamp(System.currentTimeMillis()));
        paramMap.put("isValid", Constants.ISVALID_YES);
        paramMap.put("isLock", Constants.ISLOCK_NO);

        List<Sort.Order> orders = new ArrayList<Sort.Order>();
        orders.add(new Sort.Order(Sort.Direction.DESC, "createdTime"));

        Object currentPage = request.getParameter("currentPage");
        Object pageNumber = request.getParameter("pageNumber");

        TPage page = ApiPageUtil.makePage(currentPage, pageNumber);

        Pageable pageable = new PageRequest(page.getOffset(), page.getPageSize(), new Sort(orders));

        Page<GoodsChange> rs = goodsChangeService.list(paramMap, pageable);
        List<GoodsChangeListDto> list = null;
        if (rs != null && rs.getSize() != 0) {
            list = BeanMapper.mapList(rs.getContent(), GoodsChangeListDto.class);
            page.setTotalSize((int) rs.getTotalElements());
        }
        Map<String, Object> rsMap = Maps.newHashMap();
        MessagePage messagePage = new MessagePage(page, list);
        rsMap.put("data", messagePage);
        return MessagePacket.newSuccess(rsMap, "getGoodsChangeList success!");
    }

    @ApiOperation(value = "getGoodsChangeDetail", notes = "获取产品互换详情")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "goodsChangeID", value = "产品互换id", dataType = "String")})
    @RequestMapping(value = "/getGoodsChangeDetail")
    public MessagePacket getGoodsChangeDetail(HttpServletRequest request) {
        String goodsChangeID = request.getParameter("goodsChangeID");
        if (StringUtils.isEmpty(goodsChangeID)) {
            return MessagePacket.newFail(MessageHeader.Code.goodsChangeIDIsNull, "goodsChangeID不能为空");
        }
        GoodsChange goodsChange = goodsChangeService.findById(goodsChangeID);
        if (goodsChange == null) {
            return MessagePacket.newFail(MessageHeader.Code.goodsChangeIDIsError, "goodsChangeID不正确");
        }
        GoodsChangeDetailDto data = BeanMapper.map(goodsChange, GoodsChangeDetailDto.class);
        Map<String, Object> rsMap = Maps.newHashMap();
        rsMap.put("data", data);
        return MessagePacket.newSuccess(rsMap, "getGoodsChangeDetail success!");
    }
}
