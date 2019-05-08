package com.kingpivot.base.bonusDefine.dao;

import com.kingpivot.base.bonusDefine.model.BonusDefine;
import com.kingpivot.common.dao.BaseDao;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.Table;

@Repository
@Table(name = "bonusdefine")
@Qualifier("bonusDefineDao")
public interface BonusDefineDao extends BaseDao<BonusDefine, String> {
}
