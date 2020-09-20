package com.kingpivot.api.controller.ApiGoodsShopParameterController;

import com.google.common.collect.Maps;
import com.kingpivot.api.dto.goodsShopParameter.GoodsShopParameterListDto;
import com.kingpivot.base.goodsShopParameter.model.GoodsShopParameter;
import com.kingpivot.base.goodsShopParameter.service.GoodsShopParameterService;
import com.kingpivot.common.util.Constants;
import com.kingpivot.common.utils.ApiPageUtil;
import com.kingpivot.common.utils.BeanMapper;
import com.kingpivot.common.utils.TPage;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequestMapping("/api")
@RestController
@Api(description = "商品参数接口")
public class ApiGoodsShopParameterController {

    @Autowired
    private GoodsShopParameterService goodsShopParameterService;

    @ApiOperation(value = "获取商品参数列表", notes = "获取商品参数列表")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "currentPage", value = "分页，页码从1开始", dataType = "int"),
            @ApiImplicitParam(paramType = "query", name = "pageNumber", value = "每一页大小", dataType = "int"),
            @ApiImplicitParam(paramType = "query", name = "goodsShopID", value = "商品id", dataType = "String")})
    @RequestMapping(value = "/getGoodsShopParameterList")
    public MessagePacket getGoodsShopParameterList(HttpServletRequest request) {
        String goodsShopID = request.getParameter("goodsShopID");
        if (StringUtils.isEmpty(goodsShopID)) {
            return MessagePacket.newFail(MessageHeader.Code.goodsShopIdIsNull, "goodsShopID不能为空");
        }
        String goodsShopParameterDefineID = request.getParameter("goodsShopParameterDefineID");//产品参数定义ID
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("goodsShopID", goodsShopID);
        if (StringUtils.isNotBlank(goodsShopParameterDefineID)) {
            paramMap.put("goodsShopParameterDefineID", goodsShopParameterDefineID);
        }
        paramMap.put("isValid", Constants.ISVALID_YES);
        paramMap.put("isLock", Constants.ISLOCK_NO);

        List<Sort.Order> orders = new ArrayList<Sort.Order>();
        orders.add(new Sort.Order(Sort.Direction.ASC, "orderSeq"));

        Object currentPage = request.getParameter("currentPage");
        Object pageNumber = request.getParameter("pageNumber");

        TPage page = ApiPageUtil.makePage(currentPage, pageNumber);

        Pageable pageable = new PageRequest(page.getOffset(), page.getPageSize(), new Sort(orders));

        Page<GoodsShopParameter> rs = goodsShopParameterService.list(paramMap, pageable);

        List<GoodsShopParameterListDto> list = null;
        if (rs != null && rs.getSize() != 0) {
            list = BeanMapper.mapList(rs.getContent(), GoodsShopParameterListDto.class);
            page.setTotalSize((int) rs.getTotalElements());
        }
        Map<String, Object> rsMap = Maps.newHashMap();
        MessagePage messagePage = new MessagePage(page, list);
        rsMap.put("data", messagePage);

        return MessagePacket.newSuccess(rsMap, "getGoodsShopParameterList success!");
    }
}
