package com.kingpivot.api.controller.ApiObjectFeatureItemController;

import com.google.common.collect.Maps;
import com.kingpivot.api.dto.objectFeatureItem.ApiObjectFeatureItemDefineDto;
import com.kingpivot.api.dto.objectFeatureItem.ApiObjectFeatureItemDto;
import com.kingpivot.base.featureDefine.model.FeatureDefine;
import com.kingpivot.base.featureDefine.service.FeatureDefineService;
import com.kingpivot.base.objectFeatureItem.service.ObjectFeatureItemService;
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
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@RequestMapping("/api")
@RestController
@Api(description = "对象特征定义选项管理接口")
public class ApiObjectFeatureItemController {

    @Autowired
    private ObjectFeatureItemService objectFeatureItemService;
    @Autowired
    private FeatureDefineService featureDefineService;

    @ApiOperation(value = "获取对象特征选项列表", notes = "获取对象特征选项列表")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "objectID", value = "对象id", dataType = "String")})
    @RequestMapping(value = "/getObjectFeatureItemList")
    public MessagePacket getObjectFeatureItemList(HttpServletRequest request) {
        String objectID = request.getParameter("objectID");

        if (StringUtils.isEmpty(objectID)) {
            return MessagePacket.newFail(MessageHeader.Code.objectIDNotNull, "objectID不能为空");
        }
        List<ApiObjectFeatureItemDefineDto> list = new LinkedList<>();
        List<ApiObjectFeatureItemDto> itemList = null;

        Object[] featureItemDefineObj = null;
        Object[] objectFeatureItemObj = null;
        ApiObjectFeatureItemDefineDto apiObjectFeatureItemDefineDto = null;
        ApiObjectFeatureItemDto apiObjectFeatureItemDto = null;
        Object[] featureItemDefineDtoList = objectFeatureItemService.getFeatureItemDefineList(objectID);
        for (int i = 0; i < featureItemDefineDtoList.length; i++) {
            featureItemDefineObj = (Object[]) featureItemDefineDtoList[i];
            apiObjectFeatureItemDefineDto = new ApiObjectFeatureItemDefineDto();
            apiObjectFeatureItemDefineDto.setFeatureDefineID((String) featureItemDefineObj[0]);
            apiObjectFeatureItemDefineDto.setFeatureDefineName((String) featureItemDefineObj[1]);
            if (StringUtils.isBlank(apiObjectFeatureItemDefineDto.getFeatureDefineName())) { //如果"特征定义名称"为空
                if (StringUtils.isNotBlank(apiObjectFeatureItemDefineDto.getFeatureDefineID())) { //"特征定义ID"不为空
                    FeatureDefine featureDefine = featureDefineService.findById(apiObjectFeatureItemDefineDto.getFeatureDefineID());
                    if (featureDefine != null) {
                        apiObjectFeatureItemDefineDto.setFeatureDefineName(featureDefine.getName());
                    }
                }
            }
            Object[] objectFeatureItemListDto = objectFeatureItemService.getFeatureItemList(objectID, apiObjectFeatureItemDefineDto.getFeatureDefineID());
            itemList = new LinkedList<>();
            for (int j = 0; j < objectFeatureItemListDto.length; j++) {
                objectFeatureItemObj = (Object[]) objectFeatureItemListDto[j];
                apiObjectFeatureItemDto = new ApiObjectFeatureItemDto();
                apiObjectFeatureItemDto.setObjectFeatureItemName((String) objectFeatureItemObj[0]);
                apiObjectFeatureItemDto.setObjectFeatureItemID((String) objectFeatureItemObj[1]);
                apiObjectFeatureItemDto.setListImage((String) objectFeatureItemObj[2]);
                apiObjectFeatureItemDto.setFaceImage((String) objectFeatureItemObj[3]);
                itemList.add(apiObjectFeatureItemDto);
            }
            apiObjectFeatureItemDefineDto.setItemList(itemList);
            list.add(apiObjectFeatureItemDefineDto);
        }
        Map<String, Object> rsMap = Maps.newHashMap();
        rsMap.put("data", list);

        return MessagePacket.newSuccess(rsMap, "getObjectFeatureItemList success");

    }
}
