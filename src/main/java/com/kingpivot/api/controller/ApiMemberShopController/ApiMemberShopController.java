package com.kingpivot.api.controller.ApiMemberShopController;

import com.google.common.collect.Maps;
import com.kingpivot.api.dto.memberShop.MemberShopDetailDto;
import com.kingpivot.api.dto.memberShop.MemberShopListDto;
import com.kingpivot.base.category.model.Category;
import com.kingpivot.base.category.service.CategoryService;
import com.kingpivot.base.config.RedisKey;
import com.kingpivot.base.config.UserAgent;
import com.kingpivot.base.member.model.Member;
import com.kingpivot.base.memberShop.model.MemberShop;
import com.kingpivot.base.memberShop.service.MemberShopService;
import com.kingpivot.base.memberlog.model.Memberlog;
import com.kingpivot.base.support.MemberLogDTO;
import com.kingpivot.common.jms.SendMessageService;
import com.kingpivot.common.jms.dto.memberLog.MemberLogRequestBase;
import com.kingpivot.common.util.Constants;
import com.kingpivot.common.utils.*;
import com.kingpivot.protocol.ApiBaseController;
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
@Api(description = "会员店铺管理接口")
public class ApiMemberShopController extends ApiBaseController {

    @Resource
    private SendMessageService sendMessageService;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private MemberShopService memberShopService;
    @Autowired
    private CategoryService categoryService;

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

        if (StringUtils.isEmpty(name)) {
            return MessagePacket.newFail(MessageHeader.Code.nameIsNull, "name不能为空");
        }

        if (StringUtils.isEmpty(address)) {
            return MessagePacket.newFail(MessageHeader.Code.addressIsNull, "address不能为空");
        }

        String id = memberShopService.getIdByAddress(address);
        if (StringUtils.isNotBlank(id)) {
            return MessagePacket.newFail(MessageHeader.Code.illegalParameter, "已申请");
        }

        if (StringUtils.isEmpty(shopCategoryID)) {
            return MessagePacket.newFail(MessageHeader.Code.shopCategoryIDIsNull, "shopCategoryID不能为空");
        }

        Category category = categoryService.findById(shopCategoryID);
        if (category == null) {
            return MessagePacket.newFail(MessageHeader.Code.shopCategoryIDIsError, "shopCategoryID不正确");
        }

        if (StringUtils.isEmpty(shopFaceImage)) {
            return MessagePacket.newFail(MessageHeader.Code.shopFaceImageIsNull, "shopFaceImage不能为空");
        }

        if (StringUtils.isEmpty(businessImage)) {
            return MessagePacket.newFail(MessageHeader.Code.businessImageIsNull, "businessImage不能为空");
        }

        if (StringUtils.isEmpty(contact)) {
            return MessagePacket.newFail(MessageHeader.Code.contactIsNull, "contact不能为空");
        }

        if (StringUtils.isEmpty(contactPhone)) {
            return MessagePacket.newFail(MessageHeader.Code.contactPhoneIsNull, "contactPhone不能为空");
        }

        if (StringUtils.isEmpty(contactIdCardFaceImage)) {
            return MessagePacket.newFail(MessageHeader.Code.contactIdCardFaceImageIsNull, "contactIdCardFaceImage不能为空");
        }

        if (StringUtils.isEmpty(contactIdCardBackImage)) {
            return MessagePacket.newFail(MessageHeader.Code.contactIdCardBackImageIsNull, "contactIdCardBackImage不能为空");
        }

        MemberShop memberShop = new MemberShop();
        memberShop.setName(name);
        memberShop.setMemberID(member.getId());
        memberShop.setApplicationID(member.getApplicationID());
        memberShop.setShopCategoryID(shopCategoryID);
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
        rsMap.put("data", memberShop.getId());

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
        if (StringUtils.isEmpty(memberShopID)) {
            return MessagePacket.newFail(MessageHeader.Code.memberShopIDIsNull, "memberShopID不能为空");
        }

        MemberShop memberShop = memberShopService.findById(memberShopID);

        if (memberShop == null) {
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

        if (StringUtils.isEmpty(name)) {
            return MessagePacket.newFail(MessageHeader.Code.nameIsNull, "name不能为空");
        }

        if (StringUtils.isEmpty(shopCategoryID)) {
            return MessagePacket.newFail(MessageHeader.Code.shopCategoryIDIsNull, "shopCategoryID不能为空");
        }

        Category category = categoryService.findById(shopCategoryID);
        if (category == null) {
            return MessagePacket.newFail(MessageHeader.Code.shopCategoryIDIsError, "shopCategoryID不正确");
        }

        if (StringUtils.isEmpty(shopFaceImage)) {
            return MessagePacket.newFail(MessageHeader.Code.shopFaceImageIsNull, "shopFaceImage不能为空");
        }

        if (StringUtils.isEmpty(businessImage)) {
            return MessagePacket.newFail(MessageHeader.Code.businessImageIsNull, "businessImage不能为空");
        }

        if (StringUtils.isEmpty(contact)) {
            return MessagePacket.newFail(MessageHeader.Code.contactIsNull, "contact不能为空");
        }

        if (StringUtils.isEmpty(contactPhone)) {
            return MessagePacket.newFail(MessageHeader.Code.contactPhoneIsNull, "contactPhone不能为空");
        }

        if (StringUtils.isEmpty(contactIdCardFaceImage)) {
            return MessagePacket.newFail(MessageHeader.Code.contactIdCardFaceImageIsNull, "contactIdCardFaceImage不能为空");
        }

        if (StringUtils.isEmpty(contactIdCardBackImage)) {
            return MessagePacket.newFail(MessageHeader.Code.contactIdCardBackImageIsNull, "contactIdCardBackImage不能为空");
        }

        memberShop.setName(name);
        memberShop.setMemberID(member.getId());
        memberShop.setApplicationID(member.getApplicationID());
        memberShop.setShopCategoryID(shopCategoryID);
        memberShop.setShopFaceImage(shopFaceImage);
        memberShop.setBusinessImage(businessImage);
        if (StringUtils.isNotBlank(address) && !address.equals(memberShop.getAddress())) {
            String id = memberShopService.getIdByAddress(address);
            if (StringUtils.isNotBlank(id)) {
                return MessagePacket.newFail(MessageHeader.Code.illegalParameter, "已申请");
            }
            memberShop.setAddress(address);
        }
        memberShop.setContact(contact);
        memberShop.setContactPhone(contactPhone);
        memberShop.setContactIdCardBackImage(contactIdCardBackImage);
        memberShop.setContactIdCardFaceImage(contactIdCardFaceImage);
        memberShop.setVerifyStatus(0);
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
        rsMap.put("data", TimeTest.getNowDateFormat());

        return MessagePacket.newSuccess(rsMap, "updateMemberShop success!");
    }

    @ApiOperation(value = "删除会员店铺", notes = "删除会员店铺")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "sessionID", value = "登录标识", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "memberShopID", value = "会员店铺id", dataType = "String")})
    @RequestMapping(value = "/removeMemberShop")
    public MessagePacket removeMemberShop(HttpServletRequest request) {
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
        if (StringUtils.isEmpty(memberShopID)) {
            return MessagePacket.newFail(MessageHeader.Code.memberShopIDIsNull, "memberShopID不能为空");
        }

        MemberShop memberShop = memberShopService.findById(memberShopID);

        if (memberShop == null) {
            return MessagePacket.newFail(MessageHeader.Code.memberShopIDIsError, "memberShopID不正确");
        }

        memberShopService.del(memberShop);

        String description = String.format("%s删除会员店铺", member.getName());

        UserAgent userAgent = UserAgentUtil.getUserAgent(request.getHeader("user-agent"));
        MemberLogRequestBase base = MemberLogRequestBase.BALANCE()
                .sessionID(sessionID)
                .description(description)
                .userAgent(userAgent == null ? null : userAgent.getBrowserType())
                .operateType(Memberlog.MemberOperateType.REMOVEMEMBERSHOP.getOname())
                .build();

        sendMessageService.sendMemberLogMessage(JacksonHelper.toJson(base));

        Map<String, Object> rsMap = Maps.newHashMap();
        rsMap.put("data", TimeTest.getNowDateFormat());

        return MessagePacket.newSuccess(rsMap, "removeMemberShop success!");
    }

    @ApiOperation(value = "获取会员店铺列表", notes = "获取会员店铺列表")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "sessionID", value = "登录标识", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "currentPage", value = "分页，页码从1开始", dataType = "int"),
            @ApiImplicitParam(paramType = "query", name = "pageNumber", value = "每一页大小", dataType = "int")})
    @RequestMapping(value = "/getMemberShopList")
    public MessagePacket getMemberShopList(HttpServletRequest request) {
        String sessionID = request.getParameter("sessionID");
        String verifyStatus = request.getParameter("verifyStatus");
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
        paramMap.put("memberID", member.getId());
        if (StringUtils.isNotBlank(verifyStatus)) {
            paramMap.put("verifyStatus", Integer.parseInt(verifyStatus));
        }
        paramMap.put("isValid", Constants.ISVALID_YES);
        paramMap.put("isLock", Constants.ISLOCK_NO);
        List<Sort.Order> orders = new ArrayList<Sort.Order>();
        orders.add(new Sort.Order(Sort.Direction.DESC, "createdTime"));

        Object currentPage = request.getParameter("currentPage");
        Object pageNumber = request.getParameter("pageNumber");

        TPage page = ApiPageUtil.makePage(currentPage, pageNumber);

        Pageable pageable = new PageRequest(page.getStart(), page.getPageSize(), new Sort(orders));

        Page<MemberShop> rs = memberShopService.list(paramMap, pageable);

        List<MemberShopListDto> list = null;
        if (rs != null && rs.getSize() != 0) {
            list = BeanMapper.mapList(rs.getContent(), MemberShopListDto.class);
            page.setTotalSize((int) rs.getTotalElements());
        }

        String description = String.format("%s获取会员店铺列表", member.getName());

        UserAgent userAgent = UserAgentUtil.getUserAgent(request.getHeader("user-agent"));
        MemberLogRequestBase base = MemberLogRequestBase.BALANCE()
                .sessionID(sessionID)
                .description(description)
                .userAgent(userAgent == null ? null : userAgent.getBrowserType())
                .operateType(Memberlog.MemberOperateType.GETMEMBERSHOPLIST.getOname())
                .build();

        sendMessageService.sendMemberLogMessage(JacksonHelper.toJson(base));

        Map<String, Object> rsMap = Maps.newHashMap();
        MessagePage messagePage = new MessagePage(page, list);
        rsMap.put("data", messagePage);
        return MessagePacket.newSuccess(rsMap, "getMemberShopList success!");
    }

    @ApiOperation(value = "获取会员店铺详情", notes = "获取会员店铺详情")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "sessionID", value = "登录标识", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "memberShopID", value = "会员店铺id", dataType = "String")})
    @RequestMapping(value = "/getMemberShopDetail")
    public MessagePacket getMemberShopDetail(HttpServletRequest request) {
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

        if (StringUtils.isEmpty(memberShopID)) {
            return MessagePacket.newFail(MessageHeader.Code.memberShopIDIsNull, "memberShopID不能为空");
        }

        MemberShop memberShop = memberShopService.findById(memberShopID);
        if (memberShop == null) {
            return MessagePacket.newFail(MessageHeader.Code.memberShopIDIsError, "memberShopID不正确");
        }

        MemberShopDetailDto memberShopDetailDto = BeanMapper.map(memberShop, MemberShopDetailDto.class);

        String description = String.format("%s获取会员店铺信息", member.getName());

        UserAgent userAgent = UserAgentUtil.getUserAgent(request.getHeader("user-agent"));
        MemberLogRequestBase base = MemberLogRequestBase.BALANCE()
                .sessionID(sessionID)
                .description(description)
                .userAgent(userAgent == null ? null : userAgent.getBrowserType())
                .operateType(Memberlog.MemberOperateType.GETMEMBERSHOPDETAIL.getOname())
                .build();

        sendMessageService.sendMemberLogMessage(JacksonHelper.toJson(base));

        Map<String, Object> rsMap = Maps.newHashMap();
        rsMap.put("data", memberShopDetailDto);
        return MessagePacket.newSuccess(rsMap, "getMemberShopList success!");
    }

}
