package com.kingpivot.api.controller.ApiArticleController;

import com.google.common.collect.Maps;
import com.kingpivot.api.dto.article.ArticleDetailDto;
import com.kingpivot.base.article.model.Article;
import com.kingpivot.base.article.service.ArticleService;
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

        ArticleDetailDto articleDetailDto = BeanMapper.map(article, ArticleDetailDto.class);
        Map<String, Object> rsMap = Maps.newHashMap();
        rsMap.put("data", articleDetailDto);

        return MessagePacket.newSuccess(rsMap, "getArticleDetail success!");
    }
}
