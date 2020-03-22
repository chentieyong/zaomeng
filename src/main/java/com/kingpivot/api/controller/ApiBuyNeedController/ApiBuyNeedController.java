package com.kingpivot.api.controller.ApiBuyNeedController;

import com.google.common.collect.Maps;
import com.kingpivot.api.dto.buyNeed.BuyNeedDetailDto;
import com.kingpivot.api.dto.buyNeed.BuyNeedListDto;
import com.kingpivot.base.buyNeed.model.BuyNeed;
import com.kingpivot.base.buyNeed.service.BuyNeedService;
import com.kingpivot.base.config.Config;
import com.kingpivot.base.config.RedisKey;
import com.kingpivot.base.config.UserAgent;
import com.kingpivot.base.member.model.Member;
import com.kingpivot.base.memberlog.model.Memberlog;
import com.kingpivot.base.pointapplication.service.PointApplicationService;
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
@Api(description = "产品求购(找产品)管理接口")
public class ApiBuyNeedController extends ApiBaseController {

    @Resource
    private SendMessageService sendMessageService;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private BuyNeedService buyNeedService;
    @Autowired
    private KingBase kingBase;
    @Autowired
    private PointApplicationService pointApplicationService;

    @ApiOperation(value = "submitOneBuyNeed", notes = "提交一个产品求购")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "sessionID", value = "登录标识", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "name", value = "名称", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "description", value = "说明", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "beginDate", value = "开始日期", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "endDate", value = "结束日期", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "beginAmount", value = "开始价格", dataType = "double"),
            @ApiImplicitParam(paramType = "query", name = "endAmount", value = "结束价格", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "fromWhere", value = "产地", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "priceUnit", value = "价格单位", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "qty", value = "数量", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "urls", value = "附件图", dataType = "String"),
    })
    @RequestMapping(value = "/submitOneBuyNeed")
    public MessagePacket submitOneProduct(HttpServletRequest request) {
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
        if(!kingBase.pointLess(member, pointApplicationService.getNumberByAppIdAndPointName(
                member.getApplicationID(), Config.CAPITALNEED_POINT_USENAME))){
            return MessagePacket.newFail(MessageHeader.Code.pointNumberLess, "积分个数不足");
        }

        String name = request.getParameter("name");
        if (StringUtils.isEmpty(name)) {
            return MessagePacket.newFail(MessageHeader.Code.nameIsNull, "name不能为空");
        }
        String description = request.getParameter("description");//说明
        String beginDate = request.getParameter("beginDate");//开始日期
        String endDate = request.getParameter("endDate");//结束日期
        String days = request.getParameter("days");//发布在线天数
        String beginAmount = request.getParameter("beginAmount");
        String endAmount = request.getParameter("endAmount");
        String fromWhere = request.getParameter("fromWhere");
        String priceUnit = request.getParameter("priceUnit");//价格单位
        String qty = request.getParameter("qty");//数量
        String urls = request.getParameter("urls");//附件图

        BuyNeed buyNeed = new BuyNeed();
        buyNeed.setApplicationID(member.getApplicationID());
        buyNeed.setName(name);
        buyNeed.setDescription(description);
        buyNeed.setMemberID(member.getId());
        if (StringUtils.isEmpty(beginDate)) {
            buyNeed.setBeginDate(new Timestamp(System.currentTimeMillis()));
        } else {
            buyNeed.setBeginDate(TimeTest.strToDate(beginDate));
        }
        if (StringUtils.isNotBlank(endDate)) {
            buyNeed.setEndDate(TimeTest.strToDate(endDate));
        } else if (StringUtils.isNotBlank(days)) {
            buyNeed.setEndDate(TimeTest.timeAddDay(new Timestamp(System.currentTimeMillis()), Integer.parseInt(days)));
        } else {
            buyNeed.setEndDate(TimeTest.timeAddDay(new Timestamp(System.currentTimeMillis()), 7));
        }
        if (StringUtils.isNotBlank(beginAmount)) {
            buyNeed.setBeginAmount(Integer.parseInt(beginAmount));
        }
        if (StringUtils.isNotBlank(endAmount)) {
            buyNeed.setEndAmount(Integer.parseInt(endAmount));
        }
        if (StringUtils.isNotBlank(fromWhere)) {
            buyNeed.setFromWhere(fromWhere);
        }
        if (StringUtils.isNotBlank(priceUnit)) {
            buyNeed.setPriceUnit(priceUnit);
        }
        if (StringUtils.isNotBlank(qty)) {
            buyNeed.setQty(Integer.parseInt(qty));
        }
        buyNeedService.save(buyNeed);

        if (StringUtils.isNotBlank(urls)) {
            //新增附件图
            sendMessageService.sendAddAttachmentMessage(JacksonHelper.toJson(new AddAttachmentDto.Builder()
                    .objectID(buyNeed.getId())
                    .objectDefineID(Config.BUYNEED_OBJECTDEFIPOST)
                    .objectName(buyNeed.getName())
                    .fileType(1)
                    .showType(1)
                    .urls(urls)
                    .build()));
        }

        //发送消耗积分
        sendMessageService.sendUsePointMessage(JacksonHelper.toJson(new UsePointRequest.Builder()
                .objectDefineID(Config.BUYNEED_OBJECTDEFIPOST)
                .memberID(member.getId())
                .pointName(Config.BUYNEED_POINT_USENAME)
                .build()));

        String desc = String.format("%s提交一个产品求购", member.getName());
        UserAgent userAgent = UserAgentUtil.getUserAgent(request.getHeader("user-agent"));
        MemberLogRequestBase base = MemberLogRequestBase.BALANCE()
                .sessionID(sessionID)
                .description(desc)
                .userAgent(userAgent == null ? null : userAgent.getBrowserType())
                .operateType(Memberlog.MemberOperateType.SUBMITONEPBUYNEED.getOname())
                .build();
        sendMessageService.sendMemberLogMessage(JacksonHelper.toJson(base));

        Map<String, Object> rsMap = Maps.newHashMap();
        rsMap.put("data", buyNeed.getId());

        return MessagePacket.newSuccess(rsMap, "submitOneBuyNeed success!");
    }

    @ApiOperation(value = "getBuyNeedList", notes = "获取产品求购列表")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "applicationID", value = "应用id", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "currentPage", value = "分页，页码从1开始", dataType = "int"),
            @ApiImplicitParam(paramType = "query", name = "pageNumber", value = "每一页大小", dataType = "int")})
    @RequestMapping(value = "/getBuyNeedList")
    public MessagePacket getBuyNeedList(HttpServletRequest request) {
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

        Page<BuyNeed> rs = buyNeedService.list(paramMap, pageable);
        List<BuyNeedListDto> list = null;
        if (rs != null && rs.getSize() != 0) {
            list = BeanMapper.mapList(rs.getContent(), BuyNeedListDto.class);
            page.setTotalSize((int) rs.getTotalElements());
        }
        Map<String, Object> rsMap = Maps.newHashMap();
        MessagePage messagePage = new MessagePage(page, list);
        rsMap.put("data", messagePage);
        return MessagePacket.newSuccess(rsMap, "getBuyNeedList success!");
    }

    @ApiOperation(value = "getBuyNeedDetail", notes = "获取产品求购详情")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "buyNeedID", value = "产品求购id", dataType = "String")})
    @RequestMapping(value = "/getBuyNeedDetail")
    public MessagePacket getBuyNeedDetail(HttpServletRequest request) {
        String buyNeedID = request.getParameter("buyNeedID");
        if (StringUtils.isEmpty(buyNeedID)) {
            return MessagePacket.newFail(MessageHeader.Code.buyNeedIDIsNull, "buyNeedID不能为空");
        }
        BuyNeed buyNeed = buyNeedService.findById(buyNeedID);
        if (buyNeed == null) {
            return MessagePacket.newFail(MessageHeader.Code.buyNeedIDIsError, "buyNeedID不正确");
        }
        BuyNeedDetailDto data = BeanMapper.map(buyNeed, BuyNeedDetailDto.class);
        Map<String, Object> rsMap = Maps.newHashMap();
        rsMap.put("data", data);
        return MessagePacket.newSuccess(rsMap, "getBuyNeedDetail success!");
    }
}
