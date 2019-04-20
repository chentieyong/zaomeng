package com.kingpivot.base.hotWord.dao;

import com.kingpivot.base.hotWord.model.HotWord;
import com.kingpivot.common.dao.BaseDao;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import javax.persistence.Table;

@Repository
@Table(name = "hotWord")
@Qualifier("hotWordDao")
public interface HotWordDao extends BaseDao<HotWord, String> {
}
