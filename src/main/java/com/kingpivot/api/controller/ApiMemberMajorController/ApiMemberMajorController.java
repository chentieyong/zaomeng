package com.kingpivot.api.controller.ApiMemberMajorController;

import com.google.common.collect.Maps;
import com.kingpivot.base.config.RedisKey;
import com.kingpivot.base.config.UserAgent;
import com.kingpivot.base.major.model.Major;
import com.kingpivot.base.major.service.MajorService;
import com.kingpivot.base.member.model.Member;
import com.kingpivot.base.memberMajor.model.MemberMajor;
import com.kingpivot.base.memberMajor.service.MemberMajorService;
import com.kingpivot.base.memberlog.model.Memberlog;
import com.kingpivot.base.support.MemberLogDTO;
import com.kingpivot.common.jms.SendMessageService;
import com.kingpivot.common.jms.dto.memberLog.MemberLogRequestBase;
import com.kingpivot.common.utils.JacksonHelper;
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
@Api(description = "会员专业管理接口")
public class ApiMemberMajorController extends ApiBaseController {

    @Resource
    private SendMessageService sendMessageService;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private MemberMajorService memberMajorService;
    @Autowired
    private MajorService majorService;

    @ApiOperation(value = "申请一个专业身份", notes = "申请一个专业身份")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "name", value = "名称", dataType = "int"),
            @ApiImplicitParam(paramType = "query", name = "phone", value = "电话", dataType = "int"),
            @ApiImplicitParam(paramType = "query", name = "description", value = "说明", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "majorID", value = "专业id", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "shengID", value = "省id", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "shiID", value = "市id", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "xianID", value = "县id", dataType = "String"),
    })
    @RequestMapping(value = "/applyOneMajor")
    public MessagePacket applyOneMajor(HttpServletRequest request) {
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
        String phone = request.getParameter("phone");
        if (StringUtils.isEmpty(phone)) {
            return MessagePacket.newFail(MessageHeader.Code.phoneIsNull, "phone不能为空");
        }
        String description = request.getParameter("description");
        String majorID = request.getParameter("majorID");
        if (StringUtils.isEmpty(majorID)) {
            return MessagePacket.newFail(MessageHeader.Code.majorIDIsNull, "majorID不能为空");
        }

        Major major = majorService.findById(majorID);
        if (major == null) {
            return MessagePacket.newFail(MessageHeader.Code.majorIDIsNull, "专业记录不存在");
        }

        if (memberMajorService.isApply(majorID, member.getId())) {
            return MessagePacket.newFail(MessageHeader.Code.majorIsApply, "请勿重复申请");
        }

        if (major.getUpgradeNumber() <= major.getAlreadyUpgradeNumber()) {
            return MessagePacket.newFail(MessageHeader.Code.majorIsFull, "升级个数已满");
        }

        String shengID = request.getParameter("shengID");
        String shiID = request.getParameter("shiID");
        String xianID = request.getParameter("xianID");

        MemberMajor memberMajor = new MemberMajor();
        memberMajor.setName(name);
        memberMajor.setMemberID(member.getId());
        memberMajor.setApplicationID(member.getApplicationID());
        memberMajor.setPhone(phone);
        memberMajor.setDescription(description);
        memberMajor.setMajorID(majorID);
        if (StringUtils.isNotBlank(shengID)) {
            memberMajor.setShengID(shengID);
        }
        if (StringUtils.isNotBlank(shiID)) {
            memberMajor.setShiID(shiID);
        }
        if (StringUtils.isNotBlank(xianID)) {
            memberMajor.setXianID(xianID);
        }
        memberMajor.setStatus(1);
        memberMajorService.save(memberMajor);

        //更新数量
        major.setAlreadyUpgradeNumber(major.getAlreadyUpgradeNumber() + 1);
        majorService.save(major);

        Map<String, Object> rsMap = Maps.newHashMap();
        rsMap.put("data", memberMajor.getId());

        String desc = String.format("%s申请一个专业身份", member.getName());

        UserAgent userAgent = UserAgentUtil.getUserAgent(request.getHeader("user-agent"));
        MemberLogRequestBase base = MemberLogRequestBase.BALANCE()
                .sessionID(sessionID)
                .description(desc)
                .userAgent(userAgent == null ? null : userAgent.getBrowserType())
                .operateType(Memberlog.MemberOperateType.APPLYONEMAJOR.getOname())
                .build();

        sendMessageService.sendMemberLogMessage(JacksonHelper.toJson(base));

        return MessagePacket.newSuccess(rsMap, "applyOneMajor success!");
    }

    @ApiOperation(value = "修改一个会员专业", notes = "修改一个会员专业")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "memberMajorID", value = "会员专业id", dataType = "int"),
            @ApiImplicitParam(paramType = "query", name = "name", value = "名称", dataType = "int"),
            @ApiImplicitParam(paramType = "query", name = "phone", value = "电话", dataType = "int"),
            @ApiImplicitParam(paramType = "query", name = "description", value = "说明", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "majorID", value = "专业id", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "shengID", value = "省id", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "shiID", value = "市id", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "xianID", value = "县id", dataType = "String"),
    })
    @RequestMapping(value = "/updateOneMemberMajor")
    public MessagePacket updateOneMemberMajor(HttpServletRequest request) {
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
        String memberMajorID = request.getParameter("memberMajorID");
        if (StringUtils.isEmpty(memberMajorID)) {
            return MessagePacket.newFail(MessageHeader.Code.memberMajorIDIsNull, "memberMajorID不能为空");
        }
        MemberMajor memberMajor = memberMajorService.findById(memberMajorID);
        if (memberMajor == null) {
            return MessagePacket.newFail(MessageHeader.Code.memberMajorIDIsError, "memberMajorID不正确");
        }
        String name = request.getParameter("name");
        String phone = request.getParameter("phone");
        String description = request.getParameter("description");
        String shengID = request.getParameter("shengID");
        String shiID = request.getParameter("shiID");
        String xianID = request.getParameter("xianID");

        if (StringUtils.isNotBlank(name)) {
            memberMajor.setName(name);
        }
        if (StringUtils.isNotBlank(phone)) {
            memberMajor.setPhone(phone);
        }
        if (StringUtils.isNotBlank(description)) {
            memberMajor.setDescription(description);
        }
        if (StringUtils.isNotBlank(shengID)) {
            memberMajor.setShengID(shengID);
        }
        if (StringUtils.isNotBlank(shiID)) {
            memberMajor.setShiID(shiID);
        }
        if (StringUtils.isNotBlank(xianID)) {
            memberMajor.setXianID(xianID);
        }
        memberMajorService.save(memberMajor);

        Map<String, Object> rsMap = Maps.newHashMap();
        rsMap.put("data", memberMajor.getId());

        String desc = String.format("%s修改一个会员专业", member.getName());

        UserAgent userAgent = UserAgentUtil.getUserAgent(request.getHeader("user-agent"));
        MemberLogRequestBase base = MemberLogRequestBase.BALANCE()
                .sessionID(sessionID)
                .description(desc)
                .userAgent(userAgent == null ? null : userAgent.getBrowserType())
                .operateType(Memberlog.MemberOperateType.UPDATEONEMEMBERMAJOR.getOname())
                .build();

        sendMessageService.sendMemberLogMessage(JacksonHelper.toJson(base));

        return MessagePacket.newSuccess(rsMap, "updateOneMemberMajor success!");
    }

    @ApiOperation(value = "删除一个会员专业", notes = "删除一个会员专业")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "memberMajorID", value = "会员专业id", dataType = "int"),
            @ApiImplicitParam(paramType = "query", name = "name", value = "名称", dataType = "int"),
            @ApiImplicitParam(paramType = "query", name = "phone", value = "电话", dataType = "int"),
            @ApiImplicitParam(paramType = "query", name = "description", value = "说明", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "majorID", value = "专业id", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "shengID", value = "省id", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "shiID", value = "市id", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "xianID", value = "县id", dataType = "String"),
    })
    @RequestMapping(value = "/deleteOneMemberMajor")
    public MessagePacket deleteOneMemberMajor(HttpServletRequest request) {
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
        String memberMajorID = request.getParameter("memberMajorID");
        if (StringUtils.isEmpty(memberMajorID)) {
            return MessagePacket.newFail(MessageHeader.Code.memberMajorIDIsNull, "memberMajorID不能为空");
        }
        MemberMajor memberMajor = memberMajorService.findById(memberMajorID);
        if (memberMajor == null) {
            return MessagePacket.newFail(MessageHeader.Code.memberMajorIDIsError, "memberMajorID不正确");
        }
        memberMajor.setIsValid(0);
        memberMajorService.save(memberMajor);

        Map<String, Object> rsMap = Maps.newHashMap();
        rsMap.put("data", memberMajor.getId());

        String desc = String.format("%s删除一个会员专业", member.getName());

        UserAgent userAgent = UserAgentUtil.getUserAgent(request.getHeader("user-agent"));
        MemberLogRequestBase base = MemberLogRequestBase.BALANCE()
                .sessionID(sessionID)
                .description(desc)
                .userAgent(userAgent == null ? null : userAgent.getBrowserType())
                .operateType(Memberlog.MemberOperateType.DELETEONEMEMBERMAJOR.getOname())
                .build();

        sendMessageService.sendMemberLogMessage(JacksonHelper.toJson(base));

        return MessagePacket.newSuccess(rsMap, "deleteOneMemberMajor success!");
    }
}
