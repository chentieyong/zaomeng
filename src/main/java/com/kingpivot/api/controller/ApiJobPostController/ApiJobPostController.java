package com.kingpivot.api.controller.ApiJobPostController;

import com.google.common.collect.Maps;
import com.kingpivot.api.dto.jobPost.JobPostDetailDto;
import com.kingpivot.api.dto.jobPost.JobPostListDto;
import com.kingpivot.base.config.Config;
import com.kingpivot.base.config.RedisKey;
import com.kingpivot.base.config.UserAgent;
import com.kingpivot.base.jobPost.model.JobPost;
import com.kingpivot.base.jobPost.service.JobPostService;
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
@Api(description = "职位需求管理接口")
public class ApiJobPostController extends ApiBaseController {

    @Resource
    private SendMessageService sendMessageService;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private JobPostService jobPostService;

    @ApiOperation(value = "submitOneJobPost", notes = "提交一个职位需求")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "sessionID", value = "登录标识", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "name", value = "名称", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "description", value = "说明", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "beginDate", value = "开始日期", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "endDate", value = "结束日期", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "companyName", value = "公司名称", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "companyAddress", value = "公司地址", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "phone", value = "电话", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "salaryCategoryID", value = "薪资范围", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "needNumber", value = "个数", dataType = "int"),
            @ApiImplicitParam(paramType = "query", name = "urls", value = "附件图", dataType = "String"),
    })
    @RequestMapping(value = "/submitOneJobPost")
    public MessagePacket submitOneJobPost(HttpServletRequest request) {
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
        String days = request.getParameter("days");//发布在线天数
        String companyName = request.getParameter("companyName");
        String companyAddress = request.getParameter("companyAddress");
        String phone = request.getParameter("phone");
        String salaryCategoryID = request.getParameter("salaryCategoryID");//薪资范围
        String needNumber = request.getParameter("needNumber");
        String jobCategory = request.getParameter("jobCategory");//工作性质
        String educationCategoryID = request.getParameter("educationCategoryID");//学历
        String urls = request.getParameter("urls");//附件图

        JobPost jobPost = new JobPost();
        jobPost.setApplicationID(member.getApplicationID());
        jobPost.setName(name);
        jobPost.setDescription(description);
        jobPost.setMemberID(member.getId());
        if (StringUtils.isEmpty(beginDate)) {
            jobPost.setBeginDate(new Timestamp(System.currentTimeMillis()));
        } else {
            jobPost.setBeginDate(TimeTest.strToDate(beginDate));
        }
        if (StringUtils.isNotBlank(endDate)) {
            jobPost.setEndDate(TimeTest.strToDate(endDate));
        } else if (StringUtils.isNotBlank(days)) {
            jobPost.setEndDate(TimeTest.timeAddDay(new Timestamp(System.currentTimeMillis()), Integer.parseInt(days)));
        } else {
            jobPost.setEndDate(TimeTest.timeAddDay(new Timestamp(System.currentTimeMillis()), 7));
        }
        if (StringUtils.isNotBlank(companyName)) {
            jobPost.setCompanyName(companyName);
        }
        if (StringUtils.isNotBlank(companyAddress)) {
            jobPost.setCompanyAddress(companyAddress);
        }
        if (StringUtils.isNotBlank(phone)) {
            jobPost.setPhone(phone);
        }
        if (StringUtils.isNotBlank(salaryCategoryID)) {
            jobPost.setSalaryCategoryID(salaryCategoryID);
        }
        if (StringUtils.isNotBlank(needNumber)) {
            jobPost.setNeedNumber(Integer.parseInt(needNumber));
        }
        if (StringUtils.isNotBlank(educationCategoryID)) {
            jobPost.setEducationCategoryID(educationCategoryID);
        }
        jobPost.setJobCategory(jobCategory);
        jobPostService.save(jobPost);

        if (StringUtils.isNotBlank(urls)) {
            //新增附件图
            sendMessageService.sendAddAttachmentMessage(JacksonHelper.toJson(new AddAttachmentDto.Builder()
                    .objectID(jobPost.getId())
                    .objectDefineID(Config.JOBNEED_OBJECTDEFIPOST)
                    .objectName(jobPost.getName())
                    .fileType(1)
                    .showType(1)
                    .urls(urls)
                    .build()));
        }

        String desc = String.format("%s提交一个职位需求", member.getName());
        UserAgent userAgent = UserAgentUtil.getUserAgent(request.getHeader("user-agent"));
        MemberLogRequestBase base = MemberLogRequestBase.BALANCE()
                .sessionID(sessionID)
                .description(desc)
                .userAgent(userAgent == null ? null : userAgent.getBrowserType())
                .operateType(Memberlog.MemberOperateType.SUBMITONEJOBPOST.getOname())
                .build();
        sendMessageService.sendMemberLogMessage(JacksonHelper.toJson(base));

        Map<String, Object> rsMap = Maps.newHashMap();
        rsMap.put("data", jobPost.getId());

        return MessagePacket.newSuccess(rsMap, "submitOneJobPost success!");
    }

    @ApiOperation(value = "getJobPostList", notes = "获取职位需求列表")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "applicationID", value = "应用id", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "currentPage", value = "分页，页码从1开始", dataType = "int"),
            @ApiImplicitParam(paramType = "query", name = "pageNumber", value = "每一页大小", dataType = "int")})
    @RequestMapping(value = "/getJobPostList")
    public MessagePacket getJobPostList(HttpServletRequest request) {
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

        Page<JobPost> rs = jobPostService.list(paramMap, pageable);
        List<JobPostListDto> list = null;
        if (rs != null && rs.getSize() != 0) {
            list = BeanMapper.mapList(rs.getContent(), JobPostListDto.class);
            page.setTotalSize((int) rs.getTotalElements());
        }
        Map<String, Object> rsMap = Maps.newHashMap();
        MessagePage messagePage = new MessagePage(page, list);
        rsMap.put("data", messagePage);
        return MessagePacket.newSuccess(rsMap, "getJobPostList success!");
    }

    @ApiOperation(value = "getJobPostDetail", notes = "获取职位需求详情")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "jobPostID", value = "职位需求id", dataType = "String")})
    @RequestMapping(value = "/getJobPostDetail")
    public MessagePacket getJobNeedDetail(HttpServletRequest request) {
        String jobPostID = request.getParameter("jobPostID");
        if (StringUtils.isEmpty(jobPostID)) {
            return MessagePacket.newFail(MessageHeader.Code.jobPostIDIsNull, "jobPostID不能为空");
        }
        JobPost jobPost = jobPostService.findById(jobPostID);
        if (jobPost == null) {
            return MessagePacket.newFail(MessageHeader.Code.jobPostIDIsError, "jobPostID不正确");
        }
        JobPostDetailDto data = BeanMapper.map(jobPost, JobPostDetailDto.class);
        Map<String, Object> rsMap = Maps.newHashMap();
        rsMap.put("data", data);
        return MessagePacket.newSuccess(rsMap, "getJobPostDetail success!");
    }
}
