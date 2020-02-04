package com.kingpivot.api.controller.ApiReleaseController;

import com.google.common.collect.Maps;
import com.kingpivot.api.dto.release.ReleaseArticleListDto;
import com.kingpivot.api.dto.release.ReleaseGoodsShopListDto;
import com.kingpivot.base.article.model.Article;
import com.kingpivot.base.article.service.ArticleService;
import com.kingpivot.base.config.Config;
import com.kingpivot.base.goodsShop.model.GoodsShop;
import com.kingpivot.base.goodsShop.service.GoodsShopService;
import com.kingpivot.base.memberRank.service.MemberRankService;
import com.kingpivot.base.objectFeatureData.service.ObjectFeatureDataService;
import com.kingpivot.base.objectFeatureItem.service.ObjectFeatureItemService;
import com.kingpivot.base.release.model.Release;
import com.kingpivot.base.release.service.ReleaseService;
import com.kingpivot.common.util.Constants;
import com.kingpivot.common.utils.ApiPageUtil;
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
import java.util.*;

@RequestMapping("/api")
@RestController
@Api(description = "发布管理接口")
public class ApiReleaseController extends ApiBaseController {
    @Autowired
    private ReleaseService releaseService;
    @Autowired
    private GoodsShopService goodsShopService;
    @Autowired
    private ArticleService articleService;
    @Autowired
    private ObjectFeatureItemService objectFeatureItemService;
    @Autowired
    private ObjectFeatureDataService objectFeatureDataService;

    @ApiOperation(value = "获取导航发布列表", notes = "获取导航发布列表")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "currentPage", value = "分页，页码从1开始", dataType = "int"),
            @ApiImplicitParam(paramType = "query", name = "pageNumber", value = "每一页大小", dataType = "int"),
            @ApiImplicitParam(paramType = "query", name = "objectDefineID", value = "对象定义id", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "navigatorID", value = "导航id", dataType = "String")})
    @RequestMapping(value = "/getNavigatorReleaseList")
    public MessagePacket getNavigatorReleaseList(HttpServletRequest request) {
        String navigatorID = request.getParameter("navigatorID");
        String objectDefineID = request.getParameter("objectDefineID");

        if (StringUtils.isEmpty(navigatorID)) {
            return MessagePacket.newFail(MessageHeader.Code.navigatorIDIsNull, "navigatorID不能为空");
        }

        if (StringUtils.isEmpty(objectDefineID)) {
            return MessagePacket.newFail(MessageHeader.Code.objectDefineIDIsNull, "objectDefineID不能为空");
        }

        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("isValid", Constants.ISVALID_YES);
        paramMap.put("isLock", Constants.ISLOCK_NO);
        paramMap.put("navigatorID", navigatorID);
        paramMap.put("objectDefineID", objectDefineID);

        List<Sort.Order> orders = new ArrayList<Sort.Order>();
        orders.add(new Sort.Order(Sort.Direction.DESC, "createdTime"));

        Object currentPage = request.getParameter("currentPage");
        Object pageNumber = request.getParameter("pageNumber");

        TPage page = ApiPageUtil.makePage(currentPage, pageNumber);

        Pageable pageable = new PageRequest(page.getOffset(), page.getPageSize(), new Sort(orders));

        Page<Release> rs = releaseService.list(paramMap, pageable);
        Map<String, Object> rsMap = Maps.newHashMap();
        MessagePage messagePage = null;
        if (rs != null && rs.getSize() != 0) {
            switch (objectDefineID) {
                case Config.GOODSSHOP_OBJECTDEFINEID://店铺商品
                    List<ReleaseGoodsShopListDto> goodsShopList = new ArrayList<>();
                    ReleaseGoodsShopListDto releaseGoodsShopListDto = null;
                    GoodsShop goodsShop = null;
                    for (Release release : rs.getContent()) {
                        if (StringUtils.isNotBlank(release.getObjectID())) {
                            goodsShop = goodsShopService.findById(release.getObjectID());
                            if (goodsShop != null) {
                                if (goodsShop.getPublishStatus() != null && goodsShop.getPublishStatus().intValue() == 3
                                        && goodsShop.getStockNumber() > 0) {
                                    releaseGoodsShopListDto = new ReleaseGoodsShopListDto();
                                    releaseGoodsShopListDto.setObjectID(goodsShop.getId());
                                    releaseGoodsShopListDto.setObjectName(goodsShop.getName());
                                    releaseGoodsShopListDto.setListImage(goodsShop.getLittleImage());
                                    releaseGoodsShopListDto.setShowPrice(goodsShop.getRealPrice());
                                    releaseGoodsShopListDto.setStockNumber(goodsShop.getStockNumber());
                                    releaseGoodsShopListDto.setStockOut(goodsShop.getStockOut());
                                    releaseGoodsShopListDto.setUnitDescription(goodsShop.getUnitDescription());

                                    Object itemOjb = objectFeatureItemService.getDefaultFeatureItem(goodsShop.getId());
                                    if (itemOjb != null) {
                                        Object[] obj = (Object[]) itemOjb;
                                        if (obj != null) {
                                            releaseGoodsShopListDto.setUnitDescription((String) obj[0]);
                                            releaseGoodsShopListDto.setObjectFeatureItemID1((String) obj[1]);
                                            releaseGoodsShopListDto.setShowPrice(objectFeatureDataService.getObjectFetureData(goodsShop.getId(), (String) obj[1]));
                                        }
                                    }
                                    goodsShopList.add(releaseGoodsShopListDto);
                                }
                            }
                        }
                    }
                    Collections.sort(goodsShopList);
                    page.setTotalSize((int) rs.getTotalElements());
                    messagePage = new MessagePage(page, goodsShopList);
                    break;
                case Config.ARTICLE_OBJECTDEFINEID://文章
                    List<ReleaseArticleListDto> articleList = new ArrayList<>();
                    Article article = null;
                    ReleaseArticleListDto releaseArticleListDto = null;
                    for (Release release : rs.getContent()) {
                        if (StringUtils.isNotBlank(release.getObjectID())) {
                            article = articleService.findById(release.getObjectID());
                            if (article != null) {
                                releaseArticleListDto = new ReleaseArticleListDto();
                                releaseArticleListDto.setId(article.getId());
                                releaseArticleListDto.setAuthor(article.getAuthor());
                                releaseArticleListDto.setDescription(article.getDescription());
                                releaseArticleListDto.setFaceImage(article.getFaceImage());
                                releaseArticleListDto.setListImage(article.getListImage());
                                releaseArticleListDto.setTitle(article.getTitle());
                                releaseArticleListDto.setReadTimes(article.getReadTimes());
                                releaseArticleListDto.setCreatedTime(release.getCreatedTime());
                                articleList.add(releaseArticleListDto);
                            }
                        }
                    }
                    page.setTotalSize((int) rs.getTotalElements());
                    messagePage = new MessagePage(page, articleList);
                    break;
                default:
                    messagePage = new MessagePage(page, new ArrayList());
                    break;
            }
        } else {
            messagePage = new MessagePage(page, new ArrayList());
        }
        rsMap.put("data", messagePage);

        return MessagePacket.newSuccess(rsMap, "getNavigatorReleaseList success!");
    }
}
