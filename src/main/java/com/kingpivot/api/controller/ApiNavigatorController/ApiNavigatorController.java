package com.kingpivot.api.controller.ApiNavigatorController;

import com.google.common.collect.Maps;
import com.kingpivot.base.navigator.service.NavigatorService;
import com.kingpivot.common.util.TreeInfo;
import com.kingpivot.common.util.TreeInfoDTO;
import com.kingpivot.protocol.ApiBaseController;
import com.kingpivot.protocol.MessageHeader;
import com.kingpivot.protocol.MessagePacket;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RequestMapping("/api")
@RestController
@Api(description = "导航管理接口")
public class ApiNavigatorController extends ApiBaseController {
    @Autowired
    private NavigatorService navigatorService;

    @ApiOperation(value = "获取导航列表", notes = "获取导航列表")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "rootID", value = "父级id", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "depth", value = "深度", dataType = "String")})
    @RequestMapping(value = "/getNavigatorList")
    public MessagePacket getNavigatorList(HttpServletRequest request) {
        String rootID = request.getParameter("rootID");
        if (StringUtils.isEmpty(rootID)) {
            return MessagePacket.newFail(MessageHeader.Code.illegalParameter, "rootID不能为空");
        }
        String depth = request.getParameter("depth");
        if (StringUtils.isEmpty(depth)) {
            depth = "1";
        }
        TreeInfoDTO<TreeInfo> data = navigatorService.getTreeData(rootID, depth);
        Map<String, Object> map = Maps.newConcurrentMap();
        map.put("data", data == null ? "" : data);
        return MessagePacket.newSuccess(map);
    }
}
