package com.kingpivot.base.wechart.dao;

import com.kingpivot.base.wechart.model.Wechart;
import com.kingpivot.common.dao.BaseDao;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.persistence.Table;

@Repository
@Table(name = "wechart")
@Qualifier("wechartDao")
public interface WechartDao extends BaseDao<Wechart, String> {

    @Query(value = "SELECT * FROM wechart WHERE PUBLIC_NO=?1 AND isValid=1 AND isLock=0", nativeQuery = true)
    Wechart getWechartByPublicNo(String publicNo);
}
