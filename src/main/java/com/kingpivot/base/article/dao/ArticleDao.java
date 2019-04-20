package com.kingpivot.base.article.dao;

import com.kingpivot.base.article.model.Article;
import com.kingpivot.common.dao.BaseDao;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import javax.persistence.Table;

@Repository
@Table(name = "article")
@Qualifier("articleDao")
public interface ArticleDao extends BaseDao<Article, String> {
}
