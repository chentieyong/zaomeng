package com.kingpivot.api.controller.ApiArticleController;

import com.google.common.collect.Maps;
import com.kingpivot.api.dto.article.ArticleDetailDto;
import com.kingpivot.api.dto.article.ArticleListDto;
import com.kingpivot.base.article.model.Article;
import com.kingpivot.base.article.service.ArticleService;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequestMapping("/api")
@RestController
@Api(description = "文章管理接口")
public class ApiArticleController extends ApiBaseController {

    @Autowired
    private ArticleService articleService;

    @ApiOperation(value = "获取文章详情", notes = "获取文章详情")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "articleID", value = "文章id", dataType = "String")})
    @RequestMapping(value = "/getArticleDetail")
    public MessagePacket getArticleDetail(HttpServletRequest request) {
        String articleID = request.getParameter("articleID");

        if (StringUtils.isEmpty(articleID)) {
            return MessagePacket.newFail(MessageHeader.Code.articleIDIsNull, "articleID不能为空");
        }

        Article article = articleService.findById(articleID);
        if (article == null) {
            return MessagePacket.newFail(MessageHeader.Code.articleIDIsError, "articleID不正确");
        }
        //更新阅读次数
        article.setReadTimes(article.getReadTimes() == null ? 1 : article.getReadTimes().intValue() + 1);
        articleService.save(article);
        
        ArticleDetailDto articleDetailDto = BeanMapper.map(article, ArticleDetailDto.class);
        Map<String, Object> rsMap = Maps.newHashMap();
        rsMap.put("data", articleDetailDto);

        return MessagePacket.newSuccess(rsMap, "getArticleDetail success!");
    }

    @ApiOperation(value = "获取文章列表", notes = "获取文章列表")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "currentPage", value = "分页，页码从1开始", dataType = "int"),
            @ApiImplicitParam(paramType = "query", name = "pageNumber", value = "每一页大小", dataType = "int"),
            @ApiImplicitParam(paramType = "query", name = "goodsShopID", value = "商品id", dataType = "String")})
    @RequestMapping(value = "/getArticleList")
    public MessagePacket getArticleList(HttpServletRequest request) {
        String navigatorID = request.getParameter("navigatorID");
        String companyID = request.getParameter("companyID");

        Map<String, Object> paramMap = new HashMap<>();
        if(StringUtils.isNotBlank(navigatorID)){
            paramMap.put("navigatorID", navigatorID);
        }
        if(StringUtils.isNotBlank(companyID)){
            paramMap.put("companyID", companyID);
        }
        paramMap.put("isValid", Constants.ISVALID_YES);
        paramMap.put("isLock", Constants.ISLOCK_NO);

        List<Sort.Order> orders = new ArrayList<Sort.Order>();
        orders.add(new Sort.Order(Sort.Direction.ASC, "createdTime"));

        Object currentPage = request.getParameter("currentPage");
        Object pageNumber = request.getParameter("pageNumber");

        TPage page = ApiPageUtil.makePage(currentPage, pageNumber);

        Pageable pageable = new PageRequest(page.getOffset(), page.getPageSize(), new Sort(orders));

        Page<Article> rs = articleService.list(paramMap, pageable);

        List<ArticleListDto> list = null;
        if (rs != null && rs.getSize() != 0) {
            list = BeanMapper.mapList(rs.getContent(), ArticleListDto.class);
            page.setTotalSize((int) rs.getTotalElements());
        }
        Map<String, Object> rsMap = Maps.newHashMap();
        MessagePage messagePage = new MessagePage(page, list);
        rsMap.put("data", messagePage);

        return MessagePacket.newSuccess(rsMap, "getArticleList success!");
    }
}
