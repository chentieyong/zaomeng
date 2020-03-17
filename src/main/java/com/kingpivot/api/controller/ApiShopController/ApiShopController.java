package com.kingpivot.api.controller.ApiShopController;

import com.google.common.collect.Maps;
import com.kingpivot.api.dto.shop.ShopDetailDto;
import com.kingpivot.base.capitalNeed.model.CapitalNeed;
import com.kingpivot.base.shop.model.Shop;
import com.kingpivot.base.shop.service.ShopService;
import com.kingpivot.common.utils.BeanMapper;
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
@Api(description = "店铺管理接口")
public class ApiShopController extends ApiBaseController {

    @Autowired
    private ShopService shopService;

    @ApiOperation(value = "getShopDetail", notes = "获取店铺详情")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "capitalNeedID", value = "资金需求id", dataType = "String")})
    @RequestMapping(value = "/getShopDetail")
    public MessagePacket getShopDetail(HttpServletRequest request) {
        String shopID = request.getParameter("shopID");
        if (StringUtils.isEmpty(shopID)) {
            return MessagePacket.newFail(MessageHeader.Code.shopIDIsNull, "shopID不能为空");
        }
        Shop shop = shopService.findById(shopID);
        if (shop == null) {
            return MessagePacket.newFail(MessageHeader.Code.shopIDIsError, "shopID不正确");
        }
        ShopDetailDto data = BeanMapper.map(shop, ShopDetailDto.class);
        Map<String, Object> rsMap = Maps.newHashMap();
        rsMap.put("data", data);
        return MessagePacket.newSuccess(rsMap, "getShopDetail success!");
    }
}
