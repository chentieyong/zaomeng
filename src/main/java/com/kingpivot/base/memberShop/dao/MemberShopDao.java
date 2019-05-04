package com.kingpivot.base.memberShop.dao;

import com.kingpivot.base.memberShop.model.MemberShop;
import com.kingpivot.common.dao.BaseDao;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.persistence.Table;

@Repository
@Table(name = "memberShop")
@Qualifier("memberShopDao")
public interface MemberShopDao extends BaseDao<MemberShop, String> {
    @Query(value = "SELECT id FROM memberShop WHERE address=? AND isValid=1 AND isLock=0",nativeQuery = true)
    String getIdByAddress(String address);
}
