package com.kingpivot.base.blessingTree.dao;

import com.kingpivot.base.blessingTree.model.BlessingTree;
import com.kingpivot.common.dao.BaseDao;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import javax.persistence.Table;

@Repository
@Table(name = "blessingTree")
@Qualifier("blessingTreeDao")
public interface BlessingTreeDao extends BaseDao<BlessingTree, String> {

}