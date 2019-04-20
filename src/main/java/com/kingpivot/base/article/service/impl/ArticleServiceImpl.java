package com.kingpivot.base.article.service.impl;

import com.kingpivot.base.article.dao.ArticleDao;
import com.kingpivot.base.article.model.Article;
import com.kingpivot.base.article.service.ArticleService;
import com.kingpivot.common.dao.BaseDao;
import com.kingpivot.common.service.BaseServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service("articleService")
public class ArticleServiceImpl extends BaseServiceImpl<Article, String> implements ArticleService {

    @Resource(name = "articleDao")
    private ArticleDao articleDao;

    @Override
    public BaseDao<Article, String> getDAO() {
        return this.articleDao;
    }

}
