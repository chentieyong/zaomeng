package com.kingpivot.api.controller.ApiFeedController;

import com.google.common.collect.Maps;
import com.kingpivot.base.feed.model.Feed;
import com.kingpivot.base.feed.service.FeedService;
import com.kingpivot.protocol.ApiBaseController;
import com.kingpivot.protocol.MessageHeader;
import com.kingpivot.protocol.MessagePacket;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RequestMapping("/api")
@RestController
@Api(description = "意见反馈管理接口")
public class ApiFeedController extends ApiBaseController {
    @Autowired
    private FeedService feedService;

    @ApiOperation(value = "提交一个意见反馈", notes = "提交一个意见反馈")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "title", value = "标题", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "sex", value = "性别", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "companyName", value = "公司名称", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "industryName", value = "行业名称", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "departmentName", value = "部门名称", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "jobName", value = "职位名称", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "email", value = "邮箱", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "contact", value = "联系人", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "contacts", value = "联系电话", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "contant", value = "内容", dataType = "String"),
    })
    @RequestMapping(value = "/submitOneFeed")
    public MessagePacket submitOneFeed(HttpServletRequest request) {
        String applicationID = request.getParameter("applicationID");
        if (StringUtils.isEmpty(applicationID)) {
            return MessagePacket.newFail(MessageHeader.Code.unauth, "applicationID不能为空");
        }
        String contant = request.getParameter("contant");
        if (StringUtils.isEmpty(contant)) {
            return MessagePacket.newFail(MessageHeader.Code.contantIsNull, "contant不能为空");
        }
        String contact = request.getParameter("contact");
        if (StringUtils.isEmpty(contact)) {
            return MessagePacket.newFail(MessageHeader.Code.contactIsNull, "contact不能为空");
        }
        String contacts = request.getParameter("contacts");
        if (StringUtils.isEmpty(contacts)) {
            return MessagePacket.newFail(MessageHeader.Code.contactsIsNull, "contacts不能为空");
        }

        String title = request.getParameter("title");
        String sex = request.getParameter("sex");
        String companyName = request.getParameter("companyName");
        String industryName = request.getParameter("industryName");
        String departmentName = request.getParameter("departmentName");
        String jobName = request.getParameter("jobName");
        String email = request.getParameter("email");

        Feed feed = new Feed();
        feed.setApplicationID(applicationID);
        feed.setContant(contant);
        if (StringUtils.isNotBlank(contact)) {
            feed.setContact(contact);
        }
        if (StringUtils.isNotBlank(title)) {
            feed.setTitle(title);
        }
        if (StringUtils.isNotBlank(sex)) {
            feed.setSex(sex);
        }
        if (StringUtils.isNotBlank(companyName)) {
            feed.setCompanyName(companyName);
        }
        if (StringUtils.isNotBlank(industryName)) {
            feed.setIndustryName(industryName);
        }
        if (StringUtils.isNotBlank(departmentName)) {
            feed.setDepartmentName(departmentName);
        }
        if (StringUtils.isNotBlank(jobName)) {
            feed.setJobName(jobName);
        }
        if (StringUtils.isNotBlank(email)) {
            feed.setEmail(email);
        }
        if (StringUtils.isNotBlank(contacts)) {
            feed.setContacts(contacts);
        }
        feedService.save(feed);

        Map<String, Object> rsMap = Maps.newHashMap();
        rsMap.put("data", feed.getId());

        return MessagePacket.newSuccess(rsMap, "submitOneFeed success!");
    }
}
