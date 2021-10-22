package com.kingpivot.api.controller.ApiDiscussController;

import com.google.common.collect.Maps;
import com.kingpivot.api.dto.collect.ObjectCollectDto;
import com.kingpivot.api.dto.discuss.ObjectDiscussListDto;
import com.kingpivot.base.collect.model.Collect;
import com.kingpivot.base.config.RedisKey;
import com.kingpivot.base.config.UserAgent;
import com.kingpivot.base.discuss.model.Discuss;
import com.kingpivot.base.discuss.service.DiscussService;
import com.kingpivot.base.member.model.Member;
import com.kingpivot.base.memberlog.model.Memberlog;
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
@Api(description = "评论管理接口")
public class ApiDiscussController {

    @Resource
    private SendMessageService sendMessageService;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private DiscussService discussService;

    @ApiOperation(value = "提交一个评论", notes = "提交一个评论")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "sessionID", value = "登录标识", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "description", value = "评论", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "objectID", value = "对象id", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "objectName", value = "对象名", dataType = "int"),
            @ApiImplicitParam(paramType = "query", name = "objectDefineID", value = "对象定义id", dataType = "String")})
    @RequestMapping(value = "/submitOneDiscuss")
    public MessagePacket submitOneDiscuss(HttpServletRequest request) {
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
        String content = request.getParameter("content");
        if(StringUtils.isEmpty(content)){
            return MessagePacket.newFail(MessageHeader.Code.contentIsNull, "评论内容不能为空");
        }

        Discuss discuss = new Discuss();
        discuss.setDescription(content);
        discuss.setApplicationID(member.getApplicationID());
        discuss.setMemberID(member.getId());
        discuss.setName(String.format("%s评论%s", member.getName(), objectName));
        discuss.setObjectDefineID(objectDefineID);
        discuss.setObjectID(objectID);
        discuss.setObjectName(objectName);
        discussService.save(discuss);

        String description = String.format("%s评论%s", member.getName(), objectName);
        UserAgent userAgent = UserAgentUtil.getUserAgent(request.getHeader("user-agent"));
        MemberLogRequestBase base = MemberLogRequestBase.BALANCE()
                .sessionID(sessionID)
                .description(description)
                .userAgent(userAgent == null ? null : userAgent.getBrowserType())
                .operateType(Memberlog.MemberOperateType.SUBMITONEDISCUSS.getOname())
                .build();
        sendMessageService.sendMemberLogMessage(JacksonHelper.toJson(base));

        Map<String, Object> rsMap = Maps.newHashMap();
        rsMap.put("data", discuss.getId());

        return MessagePacket.newSuccess(rsMap, "submitOneDiscuss success!");
    }

    @ApiOperation(value = "获取对象评论列表", notes = "获取对象评论列表")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "sessionID", value = "登录标识", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "objectID", value = "对象id", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "currentPage", value = "分页，页码从1开始", dataType = "int"),
            @ApiImplicitParam(paramType = "query", name = "pageNumber", value = "每一页大小", dataType = "int")})
    @RequestMapping(value = "/getObjectDiscussList")
    public MessagePacket getObjectDiscussList(HttpServletRequest request) {
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

        Page<Discuss> rs = discussService.list(paramMap, pageable);
        Map<String, Object> rsMap = Maps.newHashMap();
        MessagePage messagePage = null;
        if (rs != null && rs.getSize() != 0) {
            page.setTotalSize((int) rs.getTotalElements());
            List<ObjectDiscussListDto> list = BeanMapper.mapList(rs.getContent(), ObjectDiscussListDto.class);
            messagePage = new MessagePage(page, list);
        } else {
            page.setTotalSize(0);
            messagePage = new MessagePage(page, new ArrayList());
        }
        rsMap.put("data", messagePage);
        return MessagePacket.newSuccess(rsMap, "getObjectDiscussList success!");
    }
}
