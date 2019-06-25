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

    @Query(value = "SELECT * FROM cartGoods WHERE cartID=?1 and goodsShopID=?2 AND if(?3 is not null,objectFeatureItemID1=?3,3=3) AND isValid=1 AND isLock=0", nativeQuery = true)
    CartGoods getCartGoodsByCartIDAndObjectFeatureItemID(String cartID, String goodsShopID, String objectFeatureItemID1);

    @Query(value = "SELECT t1.* FROM cartGoods t1 LEFT JOIN goodsShop t2 ON t1.goodsShopID=t2.id\n" +
            " WHERE t1.cartID=?1 AND if(?2!=-1,t1.isSelected=?2,2=2) AND t2.stockNumber>0 AND t2.publishStatus=3 AND t1.isValid=1 AND t1.isLock=0\n" +
            " AND t2.isValid=1 AND t2.isLock=0", nativeQuery = true)
    List<CartGoods> getCartGoodsListByCartID(String cartID, int isSelect);

    @Query(value = "SELECT SUM(priceTotal) FROM cartGoods WHERE cartID=?1 AND isSelected=1 AND isLock=0 AND isValid=1", nativeQuery = true)
    Double getPriceTotalByCartID(String cartID);
}
