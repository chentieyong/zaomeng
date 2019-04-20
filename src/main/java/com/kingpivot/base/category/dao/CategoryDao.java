package com.kingpivot.base.category.dao;

import com.kingpivot.base.category.model.Category;
import com.kingpivot.common.dao.BaseDao;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.persistence.Table;

@Repository
@Table(name = "category")
@Qualifier("categoryDao")
public interface CategoryDao extends BaseDao<Category, String> {
    @Query(value = "SELECT node.id id,node.parentID pId,node.name name,node.smallIcon smallIcon,\n" +
            "node.largeIcon largeIcon,node.depth depth,node.orderSeq,node.isLeaf\n" +
            " FROM category node,category parent WHERE node.leftWeight BETWEEN parent.leftWeight \n" +
            "             AND parent.rightWeight AND parent.id = ?1 AND parent.isValid = '1' AND node.isValid = '1' \n" +
            "             AND node.depth <= parent.depth+?2 ORDER BY node.orderSeq ASC", nativeQuery = true)
    Object[] getTreeData(String rootId, String depth);
}
