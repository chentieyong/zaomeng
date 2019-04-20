package com.kingpivot.base.sequenceHistory.dao;

import com.kingpivot.base.sequenceHistory.model.SequenceHistory;
import com.kingpivot.common.dao.BaseDao;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import javax.persistence.Table;

@Repository
@Table(name = "sequenceHistory")
@Qualifier("sequenceHistoryDao")
public interface SequenceHistoryDao extends BaseDao<SequenceHistory, String> {
}
