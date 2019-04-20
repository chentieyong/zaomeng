package com.kingpivot.base.sequenceDefine.dao;

import com.kingpivot.base.sequenceDefine.model.SequenceDefine;
import com.kingpivot.common.dao.BaseDao;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.persistence.Table;

@Repository
@Table(name = "sequenceDefine")
@Qualifier("sequenceDefineDao")
public interface SequenceDefineDao extends BaseDao<SequenceDefine, String> {
    @Query(value = "select * FROM sequenceDefine WHERE code=?1 AND isValid = 1 AND isLock = 0", nativeQuery = true)
    SequenceDefine getValueByCode(String code);

}
