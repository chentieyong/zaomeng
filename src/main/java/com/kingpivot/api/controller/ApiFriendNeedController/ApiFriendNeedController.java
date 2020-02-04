package com.kingpivot.api.controller.ApiFriendNeedController;

import com.google.common.collect.Maps;
import com.kingpivot.base.config.Config;
import com.kingpivot.base.config.RedisKey;
import com.kingpivot.base.config.UserAgent;
import com.kingpivot.base.friendNeed.model.FriendNeed;
import com.kingpivot.base.friendNeed.service.FriendNeedService;
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
@Api(description = "交友需求管理接口")
public class ApiFriendNeedController extends ApiBaseController {

    @Resource
    private SendMessageService sendMessageService;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private FriendNeedService friendNeedService;

    @ApiOperation(value = "submitOneFriendNeed", notes = "提交一个交友需求")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "sessionID", value = "登录标识", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "name", value = "名称", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "description", value = "说明", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "beginDate", value = "开始日期", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "endDate", value = "结束日期", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "age", value = "年龄", dataType = "int"),
            @ApiImplicitParam(paramType = "query", name = "titleID", value = "性别", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "salaryCategoryID", value = "收入范围", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "phone", value = "电话", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "email", value = "邮箱", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "address", value = "地址", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "urls", value = "附件图", dataType = "String"),
    })
    @RequestMapping(value = "/submitOneFriendNeed")
    public MessagePacket submitOneFriendNeed(HttpServletRequest request) {
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
        String age = request.getParameter("age");
        String titleID = request.getParameter("titleID");//性别
        String salaryCategoryID = request.getParameter("salaryCategoryID");//收入范围
        String phone = request.getParameter("phone");//电话
        String email = request.getParameter("email");//邮箱
        String address = request.getParameter("address");//地址
        String urls = request.getParameter("urls");//附件图

        FriendNeed friendNeed = new FriendNeed();
        friendNeed.setApplicationID(member.getApplicationID());
        friendNeed.setName(name);
        friendNeed.setDescription(description);
        friendNeed.setMemberID(member.getId());
        if (StringUtils.isEmpty(beginDate)) {
            friendNeed.setBeginDate(new Timestamp(System.currentTimeMillis()));
        } else {
            friendNeed.setBeginDate(TimeTest.strToDate(beginDate));
        }
        if (StringUtils.isEmpty(endDate)) {
            friendNeed.setEndDate(TimeTest.timeAddDay(new Timestamp(System.currentTimeMillis()), 7));
        } else {
            friendNeed.setEndDate(TimeTest.strToDate(endDate));
        }
        if (StringUtils.isNotBlank(age)) {
            friendNeed.setAge(Integer.parseInt(age));
        }
        if (StringUtils.isNotBlank(titleID)) {
            friendNeed.setTitleID(titleID);
        }
        if (StringUtils.isNotBlank(email)) {
            friendNeed.setEmail(email);
        }
        if (StringUtils.isNotBlank(salaryCategoryID)) {
            friendNeed.setSalaryCategoryID(salaryCategoryID);
        }
        if (StringUtils.isNotBlank(phone)) {
            friendNeed.setPhone(phone);
        }
        if (StringUtils.isNotBlank(address)) {
            friendNeed.setAddress(address);
        }
        friendNeedService.save(friendNeed);

        if (StringUtils.isNotBlank(urls)) {
            //新增附件图
            sendMessageService.sendAddAttachmentMessage(JacksonHelper.toJson(new AddAttachmentDto.Builder()
                    .objectID(friendNeed.getId())
                    .objectDefineID(Config.FRIENDNEED_OBJECTDEFIPOST)
                    .objectName(friendNeed.getName())
                    .fileType(1)
                    .showType(1)
                    .urls(urls)
                    .build()));
        }

        String desc = String.format("%s提交一个交友需求", member.getName());
        UserAgent userAgent = UserAgentUtil.getUserAgent(request.getHeader("user-agent"));
        MemberLogRequestBase base = MemberLogRequestBase.BALANCE()
                .sessionID(sessionID)
                .description(desc)
                .userAgent(userAgent == null ? null : userAgent.getBrowserType())
                .operateType(Memberlog.MemberOperateType.SUBMITONEFRIENDNEED.getOname())
                .build();
        sendMessageService.sendMemberLogMessage(JacksonHelper.toJson(base));

        Map<String, Object> rsMap = Maps.newHashMap();
        rsMap.put("data", friendNeed.getId());

        return MessagePacket.newSuccess(rsMap, "submitOneFriendNeed success!");
    }
}
