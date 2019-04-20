package com.kingpivot.base.memberBonus.dao;

import com.kingpivot.base.memberBonus.model.MemberBonus;
import com.kingpivot.common.dao.BaseDao;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import javax.persistence.Table;

@Repository
@Table(name = "memberbonus")
@Qualifier("memberBonusDao")
public interface MemberBonusDao extends BaseDao<MemberBonus, String> {

}
