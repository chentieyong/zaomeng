package com.kingpivot.api.controller.ApiCapitalPostController;

import com.google.common.collect.Maps;
import com.kingpivot.base.capitalPost.model.CapitalPost;
import com.kingpivot.base.capitalPost.service.CapitalPostService;
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
@Api(description = "资金投出(资源发布)管理接口")
public class ApiCapitalPostController extends ApiBaseController {

    @Resource
    private SendMessageService sendMessageService;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private CapitalPostService capitalPostService;

    @ApiOperation(value = "submitOneCapitalPost", notes = "提交一个资金投出")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "sessionID", value = "登录标识", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "name", value = "名称", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "description", value = "说明", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "beginDate", value = "开始日期", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "endDate", value = "结束日期", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "fromType", value = "资金类型", dataType = "int"),
            @ApiImplicitParam(paramType = "query", name = "stageType", value = "发展阶段", dataType = "int"),
            @ApiImplicitParam(paramType = "query", name = "amountType", value = "投资资金", dataType = "int"),
            @ApiImplicitParam(paramType = "query", name = "industrialName", value = "投资行业", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "zoneName", value = "投资地区", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "urls", value = "附件图", dataType = "String"),
    })
    @RequestMapping(value = "/submitOneCapitalPost")
    public MessagePacket submitOneCapitalPost(HttpServletRequest request) {
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
        String fromType = request.getParameter("fromType");//资金类型 1股权投资2金融投资3其他投资
        String stageType = request.getParameter("stageType");//发展阶段 1种子期 2初创期 3成长期 4稳健期
        String amountType = request.getParameter("amountType");//投资资金 1:小余50w 2:50w-100w 3:100w-500w 4:500w以上
        String industrialName = request.getParameter("industrialName");//投资行业
        String zoneName = request.getParameter("zoneName");//投资地区
        String urls = request.getParameter("urls");//附件图

        CapitalPost capitalPost = new CapitalPost();
        capitalPost.setApplicationID(member.getApplicationID());
        capitalPost.setName(name);
        capitalPost.setDescription(description);
        capitalPost.setMemberID(member.getId());
        if (StringUtils.isEmpty(beginDate)) {
            capitalPost.setBeginDate(new Timestamp(System.currentTimeMillis()));
        } else {
            capitalPost.setBeginDate(TimeTest.strToDate(beginDate));
        }
        if (StringUtils.isEmpty(endDate)) {
            capitalPost.setEndDate(TimeTest.timeAddDay(new Timestamp(System.currentTimeMillis()), 7));
        } else {
            capitalPost.setEndDate(TimeTest.strToDate(endDate));
        }
        if (StringUtils.isEmpty(fromType)) {
            capitalPost.setFromType(3);
        } else {
            capitalPost.setFromType(Integer.parseInt(fromType));
        }
        if (StringUtils.isEmpty(stageType)) {
            capitalPost.setStageType(1);
        } else {
            capitalPost.setStageType(Integer.parseInt(stageType));
        }
        if (StringUtils.isEmpty(amountType)) {
            capitalPost.setAmountType(1);
        } else {
            capitalPost.setAmountType(Integer.parseInt(amountType));
        }
        capitalPost.setIndustrialName(industrialName);
        capitalPost.setZoneName(zoneName);
        capitalPostService.save(capitalPost);

        if (StringUtils.isNotBlank(urls)) {
            //新增附件图
            sendMessageService.sendAddAttachmentMessage(JacksonHelper.toJson(new AddAttachmentDto.Builder()
                    .objectID(capitalPost.getId())
                    .objectDefineID(Config.CAPITALPOST_OBJECTDEFINEID)
                    .objectName(capitalPost.getName())
                    .fileType(1)
                    .showType(1)
                    .urls(urls)
                    .build()));
        }

        String desc = String.format("%s提交一个资金投出", member.getName());
        UserAgent userAgent = UserAgentUtil.getUserAgent(request.getHeader("user-agent"));
        MemberLogRequestBase base = MemberLogRequestBase.BALANCE()
                .sessionID(sessionID)
                .description(desc)
                .userAgent(userAgent == null ? null : userAgent.getBrowserType())
                .operateType(Memberlog.MemberOperateType.SUBMITONECAPITALPOST.getOname())
                .build();
        sendMessageService.sendMemberLogMessage(JacksonHelper.toJson(base));

        Map<String, Object> rsMap = Maps.newHashMap();
        rsMap.put("data", capitalPost.getId());

        return MessagePacket.newSuccess(rsMap, "submitOneCapitalPost success!");
    }
}
