package com.kingpivot.base.pageNode.dao;

import com.kingpivot.base.pageNode.model.PageNode;
import com.kingpivot.common.dao.BaseDao;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.persistence.Table;

@Repository
@Table(name = "page_node")
@Qualifier("pageNodeDao")
public interface PageNodeDao extends BaseDao<PageNode, String> {

    @Query(value = "SELECT * FROM page_node WHERE viewUrl=?1 and isValid=1 and isLock=0 limit 1", nativeQuery = true)
    PageNode getPageNodeByViewUrl(String viewUrl);
}
