package com.kingpivot.base.memberOrderReturnGoods.dao;

import com.kingpivot.base.memberOrderReturnGoods.model.MemberOrderGoodsReturn;
import com.kingpivot.common.dao.BaseDao;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import javax.persistence.Table;

@Repository
@Table(name = "memberOrderGoodsReturn")
@Qualifier("memberOrderGoodsReturnDao")
public interface MemberOrderGoodsReturnDao extends BaseDao<MemberOrderGoodsReturn, String> {
}
