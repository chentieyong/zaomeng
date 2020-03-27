package com.kingpivot.base.raffle.dao;

import com.kingpivot.base.raffle.model.Raffle;
import com.kingpivot.common.dao.BaseDao;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import javax.persistence.Table;

@Repository
@Table(name = "raffle")
@Qualifier("raffleDao")
public interface RaffleDao extends BaseDao<Raffle, String> {

}