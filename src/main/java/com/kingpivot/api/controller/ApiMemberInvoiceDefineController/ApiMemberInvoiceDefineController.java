package com.kingpivot.api.controller.ApiMemberInvoiceDefineController;

import com.google.common.collect.Maps;
import com.kingpivot.api.dto.memberInvoiceDefine.MemberInvoiceDefineDetailDto;
import com.kingpivot.api.dto.memberInvoiceDefine.MemberInvoiceDefineListDto;
import com.kingpivot.base.config.RedisKey;
import com.kingpivot.base.config.UserAgent;
import com.kingpivot.base.member.model.Member;
import com.kingpivot.base.memberInvoiceDefine.model.MemberInvoiceDefine;
import com.kingpivot.base.memberInvoiceDefine.service.MemberInvoiceDefineService;
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
@Api(description = "会员发票管理接口")
public class ApiMemberInvoiceDefineController extends ApiBaseController {

    @Resource
    private SendMessageService sendMessageService;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private MemberInvoiceDefineService memberInvoiceDefineService;

    @ApiOperation(value = "submitOneMemberInvoiceDefine", notes = "提交一个会员发票")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "sessionID", value = "登录标识", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "name", value = "发票名称", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "shortName", value = "发票别名", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "invoiceType", value = "发票类型", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "taxType", value = "发票税种", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "taxCode", value = "纳税人识别号", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "address", value = "地址", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "phone", value = "联系电话", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "bankName", value = "开户银行", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "bankAccount", value = "银行账户", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "isDefault", value = "是否默认", dataType = "String"),
    })
    @RequestMapping(value = "/submitOneMemberInvoiceDefine")
    public MessagePacket submitOneMemberInvoiceDefine(HttpServletRequest request) {
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
            return MessagePacket.newFail(MessageHeader.Code.nameIsNull, "详细地址不能为空");
        }
        String shortName = request.getParameter("shortName");
        if (StringUtils.isEmpty(shortName)) {
            return MessagePacket.newFail(MessageHeader.Code.shortNameIsNull, "地址别名不能为空");
        }
        String invoiceType = request.getParameter("invoiceType");//发票类型 1个人2单位
        if (StringUtils.isEmpty(invoiceType)) {
            return MessagePacket.newFail(MessageHeader.Code.invoiceTypeIsNull, "发票类型不能为空");
        }
        String taxType = request.getParameter("taxType");//发票税种 1增值税普通发票2增值税专用发票
        if (StringUtils.isEmpty(taxType)) {
            return MessagePacket.newFail(MessageHeader.Code.taxTypeIsNull, "发票税种不能为空");
        }
        String taxCode = request.getParameter("taxCode");//纳税人识别号
        if (StringUtils.isEmpty(taxCode)) {
            return MessagePacket.newFail(MessageHeader.Code.taxCodeIsNull, "纳税人识别号不能为空");
        }
        String address = request.getParameter("address");//地址
        String phone = request.getParameter("phone");//联系电话
        String bankName = request.getParameter("bankName");//开户银行
        String bankAccount = request.getParameter("bankAccount");//银行账户
        String isDefault = request.getParameter("isDefault");

        MemberInvoiceDefine memberInvoiceDefine = new MemberInvoiceDefine();
        memberInvoiceDefine.setApplicationID(member.getApplicationID());
        memberInvoiceDefine.setMemberID(member.getId());
        memberInvoiceDefine.setName(name);
        memberInvoiceDefine.setShortName(shortName);
        memberInvoiceDefine.setInvoiceType(Integer.parseInt(invoiceType));
        memberInvoiceDefine.setTaxType(Integer.parseInt(taxType));
        memberInvoiceDefine.setTaxCode(taxCode);
        memberInvoiceDefine.setOrderSeq(memberInvoiceDefineService.getMaxOrderSeq(member.getId()));
        if (StringUtils.isNotBlank(address)) {
            memberInvoiceDefine.setAddress(address);
        }
        if (StringUtils.isNotBlank(phone)) {
            memberInvoiceDefine.setPhone(phone);
        }
        if (StringUtils.isNotBlank(bankName)) {
            memberInvoiceDefine.setBankName(bankName);
        }
        if (StringUtils.isNotBlank(bankAccount)) {
            memberInvoiceDefine.setBankAccount(bankAccount);
        }
        if (StringUtils.isNotBlank(isDefault)) {
            //设置其他地址为0
            if (isDefault.equals("1")) {
                memberInvoiceDefineService.updateMemberInvoiceDefineDefault(member.getId(), null);
            }
            memberInvoiceDefine.setIsDefault(Integer.parseInt(isDefault));
        }
        memberInvoiceDefineService.save(memberInvoiceDefine);

        Map<String, Object> rsMap = Maps.newHashMap();
        rsMap.put("data", memberInvoiceDefine.getId());

        String description = String.format("%s提交一个会员发票", member.getName());
        UserAgent userAgent = UserAgentUtil.getUserAgent(request.getHeader("user-agent"));
        MemberLogRequestBase base = MemberLogRequestBase.BALANCE()
                .sessionID(sessionID)
                .description(description)
                .userAgent(userAgent == null ? null : userAgent.getBrowserType())
                .operateType(Memberlog.MemberOperateType.SUBMITONEMEMBERINVOICEDEFINE.getOname())
                .build();

        sendMessageService.sendMemberLogMessage(JacksonHelper.toJson(base));

        return MessagePacket.newSuccess(rsMap, "submitOneMemberInvoiceDefine success!");
    }

    @ApiOperation(value = "updateOneMemberInvoiceDefine", notes = "修改一个会员发票")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "sessionID", value = "登录标识", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "memberInvoiceDefineID", value = "会员发票", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "name", value = "发票名称", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "shortName", value = "发票别名", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "invoiceType", value = "发票类型", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "taxType", value = "发票税种", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "taxCode", value = "纳税人识别号", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "address", value = "地址", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "phone", value = "联系电话", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "bankName", value = "开户银行", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "bankAccount", value = "银行账户", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "isDefault", value = "是否默认", dataType = "String"),
    })
    @RequestMapping(value = "/updateOneMemberInvoiceDefine")
    public MessagePacket updateOneMemberInvoiceDefine(HttpServletRequest request) {
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

        String memberInvoiceDefineID = request.getParameter("memberInvoiceDefineID");
        if (StringUtils.isEmpty(memberInvoiceDefineID)) {
            return MessagePacket.newFail(MessageHeader.Code.memberInvoiceDefineIDIsNull, "memberInvoiceDefineID不能为空");
        }

        MemberInvoiceDefine memberInvoiceDefine = memberInvoiceDefineService.findById(memberInvoiceDefineID);

        if (memberInvoiceDefine == null) {
            return MessagePacket.newFail(MessageHeader.Code.memberInvoiceDefineIDIsError, "memberInvoiceDefineID不正确");
        }

        String name = request.getParameter("name");
        if (StringUtils.isEmpty(name)) {
            return MessagePacket.newFail(MessageHeader.Code.nameIsNull, "详细地址不能为空");
        }
        String shortName = request.getParameter("shortName");
        if (StringUtils.isEmpty(shortName)) {
            return MessagePacket.newFail(MessageHeader.Code.shortNameIsNull, "地址别名不能为空");
        }
        String invoiceType = request.getParameter("invoiceType");//发票类型 1个人2单位
        if (StringUtils.isEmpty(invoiceType)) {
            return MessagePacket.newFail(MessageHeader.Code.invoiceTypeIsNull, "发票类型不能为空");
        }
        String taxType = request.getParameter("taxType");//发票税种 1增值税普通发票2增值税专用发票
        if (StringUtils.isEmpty(taxType)) {
            return MessagePacket.newFail(MessageHeader.Code.taxTypeIsNull, "发票税种不能为空");
        }
        String taxCode = request.getParameter("taxCode");//纳税人识别号
        if (StringUtils.isEmpty(taxCode)) {
            return MessagePacket.newFail(MessageHeader.Code.taxCodeIsNull, "纳税人识别号不能为空");
        }
        String address = request.getParameter("address");//地址
        String phone = request.getParameter("phone");//联系电话
        String bankName = request.getParameter("bankName");//开户银行
        String bankAccount = request.getParameter("bankAccount");//银行账户
        String isDefault = request.getParameter("isDefault");

        memberInvoiceDefine.setName(name);
        memberInvoiceDefine.setShortName(shortName);
        memberInvoiceDefine.setInvoiceType(Integer.parseInt(invoiceType));
        memberInvoiceDefine.setTaxType(Integer.parseInt(taxType));
        memberInvoiceDefine.setTaxCode(taxCode);
        memberInvoiceDefine.setOrderSeq(memberInvoiceDefineService.getMaxOrderSeq(member.getId()));
        if (StringUtils.isNotBlank(address)) {
            memberInvoiceDefine.setAddress(address);
        }
        if (StringUtils.isNotBlank(phone)) {
            memberInvoiceDefine.setPhone(phone);
        }
        if (StringUtils.isNotBlank(bankName)) {
            memberInvoiceDefine.setBankName(bankName);
        }
        if (StringUtils.isNotBlank(bankAccount)) {
            memberInvoiceDefine.setBankAccount(bankAccount);
        }
        if (StringUtils.isNotBlank(isDefault)) {
            //设置其他发票为0
            if (isDefault.equals("1")) {
                memberInvoiceDefineService.updateMemberInvoiceDefineDefault(member.getId(), memberInvoiceDefineID);
            }
            memberInvoiceDefine.setIsDefault(Integer.parseInt(isDefault));
        }
        memberInvoiceDefineService.save(memberInvoiceDefine);

        Map<String, Object> rsMap = Maps.newHashMap();
        rsMap.put("data", TimeTest.getTimeStr());

        String description = String.format("%s修改一个会员发票", member.getName());
        UserAgent userAgent = UserAgentUtil.getUserAgent(request.getHeader("user-agent"));
        MemberLogRequestBase base = MemberLogRequestBase.BALANCE()
                .sessionID(sessionID)
                .description(description)
                .userAgent(userAgent == null ? null : userAgent.getBrowserType())
                .operateType(Memberlog.MemberOperateType.UPDATEONEMEMBERINVOICEDEFINE.getOname())
                .build();
        sendMessageService.sendMemberLogMessage(JacksonHelper.toJson(base));

        return MessagePacket.newSuccess(rsMap, "updateOneMemberInvoiceDefine success!");
    }

    @ApiOperation(value = "removeOneMemberInvoiceDefine", notes = "删除会员发票")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "sessionID", value = "登录标识", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "memberInvoiceDefineID", value = "会员发票id", dataType = "String")})
    @RequestMapping(value = "/removeOneMemberInvoiceDefine")
    public MessagePacket removeOneMemberInvoiceDefine(HttpServletRequest request) {
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

        String memberInvoiceDefineID = request.getParameter("memberInvoiceDefineID");
        if (StringUtils.isEmpty(memberInvoiceDefineID)) {
            return MessagePacket.newFail(MessageHeader.Code.memberInvoiceDefineIDIsNull, "memberInvoiceDefineID不能为空");
        }

        MemberInvoiceDefine memberInvoiceDefine = memberInvoiceDefineService.findById(memberInvoiceDefineID);

        if (memberInvoiceDefine == null) {
            return MessagePacket.newFail(MessageHeader.Code.memberInvoiceDefineIDIsError, "memberInvoiceDefineID不正确");
        }
        memberInvoiceDefineService.del(memberInvoiceDefine);

        String description = String.format("%s删除会员发票", member.getName());
        UserAgent userAgent = UserAgentUtil.getUserAgent(request.getHeader("user-agent"));
        MemberLogRequestBase base = MemberLogRequestBase.BALANCE()
                .sessionID(sessionID)
                .description(description)
                .userAgent(userAgent == null ? null : userAgent.getBrowserType())
                .operateType(Memberlog.MemberOperateType.REMOVEONEMEMBERINVOICEDEFINE.getOname())
                .build();

        sendMessageService.sendMemberLogMessage(JacksonHelper.toJson(base));

        Map<String, Object> rsMap = Maps.newHashMap();
        rsMap.put("data", TimeTest.getTimeStr());
        return MessagePacket.newSuccess(rsMap, "removeOneMemberInvoiceDefine success!");
    }

    @ApiOperation(value = "getMemberInvoiceDefineList", notes = "获取会员发票列表")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "sessionID", value = "登录标识", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "currentPage", value = "分页，页码从1开始", dataType = "int"),
            @ApiImplicitParam(paramType = "query", name = "pageNumber", value = "每一页大小", dataType = "int")})
    @RequestMapping(value = "/getMemberInvoiceDefineList")
    public MessagePacket getMemberInvoiceDefineList(HttpServletRequest request) {
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

        Page<MemberInvoiceDefine> rs = memberInvoiceDefineService.list(paramMap, pageable);
        List<MemberInvoiceDefineListDto> list = null;
        if (rs != null && rs.getSize() != 0) {
            list = BeanMapper.mapList(rs.getContent(), MemberInvoiceDefineListDto.class);
            page.setTotalSize((int) rs.getTotalElements());
        }
        String description = String.format("%s获取会员发票列表", member.getName());
        UserAgent userAgent = UserAgentUtil.getUserAgent(request.getHeader("user-agent"));
        MemberLogRequestBase base = MemberLogRequestBase.BALANCE()
                .sessionID(sessionID)
                .description(description)
                .userAgent(userAgent == null ? null : userAgent.getBrowserType())
                .operateType(Memberlog.MemberOperateType.GETMEMBERINVOICEDEFINELIST.getOname())
                .build();
        sendMessageService.sendMemberLogMessage(JacksonHelper.toJson(base));
        Map<String, Object> rsMap = Maps.newHashMap();
        MessagePage messagePage = new MessagePage(page, list);
        rsMap.put("data", messagePage);
        return MessagePacket.newSuccess(rsMap, "getMemberInvoiceDefineList success!");
    }

    @ApiOperation(value = "getMemberInvoiceDefineDetail", notes = "获取会员发票详情")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "sessionID", value = "登录标识", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "memberInvoiceDefineID", value = "会员发票id", dataType = "String"),
    })
    @RequestMapping(value = "/getMemberInvoiceDefineDetail")
    public MessagePacket getMemberInvoiceDefineDetail(HttpServletRequest request) {
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
        String memberInvoiceDefineID = request.getParameter("memberInvoiceDefineID");
        if (StringUtils.isEmpty(memberInvoiceDefineID)) {
            return MessagePacket.newFail(MessageHeader.Code.memberInvoiceDefineIDIsNull, "memberInvoiceDefineID不能为空");
        }

        MemberInvoiceDefine memberInvoiceDefine = memberInvoiceDefineService.findById(memberInvoiceDefineID);

        if (memberInvoiceDefine == null) {
            return MessagePacket.newFail(MessageHeader.Code.memberInvoiceDefineIDIsError, "memberInvoiceDefineID不正确");
        }

        MemberInvoiceDefineDetailDto data = BeanMapper.map(memberInvoiceDefine, MemberInvoiceDefineDetailDto.class);

        String description = String.format("%s获取会员发票详情", member.getName());
        UserAgent userAgent = UserAgentUtil.getUserAgent(request.getHeader("user-agent"));
        MemberLogRequestBase base = MemberLogRequestBase.BALANCE()
                .sessionID(sessionID)
                .description(description)
                .userAgent(userAgent == null ? null : userAgent.getBrowserType())
                .operateType(Memberlog.MemberOperateType.GETMEMBERINVOICEDEFINEDETAIL.getOname())
                .build();
        sendMessageService.sendMemberLogMessage(JacksonHelper.toJson(base));

        Map<String, Object> rsMap = Maps.newHashMap();
        rsMap.put("data", data);
        return MessagePacket.newSuccess(rsMap, "getMemberInvoiceDefineDetail success!");
    }
}
