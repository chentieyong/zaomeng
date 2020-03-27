package com.kingpivot.base.memberRaffle.dao;

import com.kingpivot.base.memberRaffle.model.MemberRaffle;
import com.kingpivot.common.dao.BaseDao;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import javax.persistence.Table;

@Repository
@Table(name = "memberRaffle")
@Qualifier("memberRaffleDao")
public interface MemberRaffleDao extends BaseDao<MemberRaffle, String> {

}