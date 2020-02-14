package com.kingpivot.api.controller.ApiPraiseController;

import com.google.common.collect.Maps;
import com.kingpivot.api.dto.praise.ObjectPraiseDto;
import com.kingpivot.api.dto.praise.PraiseDto;
import com.kingpivot.api.dto.praise.PraiseMemberDto;
import com.kingpivot.base.config.Config;
import com.kingpivot.base.config.RedisKey;
import com.kingpivot.base.config.UserAgent;
import com.kingpivot.base.member.model.Member;
import com.kingpivot.base.member.service.MemberService;
import com.kingpivot.base.memberlog.model.Memberlog;
import com.kingpivot.base.praise.model.Praise;
import com.kingpivot.base.praise.service.PraiseService;
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
@Api(description = "赞管理接口")
public class ApiPraiseController {

    @Resource
    private SendMessageService sendMessageService;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private PraiseService praiseService;
    @Autowired
    private MemberService memberService;

    @ApiOperation(value = "加入赞", notes = "加入赞")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "sessionID", value = "登录标识", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "objectID", value = "对象id", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "objectName", value = "对象名", dataType = "int"),
            @ApiImplicitParam(paramType = "query", name = "objectDefineID", value = "对象定义id", dataType = "String")})
    @RequestMapping(value = "/addPraise")
    public MessagePacket addPraise(HttpServletRequest request) {
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

        String praiseID = praiseService.getPraiseByObjectIDAndMemberID(objectID, member.getId());

        if (StringUtils.isNotBlank(praiseID)) {
            return MessagePacket.newFail(MessageHeader.Code.memberIsCollect, "请勿重复赞");
        }
        String objectName = request.getParameter("objectName");
        if (StringUtils.isEmpty(objectName)) {
            return MessagePacket.newFail(MessageHeader.Code.objectNameIsNull, "objectName不能为空");
        }
        String objectDefineID = request.getParameter("objectDefineID");
        if (StringUtils.isEmpty(objectDefineID)) {
            return MessagePacket.newFail(MessageHeader.Code.objectDefineIDIsNull, "objectDefineID不能为空");
        }

        Praise praise = new Praise();
        praise.setApplicationID(member.getApplicationID());
        praise.setMemberID(member.getId());
        praise.setName(String.format("%s赞%s", member.getName(), objectName));
        praise.setObjectDefineID(objectDefineID);
        praise.setObjectID(objectID);
        praise.setObjectName(objectName);
        praiseService.save(praise);

        String description = String.format("%s赞%s", member.getName(), objectName);

        UserAgent userAgent = UserAgentUtil.getUserAgent(request.getHeader("user-agent"));
        MemberLogRequestBase base = MemberLogRequestBase.BALANCE()
                .sessionID(sessionID)
                .description(description)
                .userAgent(userAgent == null ? null : userAgent.getBrowserType())
                .operateType(Memberlog.MemberOperateType.ADDPRAISE.getOname())
                .build();

        sendMessageService.sendMemberLogMessage(JacksonHelper.toJson(base));

        Map<String, Object> rsMap = Maps.newHashMap();
        rsMap.put("data", praise.getId());

        return MessagePacket.newSuccess(rsMap, "addPraise success!");
    }

    @ApiOperation(value = "删除赞记录", notes = "删除赞记录")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "sessionID", value = "登录标识", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "praiseID", value = "收藏id", dataType = "String")})
    @RequestMapping(value = "/removePraise")
    public MessagePacket removePraise(HttpServletRequest request) {
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

        String praiseID = request.getParameter("praiseID");
        if (StringUtils.isEmpty(praiseID)) {
            return MessagePacket.newFail(MessageHeader.Code.praiseIDIsNull, "praiseID不能为空");
        }

        Praise praise = praiseService.findById(praiseID);
        if (praise == null) {
            return MessagePacket.newFail(MessageHeader.Code.praiseIDIsError, "praiseID不正确");
        }

        praiseService.del(praise);

        String description = String.format("%s删除赞", member.getName());

        UserAgent userAgent = UserAgentUtil.getUserAgent(request.getHeader("user-agent"));
        MemberLogRequestBase base = MemberLogRequestBase.BALANCE()
                .sessionID(sessionID)
                .description(description)
                .userAgent(userAgent == null ? null : userAgent.getBrowserType())
                .operateType(Memberlog.MemberOperateType.REMOVEPRAISE.getOname())
                .build();
        sendMessageService.sendMemberLogMessage(JacksonHelper.toJson(base));

        Map<String, Object> rsMap = Maps.newHashMap();
        rsMap.put("data", TimeTest.getTimeStr());

        return MessagePacket.newSuccess(rsMap, "removePraise success!");
    }

    @ApiOperation(value = "获取会员赞列表", notes = "获取会员赞列表")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "sessionID", value = "登录标识", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "objectDefineID", value = "对象定义id", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "currentPage", value = "分页，页码从1开始", dataType = "int"),
            @ApiImplicitParam(paramType = "query", name = "pageNumber", value = "每一页大小", dataType = "int")})
    @RequestMapping(value = "/getPraiseList")
    public MessagePacket getPraiseList(HttpServletRequest request) {
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

        Page<Praise> rs = praiseService.list(paramMap, pageable);
        Map<String, Object> rsMap = Maps.newHashMap();
        MessagePage messagePage = null;
        if (rs != null && rs.getSize() != 0) {
            page.setTotalSize((int) rs.getTotalElements());
            if (StringUtils.isNotBlank(objectDefineID)) {
                switch (objectDefineID) {
                    case Config.MEMBER_OBJECTDEFINEID://会员对象定义id
                        List<PraiseMemberDto> praiseMemberList = BeanMapper.mapList(rs.getContent(), PraiseMemberDto.class);
                        Member collectMember = null;
                        for (PraiseMemberDto obj : praiseMemberList) {
                            collectMember = memberService.findById(obj.getObjectID());
                            if (collectMember != null) {
                                obj.setMemberID(collectMember.getId());
                                obj.setAvatarURL(collectMember.getAvatarURL());
                                obj.setMemberName(collectMember.getName());
                                obj.setCompanyName(collectMember.getCompanyName());
                                obj.setJobName(collectMember.getJobName());
                            }
                        }
                        messagePage = new MessagePage(page, praiseMemberList);
                        break;
                    default:
                        List<PraiseDto> list = BeanMapper.mapList(rs.getContent(), PraiseDto.class);
                        messagePage = new MessagePage(page, list);
                        break;
                }
            } else {
                List<PraiseDto> list = BeanMapper.mapList(rs.getContent(), PraiseDto.class);
                messagePage = new MessagePage(page, list);
            }

        } else {
            page.setTotalSize(0);
            messagePage = new MessagePage(page, new ArrayList());
        }
        rsMap.put("data", messagePage);

        String description = String.format("%s获取会员赞列表", member.getName());

        UserAgent userAgent = UserAgentUtil.getUserAgent(request.getHeader("user-agent"));
        MemberLogRequestBase base = MemberLogRequestBase.BALANCE()
                .sessionID(sessionID)
                .description(description)
                .userAgent(userAgent == null ? null : userAgent.getBrowserType())
                .operateType(Memberlog.MemberOperateType.GETPRAISELIST.getOname())
                .build();

        sendMessageService.sendMemberLogMessage(JacksonHelper.toJson(base));

        return MessagePacket.newSuccess(rsMap, "getPraiseList success!");
    }

    @ApiOperation(value = "获取对象赞列表", notes = "获取对象赞列表")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "sessionID", value = "登录标识", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "objectID", value = "对象id", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "currentPage", value = "分页，页码从1开始", dataType = "int"),
            @ApiImplicitParam(paramType = "query", name = "pageNumber", value = "每一页大小", dataType = "int")})
    @RequestMapping(value = "/getObjectPraiseList")
    public MessagePacket getObjectPraiseList(HttpServletRequest request) {
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

        Page<Praise> rs = praiseService.list(paramMap, pageable);
        Map<String, Object> rsMap = Maps.newHashMap();
        MessagePage messagePage = null;
        if (rs != null && rs.getSize() != 0) {
            page.setTotalSize((int) rs.getTotalElements());
            List<ObjectPraiseDto> list = BeanMapper.mapList(rs.getContent(), ObjectPraiseDto.class);
            messagePage = new MessagePage(page, list);
        } else {
            page.setTotalSize(0);
            messagePage = new MessagePage(page, new ArrayList());
        }
        rsMap.put("data", messagePage);
        return MessagePacket.newSuccess(rsMap, "getObjectPraiseList success!");
    }
}
