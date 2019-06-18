package com.kingpivot.api.controller.ApiFocusController;

import com.google.common.collect.Maps;
import com.kingpivot.api.dto.focus.FocusPictureListDto;
import com.kingpivot.base.focus.model.FocusPicture;
import com.kingpivot.base.focus.service.FocusPictureService;
import com.kingpivot.common.util.Constants;
import com.kingpivot.common.utils.*;
import io.swagger.annotations.Api;
import org.springframework.data.domain.Page;
import com.kingpivot.protocol.ApiBaseController;
import com.kingpivot.protocol.MessageHeader;
import com.kingpivot.protocol.MessagePacket;
import com.kingpivot.protocol.MessagePage;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequestMapping("/api")
@RestController
@Api(description = "轮播管理接口")
public class ApiFocusController extends ApiBaseController {
    @Autowired
    private FocusPictureService focusPictureService;

    @ApiOperation(value = "获取轮播列表", notes = "获取轮播列表")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "currentPage", value = "分页，页码从1开始", dataType = "int"),
            @ApiImplicitParam(paramType = "query", name = "pageNumber", value = "每一页大小", dataType = "int"),
            @ApiImplicitParam(paramType = "query", name = "focusID", value = "轮播id", dataType = "String")})
    @RequestMapping(value = "/getFocusPictureList")
    public MessagePacket getFocusPictureList(HttpServletRequest request) {
        String focusID = request.getParameter("focusID");
        if (StringUtils.isEmpty(focusID)) {
            return MessagePacket.newFail(MessageHeader.Code.focusIDIsNull, "focusID不能为空");
        }

        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("isValid", Constants.ISVALID_YES);
        paramMap.put("isLock", Constants.ISLOCK_NO);
        paramMap.put("focusID", focusID);

        List<Sort.Order> orders = new ArrayList<Sort.Order>();
        orders.add(new Sort.Order(Sort.Direction.ASC, "orderSeq"));

        Object currentPage = request.getParameter("currentPage");
        Object pageNumber = request.getParameter("pageNumber");

        TPage page = ApiPageUtil.makePage(currentPage, pageNumber);

        Pageable pageable = new PageRequest(page.getOffset(), page.getPageSize(), new Sort(orders));

        Page<FocusPicture> rs = focusPictureService.list(paramMap, pageable);

        List<FocusPictureListDto> list = null;
        if (rs != null && rs.getSize() != 0) {
            list = BeanMapper.mapList(rs.getContent(), FocusPictureListDto.class);
            page.setTotalSize((int) rs.getTotalElements());
        }
        Map<String, Object> rsMap = Maps.newHashMap();
        MessagePage messagePage = new MessagePage(page, list);
        rsMap.put("data", messagePage);

        return MessagePacket.newSuccess(rsMap, "getFocusPictureList success!");
    }
}
