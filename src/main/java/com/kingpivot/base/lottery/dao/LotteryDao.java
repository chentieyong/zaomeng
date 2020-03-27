package com.kingpivot.base.lottery.dao;

import com.kingpivot.base.lottery.model.Lottery;
import com.kingpivot.common.dao.BaseDao;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import javax.persistence.Table;

@Repository
@Table(name = "lottery")
@Qualifier("lotteryDao")
public interface LotteryDao extends BaseDao<Lottery, String> {

}