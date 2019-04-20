package com.kingpivot.base.site.dao;

import com.kingpivot.base.site.model.Site;
import com.kingpivot.common.dao.BaseDao;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import javax.persistence.Table;

@Repository
@Table(name = "site")
@Qualifier("siteDao")
public interface SiteDao extends BaseDao<Site, String> {
}