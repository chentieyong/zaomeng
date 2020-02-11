package com.kingpivot.api.controller.ApiPeopleController;

import com.google.common.collect.Maps;
import com.kingpivot.api.dto.people.PeopleDetailDto;
import com.kingpivot.base.config.RedisKey;
import com.kingpivot.base.config.UserAgent;
import com.kingpivot.base.member.model.Member;
import com.kingpivot.base.member.service.MemberService;
import com.kingpivot.base.memberlog.model.Memberlog;
import com.kingpivot.base.people.model.People;
import com.kingpivot.base.people.service.PeopleService;
import com.kingpivot.base.support.MemberLogDTO;
import com.kingpivot.common.jms.SendMessageService;
import com.kingpivot.common.jms.dto.memberLog.MemberLogRequestBase;
import com.kingpivot.common.utils.BeanMapper;
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
@Api(description = "实名信息管理接口")
public class ApiPeopleController extends ApiBaseController {

    @Resource
    private SendMessageService sendMessageService;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private PeopleService peopleService;
    @Autowired
    private MemberService memberService;

    @ApiOperation(value = "submitRealName", notes = "提交一个会员实名")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "sessionID", value = "登录标识", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "name", value = "名称", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "idNumber", value = "说明", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "faceImage", value = "开始日期", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "backImage", value = "结束日期", dataType = "String")
    })
    @RequestMapping(value = "/submitRealName")
    public MessagePacket submitRealName(HttpServletRequest request) {
        String sessionID = request.getParameter("sessionID");
        if (StringUtils.isEmpty(sessionID)) {
            return MessagePacket.newFail(MessageHeader.Code.unauth, "请先登录");
        }
        Member member = (Member) redisTemplate.opsForValue().get(String.format("%s%s", RedisKey.Key.MEMBER_KEY.key, sessionID));
        if (member == null) {
            return MessagePacket.newFail(MessageHeader.Code.unauth, "请先登录");
        }

        member = memberService.findById(member.getId());
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
        String idNumber = request.getParameter("idNumber");//身份证号
        if (StringUtils.isEmpty(idNumber)) {
            return MessagePacket.newFail(MessageHeader.Code.idNumberIsNull, "idNumber不能为空");
        }
        String faceImage = request.getParameter("faceImage");//正面照
        if (StringUtils.isEmpty(faceImage)) {
            return MessagePacket.newFail(MessageHeader.Code.faceImageIsNull, "faceImage不能为空");
        }
        String backImage = request.getParameter("backImage");//背面照
        if (StringUtils.isEmpty(backImage)) {
            return MessagePacket.newFail(MessageHeader.Code.backImageIsNull, "backImage不能为空");
        }

        People people = new People();
        people.setApplicationID(member.getApplicationID());
        people.setName(name);
        people.setIdNumber(idNumber);
        people.setFaceImage(faceImage);
        people.setBackImage(backImage);
        peopleService.save(people);

        member.setPeopleID(people.getId());
        memberService.save(member);

        String desc = String.format("%s提交一个会员实名", member.getName());
        UserAgent userAgent = UserAgentUtil.getUserAgent(request.getHeader("user-agent"));
        MemberLogRequestBase base = MemberLogRequestBase.BALANCE()
                .sessionID(sessionID)
                .description(desc)
                .userAgent(userAgent == null ? null : userAgent.getBrowserType())
                .operateType(Memberlog.MemberOperateType.SUBMITREALNAME.getOname())
                .build();
        sendMessageService.sendMemberLogMessage(JacksonHelper.toJson(base));

        Map<String, Object> rsMap = Maps.newHashMap();
        rsMap.put("data", people.getId());

        return MessagePacket.newSuccess(rsMap, "submitRealName success!");
    }

    @ApiOperation(value = "getPeopleDetail", notes = "获取实名详情")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "peopleID", value = "实名id", dataType = "String")})
    @RequestMapping(value = "/getPeopleDetail")
    public MessagePacket getPeopleDetail(HttpServletRequest request) {
        String peopleID = request.getParameter("peopleID");
        if (StringUtils.isEmpty(peopleID)) {
            return MessagePacket.newFail(MessageHeader.Code.peopleIDIsNull, "peopleID不能为空");
        }
        People people = peopleService.findById(peopleID);
        if (people == null) {
            return MessagePacket.newFail(MessageHeader.Code.peopleIDIsError, "peopleID不正确");
        }
        PeopleDetailDto data = BeanMapper.map(people, PeopleDetailDto.class);
        Map<String, Object> rsMap = Maps.newHashMap();
        rsMap.put("data", data);
        return MessagePacket.newSuccess(rsMap, "getPeopleDetail success!");
    }
}
