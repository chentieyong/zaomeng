package com.kingpivot.base.release.dao;

import com.kingpivot.base.release.model.Release;
import com.kingpivot.common.dao.BaseDao;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import javax.persistence.Table;

@Repository
@Table(name = "`release`")
@Qualifier("releaseDao")
public interface ReleaseDao extends BaseDao<Release, String> {
}
