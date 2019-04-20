package com.kingpivot.base.cartGoods.dao;

import com.kingpivot.base.cartGoods.model.CartGoods;
import com.kingpivot.common.dao.BaseDao;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.persistence.Table;
import java.util.List;

@Repository
@Table(name = "cartGoods")
@Qualifier("cartGoodsDao")
public interface CartGoodsDao extends BaseDao<CartGoods, String> {

    @Query(value = "SELECT * FROM cartGoods WHERE cartID=?1 AND if(?2 is not null,objectFeatureItemID1=?2,2=2) AND isValid=1 AND isLock=0", nativeQuery = true)
    CartGoods getCartGoodsByCartIDAndObjectFeatureItemID(String cartID, String objectFeatureItemID1);

    @Query(value = "SELECT * FROM cartGoods WHERE cartID=?1 AND isSelected=1 AND isValid=1 AND isLock=0", nativeQuery = true)
    List<CartGoods> getCartGoodsListByCartID(String cartID);
}
