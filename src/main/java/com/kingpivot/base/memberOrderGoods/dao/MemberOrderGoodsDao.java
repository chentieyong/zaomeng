package com.kingpivot.base.memberOrderGoods.dao;

import com.kingpivot.base.memberOrderGoods.model.MemberOrderGoods;
import com.kingpivot.common.dao.BaseDao;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import javax.persistence.Table;

@Repository
@Table(name = "memberOrderGoods")
@Qualifier("memberOrderGoodsDao")
public interface MemberOrderGoodsDao extends BaseDao<MemberOrderGoods, String> {
}
