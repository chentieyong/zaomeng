package com.kingpivot.api.controller.ApiAttachmentController;

import com.google.common.collect.Maps;
import com.kingpivot.api.dto.attachment.ObjectAttachmentListDto;
import com.kingpivot.base.attachment.model.Attachment;
import com.kingpivot.base.attachment.service.AttachmentService;
import com.kingpivot.base.config.Config;
import com.kingpivot.common.jms.SendMessageService;
import com.kingpivot.common.jms.dto.attachment.AddAttachmentDto;
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
@Api(description = "附件管理接口")
public class ApiAttachmentController extends ApiBaseController {
    @Autowired
    private AttachmentService attachmentService;
    @Resource
    private SendMessageService sendMessageService;

    @ApiOperation(value = "获取对象附件列表", notes = "获取对象附件列表")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "currentPage", value = "分页，页码从1开始", dataType = "int"),
            @ApiImplicitParam(paramType = "query", name = "pageNumber", value = "每一页大小", dataType = "int"),
            @ApiImplicitParam(paramType = "query", name = "objectID", value = "对象id", dataType = "String")})
    @RequestMapping(value = "/getObjectAttachmentList")
    public MessagePacket getObjectAttachmentList(HttpServletRequest request) {
        String objectID = request.getParameter("objectID");
        String name = request.getParameter("name");
        String objectDefineID = request.getParameter("objectDefineID");

        if (StringUtils.isEmpty(objectID) && StringUtils.isEmpty(objectDefineID)) {
            return MessagePacket.newFail(MessageHeader.Code.objectIdIsNull, "objectID和objectDefineID不能同时为空不能为空");
        }

        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("isValid", Constants.ISVALID_YES);
        paramMap.put("isLock", Constants.ISLOCK_NO);
        if (StringUtils.isNotBlank(objectID)) {
            paramMap.put("objectID", objectID);
        }
        if (StringUtils.isNotBlank(name)) {
            paramMap.put("name", name);
        }
        if (StringUtils.isNotBlank(objectDefineID)) {
            paramMap.put("ObjectDefineID", objectDefineID);
        }

        List<Sort.Order> orders = new ArrayList<Sort.Order>();
        orders.add(new Sort.Order(Sort.Direction.ASC, "orderSeq"));

        Object currentPage = request.getParameter("currentPage");
        Object pageNumber = request.getParameter("pageNumber");

        TPage page = ApiPageUtil.makePage(currentPage, pageNumber);

        Pageable pageable = new PageRequest(page.getOffset(), page.getPageSize(), new Sort(orders));

        Page<Attachment> rs = attachmentService.list(paramMap, pageable);

        List<ObjectAttachmentListDto> list = null;
        if (rs != null && rs.getSize() != 0) {
            list = BeanMapper.mapList(rs.getContent(), ObjectAttachmentListDto.class);
            page.setTotalSize((int) rs.getTotalElements());
        }
        Map<String, Object> rsMap = Maps.newHashMap();
        MessagePage messagePage = new MessagePage(page, list);
        rsMap.put("data", messagePage);

        return MessagePacket.newSuccess(rsMap, "getObjectAttachmentList success!");
    }

    @ApiOperation(value = "上传对象附件", notes = "上传对象附件")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "objectID", value = "对象id", dataType = "int"),
            @ApiImplicitParam(paramType = "query", name = "objectDefineID", value = "对象定义id", dataType = "int"),
            @ApiImplicitParam(paramType = "query", name = "objectName", value = "对象名称", dataType = "int"),
            @ApiImplicitParam(paramType = "query", name = "urls", value = "url", dataType = "String")})
    @RequestMapping(value = "/addObjectAttachment")
    public MessagePacket addObjectAttachment(HttpServletRequest request) {
        String objectID = request.getParameter("objectID");
        String objectDefineID = request.getParameter("objectDefineID");
        String objectName = request.getParameter("objectName");
        String urls = request.getParameter("urls");
        String delUrls = request.getParameter("delUrls");
        String name = request.getParameter("name");

        //新增附件图
        sendMessageService.sendAddAttachmentMessage(JacksonHelper.toJson(new AddAttachmentDto.Builder()
                .name(name)
                .objectID(objectID)
                .objectDefineID(objectDefineID)
                .objectName(objectName)
                .fileType(1)
                .showType(1)
                .urls(urls)
                .delUrls(delUrls)
                .build()));
        Map<String, Object> rsMap = Maps.newHashMap();
        rsMap.put("data", TimeTest.getTimeStr());
        return MessagePacket.newSuccess(rsMap, "addObjectAttachment success!");
    }
}
