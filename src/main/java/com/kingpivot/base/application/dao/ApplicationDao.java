package com.kingpivot.base.application.dao;

import com.kingpivot.base.application.model.Application;
import com.kingpivot.common.dao.BaseDao;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.persistence.Table;

@Repository
@Table(name = "application")
@Qualifier("applicationDao")
public interface ApplicationDao extends BaseDao<Application, String> {
    @Query(value = "SELECT `name` FROM application WHERE id=?1", nativeQuery = true)
    String getNameByAppid(String id);
}
