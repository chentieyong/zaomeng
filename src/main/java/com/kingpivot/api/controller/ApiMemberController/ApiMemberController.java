package com.kingpivot.api.controller.ApiMemberController;

import com.google.common.collect.Maps;
import com.kingpivot.api.dto.member.MemberLoginDto;
import com.kingpivot.base.application.service.ApplicationService;
import com.kingpivot.base.config.Config;
import com.kingpivot.base.config.UserAgent;
import com.kingpivot.base.member.model.Member;
import com.kingpivot.base.member.service.MemberService;
import com.kingpivot.base.memberlog.model.Memberlog;
import com.kingpivot.base.site.model.Site;
import com.kingpivot.base.site.service.SiteService;
import com.kingpivot.base.support.MemberLogDTO;
import com.kingpivot.common.jms.SendMessageService;
import com.kingpivot.common.jms.dto.memberLog.MemberLogRequestBase;
import com.kingpivot.common.jms.dto.memberLogin.MemberLoginRequestBase;
import com.kingpivot.common.util.Constants;
import com.kingpivot.common.util.sms.RadomMsgAuthCodeUtil;
import com.kingpivot.common.utils.*;
import com.kingpivot.protocol.ApiBaseController;
import com.kingpivot.protocol.MessageHeader;
import com.kingpivot.protocol.MessagePacket;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@RequestMapping("/api")
@RestController
@Api(description = "会员管理接口")
public class ApiMemberController extends ApiBaseController {
    @Resource
    private SendMessageService sendMessageService;
    @Autowired
    private MemberService memberService;
    @Autowired
    private SiteService siteService;
    @Autowired
    private ApiLoginSessionHelper apiLoginSessionHelper;
    @Autowired
    private ApplicationService applicationService;
    @Autowired
    private StringRedisTemplate redisTemplate;

    @ApiOperation(value = "会员登录", notes = "会员登录")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "loginName", value = "账号", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "password", value = "密码", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "siteID", value = "站点id", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "loginType", value = "登录类型", dataType = "int")})
    @RequestMapping(value = "/appLogin")
    public MessagePacket appLogin(HttpServletRequest request) {
        String loginType = request.getParameter("loginType");
        if (StringUtils.isEmpty(loginType)) {
            return MessagePacket.newFail(MessageHeader.Code.loginTypeIsNull, "loginType不能为空");
        }
        switch (loginType) {
            case "1":
                return loginNameLogin(request);
            default:
                return MessagePacket.newFail(MessageHeader.Code.loginTypeIsError, "loginType不正确");
        }
    }

    @ApiOperation(value = "手机号注册", notes = "手机号注册")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "loginName", value = "账号", dataType = "String", required = true),
            @ApiImplicitParam(paramType = "query", name = "password", value = "密码", dataType = "String", required = true),
            @ApiImplicitParam(paramType = "query", name = "siteID", value = "站点id", dataType = "String", required = true),
            @ApiImplicitParam(paramType = "query", name = "recommandID", value = "推荐人id", dataType = "String", required = true),
            @ApiImplicitParam(paramType = "query", name = "loginType", value = "登录类型", dataType = "int", required = true)})
    @RequestMapping(value = "/phoneRegister")
    public MessagePacket phoneRegister(HttpServletRequest request) {
        String phone = request.getParameter("phone");
        String password = request.getParameter("password");
        String vCode = request.getParameter("vCode");
        String siteID = request.getParameter("siteID");
        String recommandID = request.getParameter("recommandID");

        if (StringUtils.isEmpty(phone)) {
            return MessagePacket.newFail(MessageHeader.Code.phoneIsNull, "手机号为空");
        }

        if (StringUtils.isEmpty(siteID)) {
            return MessagePacket.newFail(MessageHeader.Code.siteIdIsNull, "siteID为空");
        }

        Site site = siteService.findById(siteID);
        if (site == null) {
            return MessagePacket.newFail(MessageHeader.Code.siteIdError, "siteID不正确");
        }

        String memberID = memberService.getMemberIdByPhoneAndApplicationId(phone, site.getApplicationID());

        if (StringUtils.isNotBlank(memberID)) {
            return MessagePacket.newFail(MessageHeader.Code.phoneIsUsed, "手机号已注册");
        }

        if (StringUtils.isEmpty(password)) {
            return MessagePacket.newFail(MessageHeader.Code.passwordIsNull, "password为空");
        }

        if (StringUtils.isEmpty(vCode)) {
            return MessagePacket.newFail(MessageHeader.Code.vCodeIsNull, "验证码为空");
        }

        String authCode = redisTemplate.opsForValue().get(String.format("%s_%s", CacheContant.REGISTER_AUTH_CODE, phone));
        if (StringUtils.isEmpty(authCode) || !vCode.equals(authCode)) {
            return MessagePacket.newFail(MessageHeader.Code.vCodeError, "验证码错误");
        }

        Member member = new Member();
        member.setPhone(phone);
        member.setLoginName(phone);
        member.setName(phone);
        member.setShortName(phone);
        member.setCompanyID(site.getCompanyID());
        member.setSiteID(site.getId());
        member.setApplicationID(site.getApplicationID());
        member.setLoginPassword(MD5.encodeMd5(String.format("%s%s", password, Config.ENCODE_KEY)));
        if (StringUtils.isNotBlank(recommandID)) {
            member.setRecommandID(recommandID);
        }
        String reCode = this.memberService.getCurRecommandCode(site.getApplicationID());
        if (StringUtils.isNotEmpty(reCode)) {
            member.setRecommandCode(NumberUtils.strFormat3(String.valueOf(Integer.valueOf(reCode) + 1)));
        } else {
            member.setRecommandCode("00001");
        }
        memberService.save(member);

        Map<String, Object> rsMap = Maps.newHashMap();
        rsMap.put("data", BeanMapper.map(member, MemberLoginDto.class));

        MemberLogDTO memberLogDTO = new MemberLogDTO(site.getId(), member.getApplicationID(),
                null, member.getId(), null, member.getCompanyID());

        putSession(request, member);

        putMemberLogSession(request, memberLogDTO);

        sendMemberLoginLog(request, member, true);

        redisTemplate.delete(String.format("%s_%s", CacheContant.REGISTER_AUTH_CODE, phone));

        return MessagePacket.newSuccess(rsMap, "phoneRegister success!");
    }

    /**
     * 账号密码登录
     *
     * @param request
     * @return
     */
    public MessagePacket loginNameLogin(HttpServletRequest request) {
        String loginName = request.getParameter("loginName");
        String password = request.getParameter("password");
        String siteID = request.getParameter("siteID");

        if (StringUtils.isEmpty(loginName)) {
            return MessagePacket.newFail(MessageHeader.Code.loginNameIsNull, "loginName为空");
        }
        if (StringUtils.isEmpty(password)) {
            return MessagePacket.newFail(MessageHeader.Code.passwordIsNull, "password为空");
        }
        if (StringUtils.isEmpty(siteID)) {
            return MessagePacket.newFail(MessageHeader.Code.siteIdIsNull, "siteID为空");
        }
        Site site = siteService.findById(siteID);
        if (site == null) {
            return MessagePacket.newFail(MessageHeader.Code.siteIdError, "siteID不正确");
        }
        Member member = this.memberService.getMemberByLoginNameAndApplicationId(loginName, site.getApplicationID());
        if (member == null) {
            return MessagePacket.newFail(MessageHeader.Code.loginNameIsError, "登录名不存在");
        }
        String encodePassword = MD5.encodeMd5(String.format("%s%s", password, Config.ENCODE_KEY));

        //判断密码是否正确&会员是否被锁定
        if (!encodePassword.equals(member.getLoginPassword())) {
            return MessagePacket.newFail(MessageHeader.Code.passwordError, "密码错误");
        }

        if (member.getIsLock() == Constants.ISLOCK_YES) {
            return MessagePacket.newFail(MessageHeader.Code.memberIsLock, "会员被锁定");
        }
        Map<String, Object> rsMap = Maps.newHashMap();
        rsMap.put("data", BeanMapper.map(member, MemberLoginDto.class));
        rsMap.put("sessionID", request.getSession().getId());

        MemberLogDTO memberLogDTO = new MemberLogDTO(site.getId(), member.getApplicationID(),
                null, member.getId(), null, member.getCompanyID());

        putSession(request, member);

        putMemberLogSession(request, memberLogDTO);

        sendMemberLoginLog(request, member, false);

        return MessagePacket.newSuccess(rsMap, "appLogin success!");
    }

    /**
     * 发送验证码
     * sendType：0-注册
     *
     * @param request
     * @return
     */
    @RequestMapping("sendSmsCommon")
    public MessagePacket sendSmsCommon(HttpServletRequest request) {
        String phone = request.getParameter("phone");
        String siteID = request.getParameter("siteID");
        String sendType = request.getParameter("sendType");
        if (StringUtils.isEmpty(phone)) {
            return MessagePacket.newFail(MessageHeader.Code.phoneIsNull, "手机号为空");
        }
        if (StringUtils.isBlank(sendType)) {
            return MessagePacket.newFail(MessageHeader.Code.sendTypeIsNull, "发送类型不能为空!");
        }
        if (StringUtils.isEmpty(siteID)) {
            return MessagePacket.newFail(MessageHeader.Code.siteIdIsNull, "siteID为空");
        }
        Site site = siteService.findById(siteID);
        if (site == null) {
            return MessagePacket.newFail(MessageHeader.Code.siteIdError, "siteID不正确");
        }

        if ("0".equals(sendType)) {
            String memberID = memberService.getMemberIdByPhoneAndApplicationId(phone, site.getApplicationID());
            if (StringUtils.isNotBlank(memberID)) {
                return MessagePacket.newFail(MessageHeader.Code.phoneIsUsed, "手机号已注册");
            }
        }

        String authCode = RadomMsgAuthCodeUtil.createRandom(true, 4);
        redisTemplate.opsForValue().set(String.format("%s_%s", CacheContant.REGISTER_AUTH_CODE, phone), authCode, 300, TimeUnit.SECONDS);

        Map<String, Object> rsMap = Maps.newHashMap();
        rsMap.put("vCode", authCode);
        return MessagePacket.newSuccess(rsMap, "sendSmsCommon success!");
    }

    //发送会员登录日志
    public void sendMemberLoginLog(HttpServletRequest request, Member member, boolean isNew) {
        UserAgent userAgent = UserAgentUtil.getUserAgent(request.getHeader("user-agent"));

        MemberLogRequestBase base = MemberLogRequestBase.BALANCE()
                .sessionID(request.getSession().getId())
                .description(String.format("%s登录[来自App：%s]", member.getLoginName(),
                        applicationService.getNameByAppid(member.getApplicationID())))
                .ipaddr(WebUtil.getRemortIP(request))
                .userAgent(userAgent == null ? null : userAgent.getBrowserType())
                .operateType(Memberlog.MemberOperateType.MEMBER_LOGIN.getOname())
                .build();
        sendMessageService.sendMemberLogMessage(JacksonHelper.toJson(base));
    }

    private void putMemberLogSession(HttpServletRequest request, MemberLogDTO memberLogDTO) {
        apiLoginSessionHelper.putLoginMemberDto(request, memberLogDTO);
    }

    private void putSession(HttpServletRequest request, Member member) {
        apiLoginSessionHelper.putCurrentMember(request, member);
    }
}
