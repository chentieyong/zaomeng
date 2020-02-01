package com.kingpivot.base.major.dao;

import com.kingpivot.base.major.model.Major;
import com.kingpivot.common.dao.BaseDao;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import javax.persistence.Table;

@Repository
@Table(name = "major")
@Qualifier("majorDao")
public interface MajorDao extends BaseDao<Major, String> {
}
