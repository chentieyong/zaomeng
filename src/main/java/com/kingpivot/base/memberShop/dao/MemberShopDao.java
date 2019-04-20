package com.kingpivot.base.memberShop.dao;

import com.kingpivot.base.memberShop.model.MemberShop;
import com.kingpivot.common.dao.BaseDao;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import javax.persistence.Table;

@Repository
@Table(name = "memberShop")
@Qualifier("memberShopDao")
public interface MemberShopDao extends BaseDao<MemberShop, String> {
}
