package com.kingpivot.api.controller.ApiHelpNeedController;

import com.google.common.collect.Maps;
import com.kingpivot.api.dto.helpNeed.HelpNeedDetailDto;
import com.kingpivot.api.dto.helpNeed.HelpNeedListDto;
import com.kingpivot.base.config.Config;
import com.kingpivot.base.config.RedisKey;
import com.kingpivot.base.config.UserAgent;
import com.kingpivot.base.helpNeed.model.HelpNeed;
import com.kingpivot.base.helpNeed.service.HelpNeedService;
import com.kingpivot.base.member.model.Member;
import com.kingpivot.base.memberlog.model.Memberlog;
import com.kingpivot.base.pointapplication.service.PointApplicationService;
import com.kingpivot.base.support.MemberLogDTO;
import com.kingpivot.common.KingBase;
import com.kingpivot.common.jms.SendMessageService;
import com.kingpivot.common.jms.dto.attachment.AddAttachmentDto;
import com.kingpivot.common.jms.dto.memberLog.MemberLogRequestBase;
import com.kingpivot.common.jms.dto.point.UsePointRequest;
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
@Api(description = "帮助需求管理接口")
public class ApiHelpNeedController extends ApiBaseController {

    @Resource
    private SendMessageService sendMessageService;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private HelpNeedService helpNeedService;
    @Autowired
    private KingBase kingBase;
    @Autowired
    private PointApplicationService pointApplicationService;

    @ApiOperation(value = "submitOneHelpNeed", notes = "提交一个帮助需求")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "sessionID", value = "登录标识", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "name", value = "名称", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "description", value = "说明", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "beginDate", value = "开始日期", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "endDate", value = "结束日期", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "contact", value = "联系人", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "contactPhone", value = "联系电话", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "address", value = "地址", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "urls", value = "附件图", dataType = "String"),
    })
    @RequestMapping(value = "/submitOneHelpNeed")
    public MessagePacket submitOneHelpNeed(HttpServletRequest request) {
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

        //积分是否足够
        if(!kingBase.pointLess(member, pointApplicationService.getNumberByAppIdAndPointName(
                member.getApplicationID(), Config.HELPNEED_POINT_USENAME))){
            return MessagePacket.newFail(MessageHeader.Code.pointNumberLess, "积分个数不足");
        }

        String name = request.getParameter("name");
        if (StringUtils.isEmpty(name)) {
            return MessagePacket.newFail(MessageHeader.Code.nameIsNull, "name不能为空");
        }
        String description = request.getParameter("description");//说明
        String beginDate = request.getParameter("beginDate");//开始日期
        String endDate = request.getParameter("endDate");//结束日期
        String days = request.getParameter("days");//发布在线天数
        String contact = request.getParameter("contact");
        String contactPhone = request.getParameter("contactPhone");
        String address = request.getParameter("address");//数量
        String urls = request.getParameter("urls");//附件图

        HelpNeed helpNeed = new HelpNeed();
        helpNeed.setApplicationID(member.getApplicationID());
        helpNeed.setName(name);
        helpNeed.setDescription(description);
        helpNeed.setMemberID(member.getId());
        if (StringUtils.isEmpty(beginDate)) {
            helpNeed.setBeginDate(new Timestamp(System.currentTimeMillis()));
        } else {
            helpNeed.setBeginDate(TimeTest.strToDate(beginDate));
        }
        if (StringUtils.isNotBlank(endDate)) {
            helpNeed.setEndDate(TimeTest.strToDate(endDate));
        } else if (StringUtils.isNotBlank(days)) {
            helpNeed.setEndDate(TimeTest.timeAddDay(new Timestamp(System.currentTimeMillis()), Integer.parseInt(days)));
        } else {
            helpNeed.setEndDate(TimeTest.timeAddDay(new Timestamp(System.currentTimeMillis()), 7));
        }
        if (StringUtils.isNotBlank(contact)) {
            helpNeed.setContact(contact);
        }
        if (StringUtils.isNotBlank(contactPhone)) {
            helpNeed.setContactPhone(contactPhone);
        }
        if (StringUtils.isNotBlank(address)) {
            helpNeed.setAddress(address);
        }
        helpNeedService.save(helpNeed);

        if (StringUtils.isNotBlank(urls)) {
            //新增附件图
            sendMessageService.sendAddAttachmentMessage(JacksonHelper.toJson(new AddAttachmentDto.Builder()
                    .objectID(helpNeed.getId())
                    .objectDefineID(Config.HELPNEED_OBJECTDEFIPOST)
                    .objectName(helpNeed.getName())
                    .fileType(1)
                    .showType(1)
                    .urls(urls)
                    .build()));
        }

        //发送消耗积分
        sendMessageService.sendUsePointMessage(JacksonHelper.toJson(new UsePointRequest.Builder()
                .objectDefineID(Config.HELPNEED_OBJECTDEFIPOST)
                .memberID(member.getId())
                .pointName(Config.HELPNEED_POINT_USENAME)
                .build()));

        String desc = String.format("%s提交一个帮助需求", member.getName());
        UserAgent userAgent = UserAgentUtil.getUserAgent(request.getHeader("user-agent"));
        MemberLogRequestBase base = MemberLogRequestBase.BALANCE()
                .sessionID(sessionID)
                .description(desc)
                .userAgent(userAgent == null ? null : userAgent.getBrowserType())
                .operateType(Memberlog.MemberOperateType.SUBMITONEHELPNEED.getOname())
                .build();
        sendMessageService.sendMemberLogMessage(JacksonHelper.toJson(base));

        Map<String, Object> rsMap = Maps.newHashMap();
        rsMap.put("data", helpNeed.getId());

        return MessagePacket.newSuccess(rsMap, "submitOneHelpNeed success!");
    }

    @ApiOperation(value = "getHelpNeedList", notes = "获取帮助需求列表")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "applicationID", value = "应用id", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "currentPage", value = "分页，页码从1开始", dataType = "int"),
            @ApiImplicitParam(paramType = "query", name = "pageNumber", value = "每一页大小", dataType = "int")})
    @RequestMapping(value = "/getHelpNeedList")
    public MessagePacket getHelpNeedList(HttpServletRequest request) {
        String applicationID = request.getParameter("applicationID");
        if (StringUtils.isEmpty(applicationID)) {
            return MessagePacket.newFail(MessageHeader.Code.applicationIdIsNull, "applicationID不能为空");
        }
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("applicationID", applicationID);
        paramMap.put("endDate:gte", new Timestamp(System.currentTimeMillis()));
        paramMap.put("isValid", Constants.ISVALID_YES);
        paramMap.put("isLock", Constants.ISLOCK_NO);

        List<Sort.Order> orders = new ArrayList<Sort.Order>();
        orders.add(new Sort.Order(Sort.Direction.DESC, "createdTime"));

        Object currentPage = request.getParameter("currentPage");
        Object pageNumber = request.getParameter("pageNumber");

        TPage page = ApiPageUtil.makePage(currentPage, pageNumber);

        Pageable pageable = new PageRequest(page.getOffset(), page.getPageSize(), new Sort(orders));

        Page<HelpNeed> rs = helpNeedService.list(paramMap, pageable);
        List<HelpNeedListDto> list = null;
        if (rs != null && rs.getSize() != 0) {
            list = BeanMapper.mapList(rs.getContent(), HelpNeedListDto.class);
            page.setTotalSize((int) rs.getTotalElements());
        }
        Map<String, Object> rsMap = Maps.newHashMap();
        MessagePage messagePage = new MessagePage(page, list);
        rsMap.put("data", messagePage);
        return MessagePacket.newSuccess(rsMap, "getHelpNeedList success!");
    }

    @ApiOperation(value = "getHelpNeedDetail", notes = "获取帮助需求详情")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "helpNeedID", value = "帮助需求id", dataType = "String")})
    @RequestMapping(value = "/getHelpNeedDetail")
    public MessagePacket getHelpNeedDetail(HttpServletRequest request) {
        String helpNeedID = request.getParameter("helpNeedID");
        if (StringUtils.isEmpty(helpNeedID)) {
            return MessagePacket.newFail(MessageHeader.Code.helpNeedIDIsNull, "helpNeedID不能为空");
        }
        HelpNeed helpNeed = helpNeedService.findById(helpNeedID);
        if (helpNeed == null) {
            return MessagePacket.newFail(MessageHeader.Code.helpNeedIDIsError, "helpNeedID不正确");
        }
        HelpNeedDetailDto data = BeanMapper.map(helpNeed, HelpNeedDetailDto.class);
        Map<String, Object> rsMap = Maps.newHashMap();
        rsMap.put("data", data);
        return MessagePacket.newSuccess(rsMap, "getHelpNeedDetail success!");
    }
}
