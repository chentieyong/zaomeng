package com.kingpivot.base.article.service.impl;

import com.kingpivot.base.article.dao.ArticleFileDao;
import com.kingpivot.base.article.model.ArticleFile;
import com.kingpivot.base.article.service.ArticleFileService;
import com.kingpivot.common.dao.BaseDao;
import com.kingpivot.common.service.BaseServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service("articleFileService")
public class ArticleFileServiceImpl extends BaseServiceImpl<ArticleFile, String> implements ArticleFileService {

    @Resource(name = "articleFileDao")
    private ArticleFileDao articleFileDao;

    @Override
    public BaseDao<ArticleFile, String> getDAO() {
        return this.articleFileDao;
    }

}
