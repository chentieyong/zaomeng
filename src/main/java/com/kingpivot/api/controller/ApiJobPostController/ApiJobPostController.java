package com.kingpivot.api.controller.ApiJobPostController;

import com.google.common.collect.Maps;
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
import java.sql.Timestamp;
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
        String companyName = request.getParameter("companyName");
        String companyAddress = request.getParameter("companyAddress");
        String phone = request.getParameter("phone");
        String salaryCategoryID = request.getParameter("salaryCategoryID");//薪资范围
        String needNumber = request.getParameter("needNumber");
        String educationCategoryID = request.getParameter("educationCategoryID");//学历
        String urls = request.getParameter("urls");//附件图

        JobPost jobNeed = new JobPost();
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
        if (StringUtils.isNotBlank(companyName)) {
            jobNeed.setCompanyName(companyName);
        }
        if (StringUtils.isNotBlank(companyAddress)) {
            jobNeed.setCompanyAddress(companyAddress);
        }
        if (StringUtils.isNotBlank(phone)) {
            jobNeed.setPhone(phone);
        }
        if (StringUtils.isNotBlank(salaryCategoryID)) {
            jobNeed.setSalaryCategoryID(salaryCategoryID);
        }
        if (StringUtils.isNotBlank(needNumber)) {
            jobNeed.setNeedNumber(Integer.parseInt(needNumber));
        }
        if (StringUtils.isNotBlank(educationCategoryID)) {
            jobNeed.setEducationCategoryID(educationCategoryID);
        }
        jobPostService.save(jobNeed);

        if (StringUtils.isNotBlank(urls)) {
            //新增附件图
            sendMessageService.sendAddAttachmentMessage(JacksonHelper.toJson(new AddAttachmentDto.Builder()
                    .objectID(jobNeed.getId())
                    .objectDefineID(Config.JOBNEED_OBJECTDEFIPOST)
                    .objectName(jobNeed.getName())
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
        rsMap.put("data", jobNeed.getId());

        return MessagePacket.newSuccess(rsMap, "submitOneJobPost success!");
    }
}
