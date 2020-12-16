package com.kingpivot.api.controller.ApiShopController;

import com.google.common.collect.Maps;
import com.kingpivot.api.dto.shop.ShopDetailDto;
import com.kingpivot.api.dto.shop.ShopListDto;
import com.kingpivot.base.shop.model.Shop;
import com.kingpivot.base.shop.service.ShopService;
import com.kingpivot.common.util.Constants;
import com.kingpivot.common.utils.ApiPageUtil;
import com.kingpivot.common.utils.BeanMapper;
import com.kingpivot.common.utils.TPage;
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

import javax.servlet.http.HttpServletRequest;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequestMapping("/api")
@RestController
@Api(description = "店铺管理接口")
public class ApiShopController extends ApiBaseController {

    @Autowired
    private ShopService shopService;

    @ApiOperation(value = "getShopList", notes = "获取店铺列表")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "applicationID", value = "应用id", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "currentPage", value = "分页，页码从1开始", dataType = "int"),
            @ApiImplicitParam(paramType = "query", name = "pageNumber", value = "每一页大小", dataType = "int")})
    @RequestMapping(value = "/getShopList")
    public MessagePacket getShopList(HttpServletRequest request) {
        String keyWords = request.getParameter("keywords");
        String shopType = request.getParameter("shopType");
        String address = request.getParameter("address");
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("isValid", Constants.ISVALID_YES);
        paramMap.put("isLock", Constants.ISLOCK_NO);
        if (StringUtils.isNotBlank(keyWords)) {
            paramMap.put("name:like", keyWords);
        }
        if (StringUtils.isNotBlank(shopType)) {
            paramMap.put("shopType", Integer.parseInt(shopType));
        }
        if (StringUtils.isNotBlank(address)) {
            paramMap.put("address:like", address);
        }
        List<Sort.Order> orders = new ArrayList<Sort.Order>();
        orders.add(new Sort.Order(Sort.Direction.DESC, "createdTime"));

        Object currentPage = request.getParameter("currentPage");
        Object pageNumber = request.getParameter("pageNumber");

        TPage page = ApiPageUtil.makePage(currentPage, pageNumber);

        Pageable pageable = new PageRequest(page.getOffset(), page.getPageSize(), new Sort(orders));

        Page<Shop> rs = shopService.list(paramMap, pageable);
        List<ShopListDto> list = null;
        if (rs != null && rs.getSize() != 0) {
            list = BeanMapper.mapList(rs.getContent(), ShopListDto.class);
            page.setTotalSize((int) rs.getTotalElements());
        }
        Map<String, Object> rsMap = Maps.newHashMap();
        MessagePage messagePage = new MessagePage(page, list);
        rsMap.put("data", messagePage);
        return MessagePacket.newSuccess(rsMap, "getShopList success!");
    }

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
