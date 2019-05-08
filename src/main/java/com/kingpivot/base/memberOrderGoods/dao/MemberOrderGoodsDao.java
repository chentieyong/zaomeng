package com.kingpivot.base.memberOrderGoods.dao;

import com.kingpivot.base.memberOrderGoods.model.MemberOrderGoods;
import com.kingpivot.common.dao.BaseDao;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.persistence.Table;
import java.util.List;

@Repository
@Table(name = "memberOrderGoods")
@Qualifier("memberOrderGoodsDao")
public interface MemberOrderGoodsDao extends BaseDao<MemberOrderGoods, String> {
    @Query(value = "select * from memberOrderGoods where memberOrderID=?1 and isValid=1 and isLock=0",nativeQuery = true)
    List<MemberOrderGoods> getMemberOrderGoodsByMemberOrderID(String memberOrderID);
}
