package com.kingpivot.base.cart.dao;

import com.kingpivot.base.cart.model.Cart;
import com.kingpivot.common.dao.BaseDao;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.persistence.Table;

@Repository
@Table(name = "cart")
@Qualifier("cart")
public interface CartDao extends BaseDao<Cart, String> {

    @Query(value = "SELECT id FROM cart WHERE memberID=?1 AND isValid=1 AND isLock=0 limit 1", nativeQuery = true)
    String getCartIdByMemberID(String memberID);
}
