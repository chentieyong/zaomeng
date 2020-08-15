package com.kingpivot.base.navigator.dao;

import com.kingpivot.base.navigator.model.Navigator;
import com.kingpivot.common.dao.BaseDao;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.persistence.Table;

@Repository
@Table(name = "navigator")
@Qualifier("navigatorDao")
public interface NavigatorDao extends BaseDao<Navigator, String> {

    @Query(value = "SELECT node.id id,node.parentID pId,node.name name,node.samllIcon smallIcon,\n" +
            "     node.largeIcon largeIcon,node.depth depth,node.orderSeq,node.isLeaf,node.functionUrl,node.description \n" +
            "     FROM navigator node,navigator parent WHERE parent.id = ?1\n" +
            "     AND parent.isValid = '1' AND node.isValid = '1'\n" +
            "     AND node.depth <= parent.depth+?2 ORDER BY node.orderSeq ASC", nativeQuery = true)
    Object[] getTreeData(String rootId, String depth);
}
