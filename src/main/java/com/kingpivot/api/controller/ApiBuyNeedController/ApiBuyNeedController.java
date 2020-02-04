package com.kingpivot.api.controller.ApiBuyNeedController;

import com.google.common.collect.Maps;
import com.kingpivot.base.buyNeed.model.BuyNeed;
import com.kingpivot.base.buyNeed.service.BuyNeedService;
import com.kingpivot.base.config.Config;
import com.kingpivot.base.config.RedisKey;
import com.kingpivot.base.config.UserAgent;
import com.kingpivot.base.member.model.Member;
import com.kingpivot.base.memberlog.model.Memberlog;
import com.kingpivot.base.support.MemberLogDTO;
import com.kingpivot.common.jms.SendMessageService;
import com.kingpivot.common.jms.dto.attachment.AddAttachmentDto;
import com.kingpivot.common.jms.dto.memberLog.MemberLogRequestBase;
import com.kingpivot.common.utils.JacksonHelper;
import com.kingpivot.common.utils.TimeTest;
import com.kingpivot.common.utils.UserAgentUtil;
import com.kingpivot.protocol.ApiBaseController;
import com.kingpivot.protocol.MessageHeader;
import com.kingpivot.protocol.MessagePacket;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.sql.Timestamp;
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

        String name = request.getParameter("name");
        if (StringUtils.isEmpty(name)) {
            return MessagePacket.newFail(MessageHeader.Code.nameIsNull, "name不能为空");
        }
        String description = request.getParameter("description");//说明
        String beginDate = request.getParameter("beginDate");//开始日期
        String endDate = request.getParameter("endDate");//结束日期
        String beginAmount = request.getParameter("beginAmount");
        String endAmount = request.getParameter("endAmount");
        String fromWhere = request.getParameter("fromWhere");
        String priceUnit = request.getParameter("endDate");//价格单位
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
        if (StringUtils.isEmpty(endDate)) {
            buyNeed.setEndDate(TimeTest.timeAddDay(new Timestamp(System.currentTimeMillis()), 7));
        } else {
            buyNeed.setEndDate(TimeTest.strToDate(endDate));
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
}
