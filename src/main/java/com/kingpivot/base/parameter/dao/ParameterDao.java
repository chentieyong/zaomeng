package com.kingpivot.base.parameter.dao;

import com.kingpivot.base.parameter.model.Parameter;
import com.kingpivot.common.dao.BaseDao;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.persistence.Table;

@Repository
@Table(name = "parameter")
@Qualifier("parameterDao")
public interface ParameterDao extends BaseDao<Parameter, String> {

    @Query(value = "SELECT `value` FROM parameter WHERE `code`=? AND isValid=1 AND isLock=0 LIMIT 1",nativeQuery = true)
    String getParemeterValueByCode(String code);
}
