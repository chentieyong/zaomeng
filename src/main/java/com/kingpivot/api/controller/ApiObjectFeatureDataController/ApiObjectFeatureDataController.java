package com.kingpivot.api.controller.ApiObjectFeatureDataController;

import com.google.common.collect.Maps;
import com.kingpivot.api.dto.objectFeatureData.ApiObjectFeatureDataDto;
import com.kingpivot.base.objectFeatureData.service.ObjectFeatureDataService;
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
@Api(description = "对象特征定义数据管理接口")
public class ApiObjectFeatureDataController extends ApiBaseController {
    @Autowired
    private ObjectFeatureDataService objectFeatureDataService;

    @ApiOperation(value = "获取对象特征数据", notes = "获取对象特征数据")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "objectID", value = "对象id", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "objectFeatureItemID1", value = "规格选项1", dataType = "String")})
    @RequestMapping(value = "/getObjectFeatureData")
    public MessagePacket getObjectFeatureData(HttpServletRequest request) {
        String objectID = request.getParameter("objectID");
        String objectFeatureItemID1 = request.getParameter("objectFeatureItemID1");
        if (StringUtils.isEmpty(objectID)) {
            return MessagePacket.newFail(MessageHeader.Code.objectIdIsNull, "objectID不能为空");
        }
        if (StringUtils.isEmpty(objectFeatureItemID1)) {
            return MessagePacket.newFail(MessageHeader.Code.objectFeatureItemID1IsNull, "objectFeatureItemID1不能为空");
        }
        double val = objectFeatureDataService.getObjectFetureData(objectID, objectFeatureItemID1);
        ApiObjectFeatureDataDto apiObjectFeatureDataDto = new ApiObjectFeatureDataDto();
        if (val != 0) {
            apiObjectFeatureDataDto.setShowPrice(val);
        }
        Map<String, Object> rsMap = Maps.newHashMap();
        rsMap.put("data", apiObjectFeatureDataDto);
        return MessagePacket.newSuccess(rsMap, "getObjectFeatureData success");
    }
}
