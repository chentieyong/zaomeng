package com.kingpivot.base.article.dao;

import com.kingpivot.base.article.model.ArticleFile;
import com.kingpivot.common.dao.BaseDao;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import javax.persistence.Table;

@Repository
@Table(name = "articlefile")
@Qualifier("articleFileDao")
public interface ArticleFileDao extends BaseDao<ArticleFile, String> {

}
