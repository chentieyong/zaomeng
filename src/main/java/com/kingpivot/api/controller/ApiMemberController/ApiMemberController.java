package com.kingpivot.api.controller.ApiMemberController;

import com.google.common.collect.Maps;
import com.kingpivot.api.dto.member.ApplicationMemberListDto;
import com.kingpivot.api.dto.member.MemberLoginDto;
import com.kingpivot.api.dto.member.MyChildrenMemberList;
import com.kingpivot.api.dto.memberstatistics.MemberStatisticsInfoDto;
import com.kingpivot.base.application.model.Application;
import com.kingpivot.base.application.service.ApplicationService;
import com.kingpivot.base.city.service.CityService;
import com.kingpivot.base.collect.service.CollectService;
import com.kingpivot.base.config.Config;
import com.kingpivot.base.config.RedisKey;
import com.kingpivot.base.config.UserAgent;
import com.kingpivot.base.member.model.Member;
import com.kingpivot.base.member.service.MemberService;
import com.kingpivot.base.memberBonus.service.MemberBonusService;
import com.kingpivot.base.memberMajor.service.MemberMajorService;
import com.kingpivot.base.memberlog.model.Memberlog;
import com.kingpivot.base.memberstatistics.model.MemberStatistics;
import com.kingpivot.base.memberstatistics.service.MemberStatisticsService;
import com.kingpivot.base.praise.service.PraiseService;
import com.kingpivot.base.rank.model.Rank;
import com.kingpivot.base.rank.service.RankService;
import com.kingpivot.base.site.model.Site;
import com.kingpivot.base.site.service.SiteService;
import com.kingpivot.base.sms.service.SMSService;
import com.kingpivot.base.smsTemplate.model.SmsTemplate;
import com.kingpivot.base.smsTemplate.service.SmsTemplateService;
import com.kingpivot.base.smsWay.model.SmsWay;
import com.kingpivot.base.smsWay.service.SmsWayService;
import com.kingpivot.base.support.MemberLogDTO;
import com.kingpivot.base.weiXinAppMember.model.WeiXinAppMember;
import com.kingpivot.base.weiXinAppMember.service.WeiXinAppMemberService;
import com.kingpivot.common.KingBase;
import com.kingpivot.common.jms.SendMessageService;
import com.kingpivot.common.jms.dto.memberLog.MemberLogRequestBase;
import com.kingpivot.common.jms.dto.memberLogin.MemberLoginRequestBase;
import com.kingpivot.common.util.Constants;
import com.kingpivot.common.util.sms.RadomMsgAuthCodeUtil;
import com.kingpivot.common.utils.*;
import com.kingpivot.protocol.ApiBaseController;
import com.kingpivot.protocol.MessageHeader;
import com.kingpivot.protocol.MessagePacket;
import com.kingpivot.protocol.MessagePage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private SmsWayService smsWayService;
    @Autowired
    private SMSService smsService;
    @Autowired
    private KingBase kingBase;
    @Autowired
    private SmsTemplateService smsTemplateService;
    @Autowired
    private MemberStatisticsService memberStatisticsService;
    @Autowired
    private MemberBonusService memberBonusService;
    @Autowired
    private RankService rankService;
    @Autowired
    private CollectService collectService;
    @Autowired
    private PraiseService praiseService;
    @Autowired
    private MemberMajorService memberMajorService;
    @Autowired
    private CityService cityService;
    @Autowired
    private WeiXinAppMemberService weiXinAppMemberService;

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
            case "1"://账号密码登录
                return loginNameLogin(request);
            case "2"://微信code登录
                return weixinCodeLogin(request);
            case "3"://微信小程序code登录
                return weiXinAppCodeLogin(request);
            case "4"://微信小程序phone登录
                return weiXinAppPhoneLogin(request);
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

        String authCode = stringRedisTemplate.opsForValue().get(String.format("%s_%s", CacheContant.REGISTER_AUTH_CODE, phone));
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
        rsMap.put("sessionID", request.getSession().getId());

        MemberLogDTO memberLogDTO = new MemberLogDTO(site.getId(), member.getApplicationID(),
                member.getId(), member.getCompanyID());

        putSession(request, member);

        putMemberLogSession(request, memberLogDTO);

        sendMemberLoginLog(request, member, true);

        stringRedisTemplate.delete(String.format("%s_%s", CacheContant.REGISTER_AUTH_CODE, phone));

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
                member.getId(), member.getCompanyID());

        putSession(request, member);

        putMemberLogSession(request, memberLogDTO);

        sendMemberLoginLog(request, member, false);

        return MessagePacket.newSuccess(rsMap, "appLogin success!");
    }

    /**
     * 微信小程序手机号登录
     *
     * @param request
     * @return
     */
    public MessagePacket weiXinAppPhoneLogin(HttpServletRequest request) {
        String name = request.getParameter("name");
        String code = request.getParameter("code");
        String siteID = request.getParameter("siteID");
        String avatarUrl = request.getParameter("avatarUrl");
        String phone = request.getParameter("phone");
        String vCode = request.getParameter("vCode");
        String recommandID = request.getParameter("recommandID");

        if (StringUtils.isEmpty(code)) {
            return MessagePacket.newFail(MessageHeader.Code.codeIsNull, "code为空");
        }
        if (StringUtils.isEmpty(phone)) {
            return MessagePacket.newFail(MessageHeader.Code.phoneIsNull, "手机号不能为空");
        }
        if (StringUtils.isEmpty(vCode)) {
            return MessagePacket.newFail(MessageHeader.Code.vCodeIsNull, "验证码为空");
        }

        String authCode = stringRedisTemplate.opsForValue().get(String.format("%s_%s", CacheContant.LOGIN_WEIXINAPPLOGIN_CODE, phone));
        if (StringUtils.isEmpty(authCode) || !vCode.equals(authCode)) {
            return MessagePacket.newFail(MessageHeader.Code.vCodeError, "验证码错误");
        }
        if (StringUtils.isEmpty(siteID)) {
            return MessagePacket.newFail(MessageHeader.Code.siteIdIsNull, "siteID为空");
        }
        Site site = siteService.findById(siteID);
        if (site == null) {
            return MessagePacket.newFail(MessageHeader.Code.siteIdError, "siteID不正确");
        }
        boolean isNew = false;//是否新会员
        Member member = memberService.getMemberByPhoneAndApplicationId(phone, site.getApplicationID());
        if (member == null) {
            isNew = true;
            member = new Member();
            member.setLoginName(name);
            member.setName(name);
            member.setShortName(name);
            member.setCompanyID(site.getCompanyID());
            member.setSiteID(site.getId());
            member.setApplicationID(site.getApplicationID());
            if (StringUtils.isNotBlank(recommandID)) {
                member.setRecommandID(recommandID);
            }
            String reCode = this.memberService.getCurRecommandCode(site.getApplicationID());
            if (StringUtils.isNotEmpty(reCode)) {
                member.setRecommandCode(NumberUtils.strFormat3(String.valueOf(Integer.valueOf(reCode) + 1)));
            } else {
                member.setRecommandCode("00001");
            }
            member.setPhone(phone);
            member.setWeixinToken(code);
            member.setAvatarURL(avatarUrl);
            memberService.save(member);
        }
        WeiXinAppMember weiXinAppMember = weiXinAppMemberService.getWeiXinAppMemberByCode(code);

        if (weiXinAppMember == null) {
            weiXinAppMember = new WeiXinAppMember();
            weiXinAppMember.setName(name);
            weiXinAppMember.setCode(code);
            weiXinAppMember.setSiteID(siteID);
            weiXinAppMember.setMemberID(member.getId());
            weiXinAppMemberService.save(weiXinAppMember);
        } else {
            weiXinAppMember.setMemberID(member.getId());
            weiXinAppMemberService.save(weiXinAppMember);
        }

        Map<String, Object> rsMap = Maps.newHashMap();
        rsMap.put("memberID", member.getId());
        rsMap.put("sessionID", request.getSession().getId());

        MemberLogDTO memberLogDTO = new MemberLogDTO(site.getId(), member.getApplicationID(), member.getId()
                , member.getCompanyID());

        putSession(request, member);

        putMemberLogSession(request, memberLogDTO);

        sendMemberLoginLog(request, member, isNew);

        stringRedisTemplate.delete(String.format("%s_%s", CacheContant.LOGIN_WEIXINAPPLOGIN_CODE, phone));

        return MessagePacket.newSuccess(rsMap, "weiXinAppPhoneLogin success!");
    }

    /**
     * 微信小程序code登录
     *
     * @param request
     * @return
     */
    public MessagePacket weiXinAppCodeLogin(HttpServletRequest request) {
        String code = request.getParameter("code");
        String siteID = request.getParameter("siteID");

        if (StringUtils.isEmpty(code)) {
            return MessagePacket.newFail(MessageHeader.Code.codeIsNull, "code为空");
        }
        if (StringUtils.isEmpty(siteID)) {
            return MessagePacket.newFail(MessageHeader.Code.siteIdIsNull, "siteID为空");
        }
        Site site = siteService.findById(siteID);
        if (site == null) {
            return MessagePacket.newFail(MessageHeader.Code.siteIdError, "siteID不正确");
        }

        Member member = memberService.getMemberByAppCode(code);
        if (member == null) {
            return MessagePacket.newFail(MessageHeader.Code.weixinAppMemberNull, "会员不存在");
        }

        Map<String, Object> rsMap = Maps.newHashMap();
        rsMap.put("memberID", member.getId());
        rsMap.put("sessionID", request.getSession().getId());

        MemberLogDTO memberLogDTO = new MemberLogDTO(site.getId(), member.getApplicationID(), member.getId()
                , member.getCompanyID());

        putSession(request, member);

        putMemberLogSession(request, memberLogDTO);

        sendMemberLoginLog(request, member, false);

        return MessagePacket.newSuccess(rsMap, "weiXinAppCodeLogin success!");
    }

    /**
     * 微信code登录
     *
     * @param request
     * @return
     */
    public MessagePacket weixinCodeLogin(HttpServletRequest request) {
        String name = request.getParameter("name");
        String code = request.getParameter("code");
        String siteID = request.getParameter("siteID");
        String avatarUrl = request.getParameter("avatarUrl");
        String recommandID = request.getParameter("recommandID");

        if (StringUtils.isEmpty(code)) {
            return MessagePacket.newFail(MessageHeader.Code.codeIsNull, "code为空");
        }
        if (StringUtils.isEmpty(siteID)) {
            return MessagePacket.newFail(MessageHeader.Code.siteIdIsNull, "siteID为空");
        }
        Site site = siteService.findById(siteID);
        if (site == null) {
            return MessagePacket.newFail(MessageHeader.Code.siteIdError, "siteID不正确");
        }
        boolean isNew = false;//是否新会员
        Member member = this.memberService.getMemberByWeixinCodeAndAppId(code, site.getApplicationID());
        if (member == null) {
            isNew = true;
            member = new Member();
            member.setLoginName(name);
            member.setName(name);
            member.setShortName(name);
            member.setCompanyID(site.getCompanyID());
            member.setSiteID(site.getId());
            member.setApplicationID(site.getApplicationID());
            if (StringUtils.isNotBlank(recommandID)) {
                member.setRecommandID(recommandID);
            }
            String reCode = this.memberService.getCurRecommandCode(site.getApplicationID());
            if (StringUtils.isNotEmpty(reCode)) {
                member.setRecommandCode(NumberUtils.strFormat3(String.valueOf(Integer.valueOf(reCode) + 1)));
            } else {
                member.setRecommandCode("00001");
            }
            member.setWeixinToken(code);
            member.setAvatarURL(avatarUrl);
            memberService.save(member);
        } else {
            if (member.getIsLock() == Constants.ISLOCK_YES) {
                return MessagePacket.newFail(MessageHeader.Code.memberIsLock, "会员被锁定");
            }
        }
        Map<String, Object> rsMap = Maps.newHashMap();
        rsMap.put("memberID", member.getId());
        rsMap.put("sessionID", request.getSession().getId());

        MemberLogDTO memberLogDTO = new MemberLogDTO(site.getId(), member.getApplicationID(), member.getId()
                , member.getCompanyID());

        putSession(request, member);

        putMemberLogSession(request, memberLogDTO);

        sendMemberLoginLog(request, member, isNew);

        return MessagePacket.newSuccess(rsMap, "appLogin success!");
    }

    @ApiOperation(value = "找回登录密码", notes = "找回登录密码")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "phone", value = "手机号", dataType = "String", required = true),
            @ApiImplicitParam(paramType = "query", name = "siteID", value = "站点id", dataType = "String", required = true),
            @ApiImplicitParam(paramType = "query", name = "vCode", value = "验证码", dataType = "String", required = true),
            @ApiImplicitParam(paramType = "query", name = "password", value = "密码", dataType = "String", required = true)})
    @RequestMapping(value = "/findLoginPassword")
    public MessagePacket findLoginPassword(HttpServletRequest request) {
        String phone = request.getParameter("phone");
        String password = request.getParameter("password");
        String vCode = request.getParameter("vCode");
        String siteID = request.getParameter("siteID");

        if (StringUtils.isEmpty(phone)) {
            return MessagePacket.newFail(MessageHeader.Code.phoneIsNull, "手机号不能为空");
        }

        if (StringUtils.isEmpty(password)) {
            return MessagePacket.newFail(MessageHeader.Code.passwordIsNull, "password为空");
        }

        if (StringUtils.isEmpty(vCode)) {
            return MessagePacket.newFail(MessageHeader.Code.vCodeIsNull, "验证码为空");
        }

        if (StringUtils.isEmpty(siteID)) {
            return MessagePacket.newFail(MessageHeader.Code.siteIdIsNull, "siteID为空");
        }

        Site site = siteService.findById(siteID);
        if (site == null) {
            return MessagePacket.newFail(MessageHeader.Code.siteIdError, "siteID不正确");
        }

        Member member = memberService.getMemberByPhoneAndApplicationId(phone, site.getApplicationID());

        if (member == null) {
            return MessagePacket.newFail(MessageHeader.Code.phoneIsError, "手机号不存在");
        }

        String authCode = stringRedisTemplate.opsForValue().get(String.format("%s_%s", CacheContant.FIND_LOGINPASSWORD_CODE, phone));
        if (StringUtils.isEmpty(authCode) || !vCode.equals(authCode)) {
            return MessagePacket.newFail(MessageHeader.Code.vCodeError, "验证码错误");
        }

        member.setLoginPassword(MD5.encodeMd5(String.format("%s%s", password, Config.ENCODE_KEY)));

        memberService.save(member);

        stringRedisTemplate.delete(String.format("%s_%s", CacheContant.FIND_LOGINPASSWORD_CODE, phone));

        Map<String, Object> rsMap = Maps.newHashMap();
        rsMap.put("data", TimeTest.getTimeStr());
        return MessagePacket.newSuccess(rsMap, "findLoginPassword success!");
    }

    @ApiOperation(value = "修改会员信息", notes = "修改会员信息")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "sessionID", value = "登录标识", dataType = "String", required = false),
            @ApiImplicitParam(paramType = "query", name = "name", value = "名称", dataType = "String", required = false),
            @ApiImplicitParam(paramType = "query", name = "avatarURL", value = "头像", dataType = "String", required = false)})
    @RequestMapping(value = "/updateMemberInfo")
    public MessagePacket updateMemberInfo(HttpServletRequest request) {
        String sessionID = request.getParameter("sessionID");
        String name = request.getParameter("name");
        String avatarURL = request.getParameter("avatarURL");
        String weixin = request.getParameter("weixin");
        String companyName = request.getParameter("companyName");
        String jobName = request.getParameter("jobName");
        String shortDescription = request.getParameter("shortDescription");
        String isShow = request.getParameter("isShow");
        String shengID = request.getParameter("shengID");
        String description = request.getParameter("description");
        String shiID = request.getParameter("shiID");
        String xianID = request.getParameter("xianID");
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

        Member updateMember = memberService.findById(member.getId());
        if (updateMember == null) {
            return MessagePacket.newFail(MessageHeader.Code.memberIDIsNull, "会员不存在");
        }
        if (StringUtils.isNotBlank(name)) {
            updateMember.setName(name);
            updateMember.setShortName(name);
        }
        if (StringUtils.isNotBlank(avatarURL)) {
            updateMember.setAvatarURL(avatarURL);
        }
        if (StringUtils.isNotBlank(weixin)) {
            updateMember.setWeixin(weixin);
        }
        if (StringUtils.isNotBlank(companyName)) {
            updateMember.setCompanyName(companyName);
        }
        if (StringUtils.isNotBlank(jobName)) {
            updateMember.setJobName(jobName);
        }
        if (StringUtils.isNotBlank(description)) {
            updateMember.setDescription(description);
        }
        if (StringUtils.isNotBlank(shortDescription)) {
            updateMember.setShortDescription(shortDescription);
        }
        if (StringUtils.isNotBlank(isShow)) {
            updateMember.setIsShow(Integer.parseInt(isShow));
        }
        if (StringUtils.isNotBlank(shengID)) {
            updateMember.setShengID(shengID);
        }
        if (StringUtils.isNotBlank(shiID)) {
            updateMember.setShiID(shiID);
        }
        if (StringUtils.isNotBlank(xianID)) {
            updateMember.setXianID(xianID);
        }
        memberService.save(updateMember);
        putSession(request, updateMember);
        String desc = String.format("%s修改会员信息", member.getName());

        UserAgent userAgent = UserAgentUtil.getUserAgent(request.getHeader("user-agent"));
        MemberLogRequestBase base = MemberLogRequestBase.BALANCE()
                .sessionID(sessionID)
                .description(desc)
                .userAgent(userAgent == null ? null : userAgent.getBrowserType())
                .operateType(Memberlog.MemberOperateType.UPDATEMEMBERINFO.getOname())
                .build();

        sendMessageService.sendMemberLogMessage(JacksonHelper.toJson(base));

        Map<String, Object> rsMap = Maps.newHashMap();
        rsMap.put("data", TimeTest.getTimeStr());
        return MessagePacket.newSuccess(rsMap, "updateMemberInfo success!");
    }

    @ApiOperation(value = "取消会员推荐关系", notes = "取消会员推荐关系")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "sessionID", value = "登录标识", dataType = "String", required = false),
            @ApiImplicitParam(paramType = "query", name = "name", value = "名称", dataType = "String", required = false),
            @ApiImplicitParam(paramType = "query", name = "avatarURL", value = "头像", dataType = "String", required = false)})
    @RequestMapping(value = "/cancelMemberRecommand")
    public MessagePacket cancelMemberRecommand(HttpServletRequest request) {
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
        String memberID = request.getParameter("memberID");
        Member childrenMember = memberService.findById(memberID);
        if (childrenMember == null) {
            return MessagePacket.newFail(MessageHeader.Code.memberIDIsNull, "会员不存在");
        }
        childrenMember.setRecommandID(null);
        childrenMember.setRecommandChain(null);
        memberService.save(childrenMember);
        String desc = String.format("%s取消%s会员推荐关系", member.getName(), childrenMember.getName());

        UserAgent userAgent = UserAgentUtil.getUserAgent(request.getHeader("user-agent"));
        MemberLogRequestBase base = MemberLogRequestBase.BALANCE()
                .sessionID(sessionID)
                .description(desc)
                .userAgent(userAgent == null ? null : userAgent.getBrowserType())
                .operateType(Memberlog.MemberOperateType.CANCELMEMBERRECOMMAND.getOname())
                .build();

        sendMessageService.sendMemberLogMessage(JacksonHelper.toJson(base));

        Map<String, Object> rsMap = Maps.newHashMap();
        rsMap.put("data", TimeTest.getTimeStr());
        return MessagePacket.newSuccess(rsMap, "cancelMemberRecommand success!");
    }

    @ApiOperation(value = "修改登录密码", notes = "修改登录密码")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "sessionID", value = "登录标识", dataType = "String", required = false),
            @ApiImplicitParam(paramType = "query", name = "newPassword", value = "新密码", dataType = "String", required = false),
            @ApiImplicitParam(paramType = "query", name = "oldPassword", value = "老密码", dataType = "String", required = false)})
    @RequestMapping(value = "/updateLoginPassword")
    public MessagePacket updateLoginPassword(HttpServletRequest request) {
        String sessionID = request.getParameter("sessionID");
        String oldPassword = request.getParameter("oldPassword");
        String newPassword = request.getParameter("newPassword");
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
        if (StringUtils.isEmpty(oldPassword)) {
            return MessagePacket.newFail(MessageHeader.Code.illegalParameter, "请输入旧密码");
        }
        if (StringUtils.isEmpty(newPassword)) {
            return MessagePacket.newFail(MessageHeader.Code.illegalParameter, "请输入新密码");
        }
        Member updateMember = memberService.findById(member.getId());
        if (updateMember == null) {
            return MessagePacket.newFail(MessageHeader.Code.memberIDIsNull, "会员不存在");
        }
        String oldPasswordMd5 = MD5.encodeMd5(String.format("%s%s", oldPassword, Config.ENCODE_KEY));
        if (!oldPasswordMd5.equals(updateMember.getLoginPassword())) {
            return MessagePacket.newFail(MessageHeader.Code.passwordError, "旧密码不正确");
        }
        updateMember.setLoginPassword(MD5.encodeMd5(String.format("%s%s", newPassword, Config.ENCODE_KEY)));
        memberService.save(updateMember);
        putSession(request, updateMember);
        String description = String.format("%s修改会员登录密码", member.getName());

        UserAgent userAgent = UserAgentUtil.getUserAgent(request.getHeader("user-agent"));
        MemberLogRequestBase base = MemberLogRequestBase.BALANCE()
                .sessionID(sessionID)
                .description(description)
                .userAgent(userAgent == null ? null : userAgent.getBrowserType())
                .operateType(Memberlog.MemberOperateType.UPDATELOGINPASSWORD.getOname())
                .build();

        sendMessageService.sendMemberLogMessage(JacksonHelper.toJson(base));

        Map<String, Object> rsMap = Maps.newHashMap();
        rsMap.put("data", TimeTest.getTimeStr());
        return MessagePacket.newSuccess(rsMap, "updateLoginPassword success!");
    }


    /**
     * 发送验证码
     * sendType：0-注册,2-找回登录密码
     *
     * @param request
     * @return
     */
    @ApiOperation(value = "发送验证码", notes = "发送验证码")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "phone", value = "手机号", dataType = "String", required = true),
            @ApiImplicitParam(paramType = "query", name = "siteID", value = "站点id", dataType = "String", required = true),
            @ApiImplicitParam(paramType = "query", name = "sendType", value = "发送类型", dataType = "String", required = true)})
    @RequestMapping(value = "/sendSmsCommon")
    public MessagePacket sendSmsCommon(HttpServletRequest request) throws Exception {
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


        String sendTypeName = "";
        String templateValue = "";
        switch (sendType) {
            case "0":
                String memberID = memberService.getMemberIdByPhoneAndApplicationId(phone, site.getApplicationID());
                if (StringUtils.isNotBlank(memberID)) {
                    return MessagePacket.newFail(MessageHeader.Code.phoneIsUsed, "手机号已注册");
                }
                sendTypeName = "注册";
                templateValue = CacheContant.REGISTER_AUTH_CODE;
                break;
            case "1":
                sendTypeName = "找回登录密码";
                templateValue = CacheContant.FIND_LOGINPASSWORD_CODE;
                break;
            case "2":
                sendTypeName = "微信小程序手机号登录";
                templateValue = CacheContant.LOGIN_WEIXINAPPLOGIN_CODE;
                break;
            default:
                return MessagePacket.newFail(MessageHeader.Code.sendTypeError, "发送类型不正确!");
        }
        if (StringUtils.isEmpty(site.getApplicationID())) {
            return MessagePacket.newFail(MessageHeader.Code.applicationIdIsNull, "站点未设置应用");
        }

        Application application = applicationService.findById(site.getApplicationID());

        if (application == null) {
            return MessagePacket.newFail(MessageHeader.Code.applicationIdIsError, "站点应用不存在");
        }

        if (StringUtils.isEmpty(application.getSmsWayID())) {
            return MessagePacket.newFail(MessageHeader.Code.smsWayIdIsNull, "应用未设置短信通道");
        }

        SmsWay smsWay = smsWayService.findById(application.getSmsWayID());

        if (smsWay == null) {
            return MessagePacket.newFail(MessageHeader.Code.smsWayIdIsError, "短信通道不存在");
        }

        SmsTemplate smsTemplate = smsTemplateService.getSmsTemplateBySmsWayIDAndTemplateValue(smsWay.getId(), templateValue);
        if (smsTemplate == null) {
            return MessagePacket.newFail(MessageHeader.Code.smsTemplateIsNull, "模板不存在");
        }
        String msg = "";
        String authCode = RadomMsgAuthCodeUtil.createRandom(true, 4);

        if (smsWay.getIsTest() != null && smsWay.getIsTest() == 1) {
            if (smsWay.getDayNumberTimes() == null) {
                smsWay.setDayNumberTimes(5);
            }

            if (smsWay.getIntervalMinute() == null) {
                smsWay.setIntervalMinute(5);
            }

            int numbers = smsService.getTodayCount(phone, sendTypeName);
            if (numbers >= smsWay.getDayNumberTimes()) {
                return MessagePacket.newFail(MessageHeader.Code.illegalParameter, "今日发送次数已超额");
            }

            if (numbers != 0) {
                int s = 0;
                if (numbers != 1) {
                    s = 1;
                }
                Timestamp sendDate = smsService.getPerSms(phone, sendTypeName, s, 1);
                if ((new Timestamp(System.currentTimeMillis()).getTime() / 1000 - sendDate.getTime() / 1000) < smsWay.getIntervalMinute() * 60) {
                    return MessagePacket.newFail(MessageHeader.Code.illegalParameter, "请勿重复请求");
                }
            }
            switch (smsWay.getSmsType()) {
                case 1:
                    String templateParam = smsTemplate.getTextDefine().replaceAll("CODE", authCode);
                    msg = SmsSendUtil.aliyunSmsSend(smsWay.getAccount(), smsWay.getPwd(), templateParam, smsTemplate.getTemplateCode(), smsWay.getSignName(), phone);
                    break;
            }
        }

        if (StringUtils.isEmpty(msg)) {
            kingBase.addSms(sendTypeName, authCode, smsTemplate.getDescription().replaceAll("CODE", authCode), phone, smsWay.getId());
            stringRedisTemplate.opsForValue().set(String.format("%s_%s", templateValue, phone), authCode, smsWay.getIntervalMinute() * 60, TimeUnit.SECONDS);
        } else {
            return MessagePacket.newFail(MessageHeader.Code.illegalParameter, msg);
        }

        Map<String, Object> rsMap = Maps.newHashMap();
        rsMap.put("data", authCode);
        return MessagePacket.newSuccess(rsMap, "sendSmsCommon success!");
    }

    @ApiOperation(value = "获取会员信息", notes = "获取会员信息")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "sessionID", value = "登录标识", dataType = "String", required = false)})
    @RequestMapping(value = "/getMemberInfo")
    public MessagePacket getMemberInfo(HttpServletRequest request) {
        String sessionID = request.getParameter("sessionID");
        String memberID = request.getParameter("memberID");
        if (StringUtils.isEmpty(sessionID) && StringUtils.isEmpty(memberID)) {
            return MessagePacket.newFail(MessageHeader.Code.unauth, "请先登录");
        }
        Member loginMember = null;
        if (StringUtils.isNotBlank(sessionID)) {
            loginMember = (Member) redisTemplate.opsForValue().get(String.format("%s%s", RedisKey.Key.MEMBER_KEY.key, sessionID));
            if (loginMember == null) {
                return MessagePacket.newFail(MessageHeader.Code.unauth, "请先登录");
            }
        }
        Member member = null;
        if (StringUtils.isNotBlank(memberID)) {
            member = memberService.findById(memberID);
        } else if (loginMember != null) {
            member = memberService.findById(loginMember.getId());
        }
        if (member == null) {
            return MessagePacket.newFail(MessageHeader.Code.memberIDIsNull, "会员不存在");
        }

        MemberLoginDto data = BeanMapper.map(member, MemberLoginDto.class);
        if (StringUtils.isNotBlank(data.getRankID())) {
            data.setRankName(rankService.getNameById(data.getRankID()));
        }
        if (StringUtils.isNotBlank(data.getRecommandID())) {
            Member recommandMember = memberService.findById(data.getRecommandID());
            if (recommandMember != null) {
                data.setRecommandMemberName(recommandMember.getName());
                data.setRecommandMemberCode(recommandMember.getRecommandCode());
                data.setRecommandMemberAvatarURL(recommandMember.getAvatarURL());
            }
        }
        if (loginMember != null) {
            data.setCollectID(collectService.getCollectByObjectIDAndMemberID(member.getId(), loginMember.getId()));
            data.setPraiseID(praiseService.getPraiseByObjectIDAndMemberID(member.getId(), loginMember.getId()));
        }
        if (StringUtils.isNotBlank(member.getShengID())) {
            data.setShengName(cityService.getNameById(member.getShengID()));
        }
        if (StringUtils.isNotBlank(member.getShiID())) {
            data.setShiName(cityService.getNameById(member.getShiID()));
        }
        if (StringUtils.isNotBlank(member.getXianID())) {
            data.setXianName(cityService.getNameById(member.getXianID()));
        }
        data.setMajorName(memberMajorService.getMajorNameByMemberId(member.getId()));
        Map<String, Object> rsMap = Maps.newHashMap();
        rsMap.put("data", data);

        if (StringUtils.isNotBlank(sessionID)) {
            String description = String.format("%s获取会员信息", member.getName());

            UserAgent userAgent = UserAgentUtil.getUserAgent(request.getHeader("user-agent"));
            MemberLogRequestBase base = MemberLogRequestBase.BALANCE()
                    .sessionID(sessionID)
                    .description(description)
                    .userAgent(userAgent == null ? null : userAgent.getBrowserType())
                    .operateType(Memberlog.MemberOperateType.GETMEMBERINFO.getOname())
                    .build();
            sendMessageService.sendMemberLogMessage(JacksonHelper.toJson(base));
        }
        return MessagePacket.newSuccess(rsMap, "getMemberInfo success!");
    }

    @ApiOperation(value = "获取会员统计信息", notes = "获取会员统计信息")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "sessionID", value = "登录标识", dataType = "String", required = false)})
    @RequestMapping(value = "/getMemberStatisticsInfo")
    public MessagePacket getMemberStatisticsInfo(HttpServletRequest request) {
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

        Member newMember = memberService.findById(member.getId());
        if (newMember == null) {
            return MessagePacket.newFail(MessageHeader.Code.illegalParameter, "会员统计信息不存在，请联系管理员");
        }

        MemberStatistics memberStatistics = memberStatisticsService.getByMemberId(member.getId());
        if (memberStatistics == null) {
            return MessagePacket.newFail(MessageHeader.Code.illegalParameter, "会员统计信息不存在，请联系管理员");
        }

        MemberStatisticsInfoDto data = BeanMapper.map(memberStatistics, MemberStatisticsInfoDto.class);
        data.setMemberBonusNum(memberBonusService.getMemberBonusNum(1, member.getId()));
        if (StringUtils.isNotBlank(newMember.getRankID())) {
            Rank rank = rankService.findById(newMember.getRankID());
            if (rank != null) {
                data.setRankName(rank.getName());
                data.setRankUrl(rank.getFaceImage());
            }
        }

        Map<String, Object> rsMap = Maps.newHashMap();
        rsMap.put("data", data);

        String description = String.format("%s获取会员统计信息", member.getName());

        UserAgent userAgent = UserAgentUtil.getUserAgent(request.getHeader("user-agent"));
        MemberLogRequestBase base = MemberLogRequestBase.BALANCE()
                .sessionID(sessionID)
                .description(description)
                .userAgent(userAgent == null ? null : userAgent.getBrowserType())
                .operateType(Memberlog.MemberOperateType.GETMEMBERSTATISTICS.getOname())
                .build();

        sendMessageService.sendMemberLogMessage(JacksonHelper.toJson(base));
        return MessagePacket.newSuccess(rsMap, "getMemberStatisticsInfo success!");
    }


    @ApiOperation(value = "获取应用会员列表", notes = "获取应用会员列表")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "currentPage", value = "分页，页码从1开始", dataType = "int"),
            @ApiImplicitParam(paramType = "query", name = "pageNumber", value = "每一页大小", dataType = "int"),
            @ApiImplicitParam(paramType = "query", name = "applicationID", value = "应用id", dataType = "String")})
    @RequestMapping(value = "/getApplicationMemberList")
    public MessagePacket getApplicationMemberList(HttpServletRequest request) {
        String applicationID = request.getParameter("applicationID");
        if (StringUtils.isEmpty(applicationID)) {
            return MessagePacket.newFail(MessageHeader.Code.applicationIdIsNull, "应用id不能为空");
        }
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("isShow", 1);
        paramMap.put("isValid", Constants.ISVALID_YES);
        paramMap.put("isLock", Constants.ISLOCK_NO);
        paramMap.put("applicationID", applicationID);

        List<Sort.Order> orders = new ArrayList<Sort.Order>();
        orders.add(new Sort.Order(Sort.Direction.DESC, "createdTime"));

        Object currentPage = request.getParameter("currentPage");
        Object pageNumber = request.getParameter("pageNumber");

        TPage page = ApiPageUtil.makePage(currentPage, pageNumber);

        Pageable pageable = new PageRequest(page.getOffset(), page.getPageSize(), new Sort(orders));

        Page<Member> rs = memberService.list(paramMap, pageable);

        List<ApplicationMemberListDto> list = null;
        if (rs != null && rs.getSize() != 0) {
            list = BeanMapper.mapList(rs.getContent(), ApplicationMemberListDto.class);
            page.setTotalSize((int) rs.getTotalElements());
        } else {
            list = new ArrayList<>();
        }

        Map<String, Object> rsMap = Maps.newHashMap();
        MessagePage messagePage = new MessagePage(page, list);
        rsMap.put("data", messagePage);

        return MessagePacket.newSuccess(rsMap, "getApplicationMemberList success!");
    }

    @ApiOperation(value = "获取我的下级会员列表", notes = "获取我的下级会员列表")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "currentPage", value = "分页，页码从1开始", dataType = "int"),
            @ApiImplicitParam(paramType = "query", name = "pageNumber", value = "每一页大小", dataType = "int"),
            @ApiImplicitParam(paramType = "query", name = "goodsCategoryID", value = "分类id", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "sessionID", value = "登录标识", dataType = "String")})
    @RequestMapping(value = "/getMyChildrenMemberList")
    public MessagePacket getMyChildrenMemberList(HttpServletRequest request) {
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
        paramMap.put("recommandID", member.getId());

        List<Sort.Order> orders = new ArrayList<Sort.Order>();
        orders.add(new Sort.Order(Sort.Direction.DESC, "createdTime"));

        Object currentPage = request.getParameter("currentPage");
        Object pageNumber = request.getParameter("pageNumber");

        TPage page = ApiPageUtil.makePage(currentPage, pageNumber);

        Pageable pageable = new PageRequest(page.getOffset(), page.getPageSize(), new Sort(orders));

        Page<Member> rs = memberService.list(paramMap, pageable);

        List<MyChildrenMemberList> list = null;
        if (rs != null && rs.getSize() != 0) {
            list = BeanMapper.mapList(rs.getContent(), MyChildrenMemberList.class);
            page.setTotalSize((int) rs.getTotalElements());
        } else {
            list = new ArrayList<>();
        }

        Map<String, Object> rsMap = Maps.newHashMap();
        MessagePage messagePage = new MessagePage(page, list);
        rsMap.put("data", messagePage);

        String description = String.format("%s获取我的下级会员列表", member.getName());

        UserAgent userAgent = UserAgentUtil.getUserAgent(request.getHeader("user-agent"));
        MemberLogRequestBase base = MemberLogRequestBase.BALANCE()
                .sessionID(sessionID)
                .description(description)
                .userAgent(userAgent == null ? null : userAgent.getBrowserType())
                .operateType(Memberlog.MemberOperateType.GETMYCHILDRENMEMBERLIST.getOname())
                .build();

        sendMessageService.sendMemberLogMessage(JacksonHelper.toJson(base));

        return MessagePacket.newSuccess(rsMap, "getMyChildrenMemberList success!");
    }

    //发送会员登录日志
    public void sendMemberLoginLog(HttpServletRequest request, Member member, boolean isNew) {
        UserAgent userAgent = UserAgentUtil.getUserAgent(request.getHeader("user-agent"));

        MemberLoginRequestBase base = MemberLoginRequestBase.BALANCE()
                .sessionID(request.getSession().getId())
                .description(String.format("%s登录[来自App：%s]", member.getLoginName(),
                        applicationService.getNameByAppid(member.getApplicationID())))
                .ipaddr(WebUtil.getRemortIP(request))
                .isnew(isNew)
                .userAgent(userAgent == null ? null : userAgent.getBrowserType())
                .operateType(Memberlog.MemberOperateType.MEMBER_LOGIN.getOname())
                .build();
        sendMessageService.sendMemberLoginMessage(JacksonHelper.toJson(base));
    }

    private void putMemberLogSession(HttpServletRequest request, MemberLogDTO memberLogDTO) {
        apiLoginSessionHelper.putLoginMemberDto(request, memberLogDTO);
    }

    private void putSession(HttpServletRequest request, Member member) {
        apiLoginSessionHelper.putCurrentMember(request, member);
    }
}
