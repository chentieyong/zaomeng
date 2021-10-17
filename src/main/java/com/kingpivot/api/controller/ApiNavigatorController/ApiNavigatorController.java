package com.kingpivot.api.controller.ApiNavigatorController;

import com.google.common.collect.Maps;
import com.kingpivot.api.dto.navigator.NodeNavigatorListDto;
import com.kingpivot.api.dto.release.ReleaseArticleListDto;
import com.kingpivot.api.dto.release.ReleaseGoodsShopListDto;
import com.kingpivot.base.article.model.Article;
import com.kingpivot.base.article.service.ArticleService;
import com.kingpivot.base.config.Config;
import com.kingpivot.base.goodsShop.model.GoodsShop;
import com.kingpivot.base.goodsShop.service.GoodsShopService;
import com.kingpivot.base.memberRank.service.MemberRankService;
import com.kingpivot.base.navigator.model.Navigator;
import com.kingpivot.base.navigator.service.NavigatorService;
import com.kingpivot.base.objectFeatureData.model.ObjectFeatureData;
import com.kingpivot.base.objectFeatureData.service.ObjectFeatureDataService;
import com.kingpivot.base.objectFeatureItem.service.ObjectFeatureItemService;
import com.kingpivot.base.release.model.Release;
import com.kingpivot.base.release.service.ReleaseService;
import com.kingpivot.common.util.Constants;
import com.kingpivot.common.util.TreeInfo;
import com.kingpivot.common.util.TreeInfoDTO;
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
import org.apache.commons.lang.StringUtils;
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
@Api(description = "导航管理接口")
public class ApiNavigatorController extends ApiBaseController {
    @Autowired
    private NavigatorService navigatorService;
    @Autowired
    private ReleaseService releaseService;
    @Autowired
    private GoodsShopService goodsShopService;
    @Autowired
    private ObjectFeatureItemService objectFeatureItemService;
    @Autowired
    private ObjectFeatureDataService objectFeatureDataService;
    @Autowired
    private ArticleService articleService;

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

    @ApiOperation(value = "获取子集导航列表", notes = "获取子集导航列表")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "rootID", value = "父级id", dataType = "String")})
    @RequestMapping(value = "/getNodeNavigatorList")
    public MessagePacket getNodeNavigatorList(HttpServletRequest request) {
        String rootID = request.getParameter("rootID");
        String objectDefineID = request.getParameter("objectDefineID");
        if (StringUtils.isEmpty(rootID)) {
            return MessagePacket.newFail(MessageHeader.Code.illegalParameter, "rootID不能为空");
        }

        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("isValid", Constants.ISVALID_YES);
        paramMap.put("isLock", Constants.ISLOCK_NO);
        paramMap.put("parentID", rootID);

        List<Sort.Order> orders = new ArrayList<Sort.Order>();
        orders.add(new Sort.Order(Sort.Direction.ASC, "orderSeq"));

        Object currentPage = request.getParameter("currentPage");
        Object pageNumber = request.getParameter("pageNumber");

        TPage page = ApiPageUtil.makePage(currentPage, pageNumber);

        Pageable pageable = new PageRequest(page.getOffset(), page.getPageSize(), new Sort(orders));

        Page<Navigator> rs = navigatorService.list(paramMap, pageable);

        List<NodeNavigatorListDto> list = null;
        if (rs != null && rs.getSize() != 0) {
            list = BeanMapper.mapList(rs.getContent(), NodeNavigatorListDto.class);
            page.setTotalSize((int) rs.getTotalElements());
            if (StringUtils.isNotBlank(objectDefineID)) {
                switch (objectDefineID) {
                    case Config.GOODSSHOP_OBJECTDEFINEID://店铺商品
                        List<ReleaseGoodsShopListDto> goodsShopList = null;
                        ReleaseGoodsShopListDto releaseGoodsShopListDto = null;
                        GoodsShop goodsShop = null;
                        Map<String, Object> releaseParamMap = null;
                        TPage releasePage = null;
                        Pageable releasePageable = null;
                        Page<Release> releaseRs = null;
                        for (NodeNavigatorListDto nodeNavigatorListDto : list) {
                            goodsShopList = new ArrayList<>();
                            List<Sort.Order> releaseOrders = new ArrayList<Sort.Order>();
                            releaseOrders.add(new Sort.Order(Sort.Direction.ASC, "orderSeq"));

                            releaseParamMap = new HashMap<>();
                            releaseParamMap.put("navigatorID", nodeNavigatorListDto.getId());
                            releaseParamMap.put("objectDefineID", Config.GOODSSHOP_OBJECTDEFINEID);
                            releaseParamMap.put("isValid", Constants.ISVALID_YES);
                            releaseParamMap.put("isLock", Constants.ISLOCK_NO);

                            releasePage = ApiPageUtil.makePage(null, null);

                            releasePageable = new PageRequest(releasePage.getOffset(), releasePage.getPageSize(), new Sort(releaseOrders));

                            releaseRs = releaseService.list(releaseParamMap, releasePageable);
                            if (StringUtils.isNotBlank(nodeNavigatorListDto.getId())) {
                                for (Release release : releaseRs.getContent()) {
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
                                                    ObjectFeatureData objectFetureData = objectFeatureDataService.getObjectFetureData(goodsShop.getId(), (String) obj[1]);
                                                    if (objectFetureData != null) {
                                                        releaseGoodsShopListDto.setShowPrice(objectFetureData.getRealPrice());
                                                    }
                                                }
                                            }
                                            goodsShopList.add(releaseGoodsShopListDto);
                                        }
                                    }
                                }
                                Collections.sort(goodsShopList);
                                nodeNavigatorListDto.setGoodsList(goodsShopList);
                            }
                        }
                        break;
                    case Config.ARTICLE_OBJECTDEFINEID://文章
                        List<ReleaseArticleListDto> articleListDtoList = null;
                        ReleaseArticleListDto releaseArticleListDto = null;
                        Article article = null;
                        Map<String, Object> articleReleaseParamMap = null;
                        TPage articleReleasePage = null;
                        Pageable articleReleasePageable = null;
                        Page<Release> articleReleaseRs = null;
                        for (NodeNavigatorListDto nodeNavigatorListDto : list) {
                            articleListDtoList = new ArrayList<>();
                            List<Sort.Order> releaseOrders = new ArrayList<Sort.Order>();
                            releaseOrders.add(new Sort.Order(Sort.Direction.ASC, "orderSeq"));

                            articleReleaseParamMap = new HashMap<>();
                            articleReleaseParamMap.put("navigatorID", nodeNavigatorListDto.getId());
                            articleReleaseParamMap.put("objectDefineID", Config.ARTICLE_OBJECTDEFINEID);
                            articleReleaseParamMap.put("isValid", Constants.ISVALID_YES);
                            articleReleaseParamMap.put("isLock", Constants.ISLOCK_NO);

                            articleReleasePage = ApiPageUtil.makePage(null, null);

                            articleReleasePageable = new PageRequest(articleReleasePage.getOffset(), articleReleasePage.getPageSize(), new Sort(releaseOrders));

                            articleReleaseRs = releaseService.list(articleReleaseParamMap, articleReleasePageable);
                            if (StringUtils.isNotBlank(nodeNavigatorListDto.getId())) {
                                for (Release release : articleReleaseRs.getContent()) {
                                    article = articleService.findById(release.getObjectID());
                                    if (article != null) {
                                        releaseArticleListDto = new ReleaseArticleListDto();
                                        releaseArticleListDto.setId(article.getId());
                                        releaseArticleListDto.setTitle(article.getTitle());
                                        releaseArticleListDto.setSubTitle(article.getSubTitle());
                                        releaseArticleListDto.setFaceImage(article.getFaceImage());
                                        releaseArticleListDto.setListImage(article.getListImage());
                                        releaseArticleListDto.setDescription(article.getDescription());
                                        releaseArticleListDto.setAuthor(article.getAuthor());
                                        articleListDtoList.add(releaseArticleListDto);
                                    }
                                }
                                nodeNavigatorListDto.setArticleList(articleListDtoList);
                            }
                        }
                        break;
                    default:
                        break;
                }
            }
        } else {
            list = new ArrayList<>();
        }
        Map<String, Object> rsMap = Maps.newHashMap();
        MessagePage messagePage = new MessagePage(page, list);
        rsMap.put("data", messagePage);

        return MessagePacket.newSuccess(rsMap, "getNodeNavigatorList success!");
    }

}
