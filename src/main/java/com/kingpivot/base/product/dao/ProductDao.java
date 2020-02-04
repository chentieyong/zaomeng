package com.kingpivot.base.product.dao;

import com.kingpivot.base.product.model.Product;
import com.kingpivot.common.dao.BaseDao;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import javax.persistence.Table;

@Repository
@Table(name = "product")
@Qualifier("productDao")
public interface ProductDao extends BaseDao<Product, String> {

}