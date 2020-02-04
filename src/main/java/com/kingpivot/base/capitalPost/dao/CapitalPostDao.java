package com.kingpivot.base.capitalPost.dao;

import com.kingpivot.base.capitalPost.model.CapitalPost;
import com.kingpivot.common.dao.BaseDao;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import javax.persistence.Table;

@Repository
@Table(name = "capitalPost")
@Qualifier("capitalPostDao")
public interface CapitalPostDao extends BaseDao<CapitalPost,String> {
}
