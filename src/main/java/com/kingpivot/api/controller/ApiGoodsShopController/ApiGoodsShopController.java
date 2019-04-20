package com.kingpivot.api.controller.ApiGoodsShopController;

import com.google.common.collect.Maps;
import com.kingpivot.api.dto.goodsShop.GoodsShopDetailDto;
import com.kingpivot.api.dto.goodsShop.GoodsShopListDto;
import com.kingpivot.base.goodsShop.model.GoodsShop;
import com.kingpivot.base.goodsShop.service.GoodsShopService;
import com.kingpivot.base.memberRank.service.MemberRankService;
import com.kingpivot.common.jms.SendMessageService;
import com.kingpivot.common.util.Constants;
import com.kingpivot.common.utils.ApiPageUtil;
import com.kingpivot.common.utils.BeanMapper;
import com.kingpivot.common.utils.NumberUtils;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequestMapping("/api")
@RestController
@Api(description = "店铺商品管理接口")
public class ApiGoodsShopController extends ApiBaseController {
    @Autowired
    private GoodsShopService goodsShopService;
    @Autowired
    private MemberRankService memberRankService;


    @ApiOperation(value = "获取商品列表", notes = "获取商品列表")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "currentPage", value = "分页，页码从1开始", dataType = "int"),
            @ApiImplicitParam(paramType = "query", name = "pageNumber", value = "每一页大小", dataType = "int"),
            @ApiImplicitParam(paramType = "query", name = "goodsCategoryID", value = "分类id", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "memberID", value = "会员id", dataType = "String")})
    @RequestMapping(value = "/getGoodsShopList")
    public MessagePacket getGoodsShopList(HttpServletRequest request) {
        String goodsCategoryID = request.getParameter("goodsCategoryID");
        String memberID = request.getParameter("memberID");

        if (StringUtils.isEmpty(goodsCategoryID)) {
            return MessagePacket.newFail(MessageHeader.Code.goodsCategoryIDIsNull, "goodsCategoryID不能为空");
        }

        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("isValid", Constants.ISVALID_YES);
        paramMap.put("isLock", Constants.ISLOCK_NO);
        if (StringUtils.isNotBlank(goodsCategoryID)) {
            paramMap.put("goodsCategoryID", goodsCategoryID);
        }
        paramMap.put("publishStatus", 3);

        List<Sort.Order> orders = new ArrayList<Sort.Order>();
        orders.add(new Sort.Order(Sort.Direction.ASC, "orderSeq"));

        Object currentPage = request.getParameter("currentPage");
        Object pageNumber = request.getParameter("pageNumber");

        TPage page = ApiPageUtil.makePage(currentPage, pageNumber);

        Pageable pageable = new PageRequest(page.getStart(), page.getPageSize(), new Sort(orders));

        Page<GoodsShop> rs = goodsShopService.list(paramMap, pageable);

        List<GoodsShopListDto> list = null;
        if (rs != null && rs.getSize() != 0) {
            list = BeanMapper.mapList(rs.getContent(), GoodsShopListDto.class);
            if (StringUtils.isNotBlank(memberID)) {
                double rate = memberRankService.getDepositeRateByMemberId(memberID);
                if (rate != 1) {
                    for (GoodsShopListDto goodsShopListDto : list) {
                        goodsShopListDto.setShowPrice(NumberUtils.keepPrecision(rate * goodsShopListDto.getRealPrice(), 2));
                    }
                }
            }
            page.setTotalSize(rs.getSize());
        }
        Map<String, Object> rsMap = Maps.newHashMap();
        MessagePage messagePage = new MessagePage(page, list);
        rsMap.put("data", messagePage);

        return MessagePacket.newSuccess(rsMap, "getGoodsShopList success!");
    }

    @ApiOperation(value = "获取商品详情", notes = "获取商品详情")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "goodsShopID", value = "店铺商品id", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "memberID", value = "会员id", dataType = "String")})
    @RequestMapping(value = "/getGoodsShopDetail")
    public MessagePacket getGoodsShopDetail(HttpServletRequest request) {
        String goodsShopID = request.getParameter("goodsShopID");
        String memberID = request.getParameter("memberID");

        if (StringUtils.isEmpty(goodsShopID)) {
            return MessagePacket.newFail(MessageHeader.Code.goodsShopIdIsNull, "goodsShopID不能为空");
        }

        GoodsShop goodsShop = goodsShopService.findById(goodsShopID);
        if (goodsShop == null) {
            return MessagePacket.newFail(MessageHeader.Code.goodsShopIdIsError, "goodsShopID不正确");
        }

        GoodsShopDetailDto goodsShopDetailDto = BeanMapper.map(goodsShop, GoodsShopDetailDto.class);
        if (StringUtils.isNotBlank(memberID)) {
            double rate = memberRankService.getDepositeRateByMemberId(memberID);
            if (rate != 1) {
                goodsShopDetailDto.setShowPrice(NumberUtils.keepPrecision(rate * goodsShopDetailDto.getRealPrice(), 2));
            }
        }

        Map<String, Object> rsMap = Maps.newHashMap();
        rsMap.put("data", goodsShopDetailDto);

        return MessagePacket.newSuccess(rsMap, "getGoodsShopDetail success!");
    }
}
