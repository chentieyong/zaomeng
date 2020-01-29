package com.kingpivot.api.controller.ApiStoryController;

import com.google.common.collect.Maps;
import com.kingpivot.base.config.Config;
import com.kingpivot.base.config.RedisKey;
import com.kingpivot.base.config.UserAgent;
import com.kingpivot.base.member.model.Member;
import com.kingpivot.base.memberlog.model.Memberlog;
import com.kingpivot.base.story.model.Story;
import com.kingpivot.base.story.service.StoryService;
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
import java.util.Map;

@RequestMapping("/api")
@RestController
@Api(description = "故事管理接口")
public class ApiStoryController extends ApiBaseController {

    @Resource
    private SendMessageService sendMessageService;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private StoryService storyService;

    @ApiOperation(value = "提交一个故事", notes = "提交一个故事")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "sessionID", value = "登录标识", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "name", value = "名称", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "isPublish", value = "是否上架", dataType = "int"),
            @ApiImplicitParam(paramType = "query", name = "faceImage", value = "押题图", dataType = "int")})
    @RequestMapping(value = "/submitOneStory")
    public MessagePacket submitOneStory(HttpServletRequest request) {
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
        String isPublish = request.getParameter("isPublish");
        String faceImage = request.getParameter("faceImage");
        String urls = request.getParameter("urls");

        Story story = new Story();
        story.setApplicationID(member.getApplicationID());
        story.setName(name);

        if (StringUtils.isNotBlank(isPublish)) {
            story.setIsPublish(Integer.parseInt(isPublish));
        }
        if (StringUtils.isNotBlank(faceImage)) {
            story.setFaceImage(faceImage);
        }
        storyService.save(story);

        if (StringUtils.isNotBlank(urls)) {
            //新增附件图
            sendMessageService.sendAddAttachmentMessage(new AddAttachmentDto.Builder()
                    .objectID(story.getId())
                    .objectDefineID(Config.STORY_OBJECTDEFINEID)
                    .objectName(story.getName())
                    .fileType(1)
                    .showType(1)
                    .urls(urls)
                    .build().toString());
        }

        String description = String.format("%s提交一个故事", member.getName());
        UserAgent userAgent = UserAgentUtil.getUserAgent(request.getHeader("user-agent"));
        MemberLogRequestBase base = MemberLogRequestBase.BALANCE()
                .sessionID(sessionID)
                .description(description)
                .userAgent(userAgent == null ? null : userAgent.getBrowserType())
                .operateType(Memberlog.MemberOperateType.SUBMITONESTORY.getOname())
                .build();
        sendMessageService.sendMemberLogMessage(JacksonHelper.toJson(base));

        Map<String, Object> rsMap = Maps.newHashMap();
        rsMap.put("data", story.getId());

        return MessagePacket.newSuccess(rsMap, "submitOneStory success!");
    }

    @ApiOperation(value = "修改一个故事", notes = "修改一个故事")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "sessionID", value = "登录标识", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "storyID", value = "故事id", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "name", value = "名称", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "isPublish", value = "是否上架", dataType = "int"),
            @ApiImplicitParam(paramType = "query", name = "faceImage", value = "押题图", dataType = "int")})
    @RequestMapping(value = "/updateOneStory")
    public MessagePacket updateOneStory(HttpServletRequest request) {
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
        String storyID = request.getParameter("storyID");
        if (StringUtils.isEmpty(storyID)) {
            return MessagePacket.newFail(MessageHeader.Code.storyIdIsNull, "storyID不能为空");
        }
        Story story = storyService.findById(storyID);
        if (story == null) {
            return MessagePacket.newFail(MessageHeader.Code.storyIdIsError, "故事不存在");
        }
        String name = request.getParameter("name");
        if (StringUtils.isEmpty(name)) {
            return MessagePacket.newFail(MessageHeader.Code.nameIsNull, "name不能为空");
        }
        String isPublish = request.getParameter("isPublish");
        String faceImage = request.getParameter("faceImage");

        story.setName(name);
        if (StringUtils.isNotBlank(isPublish)) {
            story.setIsPublish(Integer.parseInt(isPublish));
        }
        if (StringUtils.isNotBlank(faceImage)) {
            story.setFaceImage(faceImage);
        }
        storyService.save(story);

        String description = String.format("%s修改一个故事", member.getName());
        UserAgent userAgent = UserAgentUtil.getUserAgent(request.getHeader("user-agent"));
        MemberLogRequestBase base = MemberLogRequestBase.BALANCE()
                .sessionID(sessionID)
                .description(description)
                .userAgent(userAgent == null ? null : userAgent.getBrowserType())
                .operateType(Memberlog.MemberOperateType.UPDATEONESTORY.getOname())
                .build();
        sendMessageService.sendMemberLogMessage(JacksonHelper.toJson(base));

        Map<String, Object> rsMap = Maps.newHashMap();
        rsMap.put("data", TimeTest.getTimeStr());

        return MessagePacket.newSuccess(rsMap, "updateOneStory success!");
    }

    @ApiOperation(value = "删除一个故事", notes = "删除一个故事")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "sessionID", value = "登录标识", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "storyID", value = "故事id", dataType = "String")})
    @RequestMapping(value = "/deleteOneStory")
    public MessagePacket deleteOneStory(HttpServletRequest request) {
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
        String storyID = request.getParameter("storyID");
        if (StringUtils.isEmpty(storyID)) {
            return MessagePacket.newFail(MessageHeader.Code.storyIdIsNull, "storyID不能为空");
        }
        Story story = storyService.findById(storyID);
        if (story == null) {
            return MessagePacket.newFail(MessageHeader.Code.storyIdIsError, "故事不存在");
        }
        storyService.del(story);
        String description = String.format("%s删除一个故事", member.getName());

        UserAgent userAgent = UserAgentUtil.getUserAgent(request.getHeader("user-agent"));
        MemberLogRequestBase base = MemberLogRequestBase.BALANCE()
                .sessionID(sessionID)
                .description(description)
                .userAgent(userAgent == null ? null : userAgent.getBrowserType())
                .operateType(Memberlog.MemberOperateType.DELETEONESTORY.getOname())
                .build();
        sendMessageService.sendMemberLogMessage(JacksonHelper.toJson(base));

        Map<String, Object> rsMap = Maps.newHashMap();
        rsMap.put("data", TimeTest.getTimeStr());

        return MessagePacket.newSuccess(rsMap, "deleteOneStory success!");
    }
}
