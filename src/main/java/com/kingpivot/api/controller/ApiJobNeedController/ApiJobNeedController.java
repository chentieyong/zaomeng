package com.kingpivot.api.controller.ApiJobNeedController;

import com.google.common.collect.Maps;
import com.kingpivot.api.dto.jobNeed.JobNeedDetailDto;
import com.kingpivot.api.dto.jobNeed.JobNeedListDto;
import com.kingpivot.base.config.Config;
import com.kingpivot.base.config.RedisKey;
import com.kingpivot.base.config.UserAgent;
import com.kingpivot.base.jobNeed.model.JobNeed;
import com.kingpivot.base.jobNeed.service.JobNeedService;
import com.kingpivot.base.member.model.Member;
import com.kingpivot.base.memberlog.model.Memberlog;
import com.kingpivot.base.support.MemberLogDTO;
import com.kingpivot.common.jms.SendMessageService;
import com.kingpivot.common.jms.dto.attachment.AddAttachmentDto;
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
@Api(description = "职位求职管理接口")
public class ApiJobNeedController extends ApiBaseController {
    @Resource
    private SendMessageService sendMessageService;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private JobNeedService jobNeedService;

    @ApiOperation(value = "submitOneJobNeed", notes = "提交一个职位求职")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "sessionID", value = "登录标识", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "name", value = "名称", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "description", value = "说明", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "beginDate", value = "开始日期", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "endDate", value = "结束日期", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "age", value = "年龄", dataType = "int"),
            @ApiImplicitParam(paramType = "query", name = "titleID", value = "性别", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "salaryCategoryID", value = "薪资范围", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "workYears", value = "工作年限", dataType = "int"),
            @ApiImplicitParam(paramType = "query", name = "educationCategoryID", value = "学历", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "address", value = "地址", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "urls", value = "附件图", dataType = "String"),
    })
    @RequestMapping(value = "/submitOneJobNeed")
    public MessagePacket submitOneJobNeed(HttpServletRequest request) {
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
        String description = request.getParameter("description");//说明
        String beginDate = request.getParameter("beginDate");//开始日期
        String endDate = request.getParameter("endDate");//结束日期
        String age = request.getParameter("age");
        String titleID = request.getParameter("titleID");//性别
        String salaryCategoryID = request.getParameter("salaryCategoryID");//薪资范围
        String workYears = request.getParameter("workYears");//工作年限
        String educationCategoryID = request.getParameter("educationCategoryID");//学历
        String address = request.getParameter("address");//地址
        String urls = request.getParameter("urls");//附件图

        JobNeed jobNeed = new JobNeed();
        jobNeed.setApplicationID(member.getApplicationID());
        jobNeed.setName(name);
        jobNeed.setDescription(description);
        jobNeed.setMemberID(member.getId());
        if (StringUtils.isEmpty(beginDate)) {
            jobNeed.setBeginDate(new Timestamp(System.currentTimeMillis()));
        } else {
            jobNeed.setBeginDate(TimeTest.strToDate(beginDate));
        }
        if (StringUtils.isEmpty(endDate)) {
            jobNeed.setEndDate(TimeTest.timeAddDay(new Timestamp(System.currentTimeMillis()), 7));
        } else {
            jobNeed.setEndDate(TimeTest.strToDate(endDate));
        }
        if (StringUtils.isNotBlank(age)) {
            jobNeed.setAge(Integer.parseInt(age));
        }
        if (StringUtils.isNotBlank(titleID)) {
            jobNeed.setTitleID(titleID);
        }
        if (StringUtils.isNotBlank(salaryCategoryID)) {
            jobNeed.setSalaryCategoryID(salaryCategoryID);
        }
        if (StringUtils.isNotBlank(workYears)) {
            jobNeed.setWorkYears(Integer.parseInt(workYears));
        }
        if (StringUtils.isNotBlank(educationCategoryID)) {
            jobNeed.setEducationCategoryID(educationCategoryID);
        }
        jobNeed.setAddress(address);
        jobNeedService.save(jobNeed);

        if (StringUtils.isNotBlank(urls)) {
            //新增附件图
            sendMessageService.sendAddAttachmentMessage(JacksonHelper.toJson(new AddAttachmentDto.Builder()
                    .objectID(jobNeed.getId())
                    .objectDefineID(Config.JOBNEED_OBJECTDEFINEID)
                    .objectName(jobNeed.getName())
                    .fileType(1)
                    .showType(1)
                    .urls(urls)
                    .build()));
        }

        String desc = String.format("%s提交一个职位求职", member.getName());
        UserAgent userAgent = UserAgentUtil.getUserAgent(request.getHeader("user-agent"));
        MemberLogRequestBase base = MemberLogRequestBase.BALANCE()
                .sessionID(sessionID)
                .description(desc)
                .userAgent(userAgent == null ? null : userAgent.getBrowserType())
                .operateType(Memberlog.MemberOperateType.SUBMITONEJOBNEED.getOname())
                .build();
        sendMessageService.sendMemberLogMessage(JacksonHelper.toJson(base));

        Map<String, Object> rsMap = Maps.newHashMap();
        rsMap.put("data", jobNeed.getId());

        return MessagePacket.newSuccess(rsMap, "submitOneJobNeed success!");
    }

    @ApiOperation(value = "getJobNeedList", notes = "获取职位求职列表")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "applicationID", value = "应用id", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "currentPage", value = "分页，页码从1开始", dataType = "int"),
            @ApiImplicitParam(paramType = "query", name = "pageNumber", value = "每一页大小", dataType = "int")})
    @RequestMapping(value = "/getJobNeedList")
    public MessagePacket getJobNeedList(HttpServletRequest request) {
        String applicationID = request.getParameter("applicationID");
        if (StringUtils.isEmpty(applicationID)) {
            return MessagePacket.newFail(MessageHeader.Code.applicationIdIsNull, "applicationID不能为空");
        }
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("applicationID", applicationID);
        paramMap.put("isValid", Constants.ISVALID_YES);
        paramMap.put("isLock", Constants.ISLOCK_NO);

        List<Sort.Order> orders = new ArrayList<Sort.Order>();
        orders.add(new Sort.Order(Sort.Direction.DESC, "createdTime"));

        Object currentPage = request.getParameter("currentPage");
        Object pageNumber = request.getParameter("pageNumber");

        TPage page = ApiPageUtil.makePage(currentPage, pageNumber);

        Pageable pageable = new PageRequest(page.getOffset(), page.getPageSize(), new Sort(orders));

        Page<JobNeed> rs = jobNeedService.list(paramMap, pageable);
        List<JobNeedListDto> list = null;
        if (rs != null && rs.getSize() != 0) {
            list = BeanMapper.mapList(rs.getContent(), JobNeedListDto.class);
            page.setTotalSize((int) rs.getTotalElements());
        }
        Map<String, Object> rsMap = Maps.newHashMap();
        MessagePage messagePage = new MessagePage(page, list);
        rsMap.put("data", messagePage);
        return MessagePacket.newSuccess(rsMap, "getJobNeedList success!");
    }

    @ApiOperation(value = "getJobNeedDetail", notes = "获取职位求职详情")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "jobNeedID", value = "职位求职id", dataType = "String")})
    @RequestMapping(value = "/getJobNeedDetail")
    public MessagePacket getJobNeedDetail(HttpServletRequest request) {
        String jobNeedID = request.getParameter("jobNeedID");
        if (StringUtils.isEmpty(jobNeedID)) {
            return MessagePacket.newFail(MessageHeader.Code.jobNeedIDIsNull, "jobNeedID不能为空");
        }
        JobNeed jobNeed = jobNeedService.findById(jobNeedID);
        if (jobNeed == null) {
            return MessagePacket.newFail(MessageHeader.Code.jobNeedIDIsError, "jobNeedID不正确");
        }
        JobNeedDetailDto data = BeanMapper.map(jobNeed, JobNeedDetailDto.class);
        Map<String, Object> rsMap = Maps.newHashMap();
        rsMap.put("data", data);
        return MessagePacket.newSuccess(rsMap, "getJobNeedDetail success!");
    }
}
