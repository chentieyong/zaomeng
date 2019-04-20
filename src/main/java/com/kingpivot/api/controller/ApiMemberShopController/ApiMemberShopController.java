package com.kingpivot.api.controller.ApiMemberShopController;

import com.google.common.collect.Maps;
import com.kingpivot.base.config.RedisKey;
import com.kingpivot.base.config.UserAgent;
import com.kingpivot.base.member.model.Member;
import com.kingpivot.base.memberShop.model.MemberShop;
import com.kingpivot.base.memberShop.service.MemberShopService;
import com.kingpivot.base.memberlog.model.Memberlog;
import com.kingpivot.base.support.MemberLogDTO;
import com.kingpivot.common.jms.SendMessageService;
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
@Api(description = "会员店铺管理接口")
public class ApiMemberShopController extends ApiBaseController {

    @Resource
    private SendMessageService sendMessageService;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private MemberShopService memberShopService;

    @ApiOperation(value = "申请会员店铺", notes = "申请会员店铺")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "sessionID", value = "登录标识", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "name", value = "名称", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "shopCategoryID", value = "店铺分类id", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "shopFaceImage", value = "店铺正面照", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "businessImage", value = "营业执照", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "address", value = "地址", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "contact", value = "联系人", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "contactPhone", value = "联系电话", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "contactIdCardFaceImage", value = "联系人身份证正面", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "contactIdCardBackImage", value = "联系人身份证反面", dataType = "String")})
    @RequestMapping(value = "/applyMemberShop")
    public MessagePacket applyMemberShop(HttpServletRequest request) {
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
        String shopCategoryID = request.getParameter("shopCategoryID");
        String shopFaceImage = request.getParameter("shopFaceImage");
        String businessImage = request.getParameter("businessImage");
        String address = request.getParameter("address");
        String contact = request.getParameter("contact");
        String contactPhone = request.getParameter("contactPhone");
        String contactIdCardFaceImage = request.getParameter("contactIdCardFaceImage");
        String contactIdCardBackImage = request.getParameter("contactIdCardBackImage");

        MemberShop memberShop = new MemberShop();
        memberShop.setName(name);
        memberShop.setMemberID(member.getId());
        memberShop.setApplicationID(member.getApplicationID());
        if(StringUtils.isNotBlank(shopCategoryID)){
            memberShop.setShopCategoryID(shopCategoryID);
        }
        memberShop.setShopFaceImage(shopFaceImage);
        memberShop.setBusinessImage(businessImage);
        memberShop.setAddress(address);
        memberShop.setContact(contact);
        memberShop.setContactPhone(contactPhone);
        memberShop.setContactIdCardBackImage(contactIdCardBackImage);
        memberShop.setContactIdCardFaceImage(contactIdCardFaceImage);
        memberShopService.save(memberShop);

        String description = String.format("%s申请会员店铺", member.getName());

        UserAgent userAgent = UserAgentUtil.getUserAgent(request.getHeader("user-agent"));
        MemberLogRequestBase base = MemberLogRequestBase.BALANCE()
                .sessionID(sessionID)
                .description(description)
                .userAgent(userAgent == null ? null : userAgent.getBrowserType())
                .operateType(Memberlog.MemberOperateType.APPLYMEMBERSHOP.getOname())
                .build();

        sendMessageService.sendMemberLogMessage(JacksonHelper.toJson(base));

        Map<String, Object> rsMap = Maps.newHashMap();
        rsMap.put("memberShopID", memberShop.getId());

        return MessagePacket.newSuccess(rsMap, "applyMemberShop success!");
    }

    @ApiOperation(value = "修改会员店铺", notes = "修改会员店铺")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "sessionID", value = "登录标识", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "memberShopID", value = "会员店铺id", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "name", value = "名称", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "shopCategoryID", value = "店铺分类id", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "shopFaceImage", value = "店铺正面照", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "businessImage", value = "营业执照", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "address", value = "地址", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "contact", value = "联系人", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "contactPhone", value = "联系电话", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "contactIdCardFaceImage", value = "联系人身份证正面", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "contactIdCardBackImage", value = "联系人身份证反面", dataType = "String")})
    @RequestMapping(value = "/updateMemberShop")
    public MessagePacket updateMemberShop(HttpServletRequest request) {
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
        String memberShopID = request.getParameter("memberShopID");
        if(StringUtils.isEmpty(memberShopID)){
            return MessagePacket.newFail(MessageHeader.Code.memberShopIDIsNull, "memberShopID不能为空");
        }

        MemberShop memberShop = memberShopService.findById(memberShopID);

        if(memberShop==null){
            return MessagePacket.newFail(MessageHeader.Code.memberShopIDIsError, "memberShopID不正确");
        }

        String name = request.getParameter("name");
        String shopCategoryID = request.getParameter("shopCategoryID");
        String shopFaceImage = request.getParameter("shopFaceImage");
        String businessImage = request.getParameter("businessImage");
        String address = request.getParameter("address");
        String contact = request.getParameter("contact");
        String contactPhone = request.getParameter("contactPhone");
        String contactIdCardFaceImage = request.getParameter("contactIdCardFaceImage");
        String contactIdCardBackImage = request.getParameter("contactIdCardBackImage");

        memberShop.setName(name);
        memberShop.setMemberID(member.getId());
        memberShop.setApplicationID(member.getApplicationID());
        if(StringUtils.isNotBlank(shopCategoryID)){
            memberShop.setShopCategoryID(shopCategoryID);
        }
        memberShop.setShopFaceImage(shopFaceImage);
        memberShop.setBusinessImage(businessImage);
        memberShop.setAddress(address);
        memberShop.setContact(contact);
        memberShop.setContactPhone(contactPhone);
        memberShop.setContactIdCardBackImage(contactIdCardBackImage);
        memberShop.setContactIdCardFaceImage(contactIdCardFaceImage);
        memberShopService.save(memberShop);

        String description = String.format("%s修改会员店铺", member.getName());

        UserAgent userAgent = UserAgentUtil.getUserAgent(request.getHeader("user-agent"));
        MemberLogRequestBase base = MemberLogRequestBase.BALANCE()
                .sessionID(sessionID)
                .description(description)
                .userAgent(userAgent == null ? null : userAgent.getBrowserType())
                .operateType(Memberlog.MemberOperateType.UPDATEMEMBERSHOP.getOname())
                .build();

        sendMessageService.sendMemberLogMessage(JacksonHelper.toJson(base));

        Map<String, Object> rsMap = Maps.newHashMap();
        rsMap.put("updateTime", TimeTest.getNowDateFormat());

        return MessagePacket.newSuccess(rsMap, "updateMemberShop success!");
    }

}
