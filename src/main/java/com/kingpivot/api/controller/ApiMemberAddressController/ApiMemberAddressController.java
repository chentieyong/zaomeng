package com.kingpivot.api.controller.ApiMemberAddressController;

import com.google.common.collect.Maps;
import com.kingpivot.api.dto.memberAddress.MemberAddressDetailDto;
import com.kingpivot.api.dto.memberAddress.MemberAddressListDto;
import com.kingpivot.base.config.RedisKey;
import com.kingpivot.base.config.UserAgent;
import com.kingpivot.base.member.model.Member;
import com.kingpivot.base.memberAddress.model.MemberAddress;
import com.kingpivot.base.memberAddress.service.MemberAddressService;
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
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequestMapping("/api")
@RestController
@Api(description = "会员地址管理接口")
public class ApiMemberAddressController extends ApiBaseController {

    @Resource
    private SendMessageService sendMessageService;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private MemberAddressService memberAddressService;

    @ApiOperation(value = "submitOneMemberAddress", notes = "提交一个会员地址")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "sessionID", value = "登录标识", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "name", value = "详细地址", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "shortName", value = "地址别名", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "shengID", value = "省id", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "shiID", value = "市id", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "xianID", value = "县id", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "addTye", value = "地址类别", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "memberID", value = "会员id", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "contactName", value = "联系人", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "phone", value = "联系电话", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "isDefault", value = "是否默认", dataType = "String"),
    })
    @RequestMapping(value = "/submitOneMemberAddress")
    public MessagePacket submitOneMemberAddress(HttpServletRequest request) {
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
        if (StringUtils.isNotBlank(name)) {
            return MessagePacket.newFail(MessageHeader.Code.nameIsNull, "详细地址不能为空");
        }
        String shortName = request.getParameter("shortName");
        if (StringUtils.isNotBlank(shortName)) {
            return MessagePacket.newFail(MessageHeader.Code.shortNameIsNull, "地址别名不能为空");
        }
        String shengID = request.getParameter("shengID");
        String shiID = request.getParameter("shiID");
        String xianID = request.getParameter("xianID");
        String addTye = request.getParameter("addTye");
        String contactName = request.getParameter("contactName");
        if (StringUtils.isEmpty(contactName)) {
            return MessagePacket.newFail(MessageHeader.Code.contactIsNull, "联系人不能为空");
        }
        String phone = request.getParameter("phone");
        if (StringUtils.isEmpty(phone)) {
            return MessagePacket.newFail(MessageHeader.Code.phoneIsNull, "联系电话不能为空");
        }
        String isDefault = request.getParameter("isDefault");

        MemberAddress memberAddress = new MemberAddress();
        memberAddress.setApplicationID(member.getApplicationID());
        memberAddress.setMemberID(member.getId());
        memberAddress.setName(name);
        memberAddress.setShortName(shortName);
        if (StringUtils.isNotBlank(shengID)) {
            memberAddress.setShengID(shengID);
        }
        if (StringUtils.isNotBlank(shiID)) {
            memberAddress.setShiID(shiID);
        }
        if (StringUtils.isNotBlank(xianID)) {
            memberAddress.setXianID(xianID);
        }
        if (StringUtils.isNotBlank(addTye)) {
            memberAddress.setAddTye(Integer.parseInt(addTye));
        }
        if (StringUtils.isNotBlank(isDefault)) {
            //设置其他地址为0
            if (isDefault.equals("1")) {
                memberAddressService.updateMemberAddressDefault(member.getId(), null);
            }
            memberAddress.setIsDefault(Integer.parseInt(isDefault));
        }
        memberAddressService.save(memberAddress);

        Map<String, Object> rsMap = Maps.newHashMap();
        rsMap.put("data", memberAddress.getId());

        String description = String.format("%s提交一个会员地址", member.getName());
        UserAgent userAgent = UserAgentUtil.getUserAgent(request.getHeader("user-agent"));
        MemberLogRequestBase base = MemberLogRequestBase.BALANCE()
                .sessionID(sessionID)
                .description(description)
                .userAgent(userAgent == null ? null : userAgent.getBrowserType())
                .operateType(Memberlog.MemberOperateType.SUBMITONEMEMBERADDRESS.getOname())
                .build();

        sendMessageService.sendMemberLogMessage(JacksonHelper.toJson(base));

        return MessagePacket.newSuccess(rsMap, "submitOneMemberAddress success!");
    }

    @ApiOperation(value = "updateOneMemberAddress", notes = "修改一个会员地址")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "sessionID", value = "登录标识", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "memberAddressID", value = "会员地址id", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "name", value = "详细地址", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "shortName", value = "地址别名", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "shengID", value = "省id", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "shiID", value = "市id", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "xianID", value = "县id", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "addTye", value = "地址类别", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "memberID", value = "会员id", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "contactName", value = "联系人", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "phone", value = "联系电话", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "isDefault", value = "是否默认", dataType = "String"),
    })
    @RequestMapping(value = "/updateOneMemberAddress")
    public MessagePacket updateOneMemberAddress(HttpServletRequest request) {
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

        String memberAddressID = request.getParameter("memberAddressID");
        if (StringUtils.isEmpty(memberAddressID)) {
            return MessagePacket.newFail(MessageHeader.Code.memberAddressIDIsNull, "memberAddressID不能为空");
        }

        MemberAddress memberAddress = memberAddressService.findById(memberAddressID);

        if (memberAddress == null) {
            return MessagePacket.newFail(MessageHeader.Code.memberAddressIDIsError, "memberAddressID不正确");
        }

        String name = request.getParameter("name");
        if (StringUtils.isNotBlank(name)) {
            return MessagePacket.newFail(MessageHeader.Code.nameIsNull, "详细地址不能为空");
        }
        String shortName = request.getParameter("shortName");
        if (StringUtils.isNotBlank(shortName)) {
            return MessagePacket.newFail(MessageHeader.Code.shortNameIsNull, "地址别名不能为空");
        }
        String shengID = request.getParameter("shengID");
        String shiID = request.getParameter("shiID");
        String xianID = request.getParameter("xianID");
        String addTye = request.getParameter("addTye");
        String contactName = request.getParameter("contactName");
        if (StringUtils.isEmpty(contactName)) {
            return MessagePacket.newFail(MessageHeader.Code.contactIsNull, "联系人不能为空");
        }
        String phone = request.getParameter("phone");
        if (StringUtils.isEmpty(phone)) {
            return MessagePacket.newFail(MessageHeader.Code.phoneIsNull, "联系电话不能为空");
        }
        String isDefault = request.getParameter("isDefault");

        memberAddress.setName(name);
        memberAddress.setShortName(shortName);
        if (StringUtils.isNotBlank(shengID)) {
            memberAddress.setShengID(shengID);
        }
        if (StringUtils.isNotBlank(shiID)) {
            memberAddress.setShiID(shiID);
        }
        if (StringUtils.isNotBlank(xianID)) {
            memberAddress.setXianID(xianID);
        }
        if (StringUtils.isNotBlank(addTye)) {
            memberAddress.setAddTye(Integer.parseInt(addTye));
        }
        if (StringUtils.isNotBlank(isDefault)) {
            //设置其他地址为0
            if (isDefault.equals("1")) {
                memberAddressService.updateMemberAddressDefault(member.getId(), memberAddress.getId());
            }
            memberAddress.setIsDefault(Integer.parseInt(isDefault));
        }
        memberAddressService.save(memberAddress);

        Map<String, Object> rsMap = Maps.newHashMap();
        rsMap.put("data", TimeTest.getTimeStr());

        String description = String.format("%s修改一个会员地址", member.getName());
        UserAgent userAgent = UserAgentUtil.getUserAgent(request.getHeader("user-agent"));
        MemberLogRequestBase base = MemberLogRequestBase.BALANCE()
                .sessionID(sessionID)
                .description(description)
                .userAgent(userAgent == null ? null : userAgent.getBrowserType())
                .operateType(Memberlog.MemberOperateType.UPDATEONEMEMBERADDRESS.getOname())
                .build();

        sendMessageService.sendMemberLogMessage(JacksonHelper.toJson(base));

        return MessagePacket.newSuccess(rsMap, "updateOneMemberAddress success!");
    }

    @ApiOperation(value = "removeOneMemberAddress", notes = "删除会员地址")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "sessionID", value = "登录标识", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "memberAddressID", value = "会员地址id", dataType = "String")})
    @RequestMapping(value = "/removeOneMemberAddress")
    public MessagePacket removeOneMemberAddress(HttpServletRequest request) {
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

        String memberAddressID = request.getParameter("memberAddressID");
        if (StringUtils.isEmpty(memberAddressID)) {
            return MessagePacket.newFail(MessageHeader.Code.memberAddressIDIsNull, "memberAddressID不能为空");
        }

        MemberAddress memberAddress = memberAddressService.findById(memberAddressID);

        if (memberAddress == null) {
            return MessagePacket.newFail(MessageHeader.Code.memberAddressIDIsError, "memberAddressID不正确");
        }
        memberAddressService.del(memberAddress);

        String description = String.format("%s删除会员地址", member.getName());
        UserAgent userAgent = UserAgentUtil.getUserAgent(request.getHeader("user-agent"));
        MemberLogRequestBase base = MemberLogRequestBase.BALANCE()
                .sessionID(sessionID)
                .description(description)
                .userAgent(userAgent == null ? null : userAgent.getBrowserType())
                .operateType(Memberlog.MemberOperateType.REMOVEONEMEMBERADDRESS.getOname())
                .build();

        sendMessageService.sendMemberLogMessage(JacksonHelper.toJson(base));

        Map<String, Object> rsMap = Maps.newHashMap();
        rsMap.put("data", TimeTest.getTimeStr());
        return MessagePacket.newSuccess(rsMap, "removeOneMemberAddress success!");
    }

    @ApiOperation(value = "getMemberAddressList", notes = "获取会员地址列表")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "sessionID", value = "登录标识", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "currentPage", value = "分页，页码从1开始", dataType = "int"),
            @ApiImplicitParam(paramType = "query", name = "pageNumber", value = "每一页大小", dataType = "int")})
    @RequestMapping(value = "/getMemberAddressList")
    public MessagePacket getMemberAddressList(HttpServletRequest request) {
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
        paramMap.put("memberID", member.getId());
        paramMap.put("isValid", Constants.ISVALID_YES);
        paramMap.put("isLock", Constants.ISLOCK_NO);

        List<Sort.Order> orders = new ArrayList<Sort.Order>();
        orders.add(new Sort.Order(Sort.Direction.DESC, "createdTime"));

        Object currentPage = request.getParameter("currentPage");
        Object pageNumber = request.getParameter("pageNumber");

        TPage page = ApiPageUtil.makePage(currentPage, pageNumber);

        Pageable pageable = new PageRequest(page.getOffset(), page.getPageSize(), new Sort(orders));

        Page<MemberAddress> rs = memberAddressService.list(paramMap, pageable);
        List<MemberAddressListDto> list = null;
        if (rs != null && rs.getSize() != 0) {
            list = BeanMapper.mapList(rs.getContent(), MemberAddressListDto.class);
            page.setTotalSize((int) rs.getTotalElements());
        }
        String description = String.format("%s获取会员地址列表", member.getName());
        UserAgent userAgent = UserAgentUtil.getUserAgent(request.getHeader("user-agent"));
        MemberLogRequestBase base = MemberLogRequestBase.BALANCE()
                .sessionID(sessionID)
                .description(description)
                .userAgent(userAgent == null ? null : userAgent.getBrowserType())
                .operateType(Memberlog.MemberOperateType.GETMEMBERADDRESSLIST.getOname())
                .build();

        sendMessageService.sendMemberLogMessage(JacksonHelper.toJson(base));
        Map<String, Object> rsMap = Maps.newHashMap();
        MessagePage messagePage = new MessagePage(page, list);
        rsMap.put("data", messagePage);
        return MessagePacket.newSuccess(rsMap, "getMemberAddressList success!");
    }

    @ApiOperation(value = "getMemberAddressDetail", notes = "获取会员地址详情")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "sessionID", value = "登录标识", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "memberAddressID", value = "会员地址id", dataType = "String"),
    })
    @RequestMapping(value = "/getMemberAddressDetail")
    public MessagePacket getMemberAddressDetail(HttpServletRequest request) {
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
        String memberAddressID = request.getParameter("memberAddressID");
        if (StringUtils.isEmpty(memberAddressID)) {
            return MessagePacket.newFail(MessageHeader.Code.memberAddressIDIsNull, "memberAddressID不能为空");
        }

        MemberAddress memberAddress = memberAddressService.findById(memberAddressID);

        if (memberAddress == null) {
            return MessagePacket.newFail(MessageHeader.Code.memberAddressIDIsError, "memberAddressID不正确");
        }

        MemberAddressDetailDto data = BeanMapper.map(memberAddress, MemberAddressDetailDto.class);

        String description = String.format("%s获取会员地址详情", member.getName());
        UserAgent userAgent = UserAgentUtil.getUserAgent(request.getHeader("user-agent"));
        MemberLogRequestBase base = MemberLogRequestBase.BALANCE()
                .sessionID(sessionID)
                .description(description)
                .userAgent(userAgent == null ? null : userAgent.getBrowserType())
                .operateType(Memberlog.MemberOperateType.GETMEMBERADDRESSDETAIL.getOname())
                .build();
        sendMessageService.sendMemberLogMessage(JacksonHelper.toJson(base));

        Map<String, Object> rsMap = Maps.newHashMap();
        rsMap.put("data", data);
        return MessagePacket.newSuccess(rsMap, "getMemberAddressDetail success!");
    }
}
