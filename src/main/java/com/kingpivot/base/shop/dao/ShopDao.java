package com.kingpivot.base.shop.dao;

import com.kingpivot.base.shop.model.Shop;
import com.kingpivot.common.dao.BaseDao;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.persistence.Table;

@Repository
@Table(name = "shop")
@Qualifier("shopDao")
public interface ShopDao extends BaseDao<Shop, String> {
    @Query(value = "select name from shop where id=?1 and isValid=1 and isLock=0", nativeQuery = true)
    String getNameById(String id);
}
