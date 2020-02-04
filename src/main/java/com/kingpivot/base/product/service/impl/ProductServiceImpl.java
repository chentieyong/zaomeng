package com.kingpivot.base.product.service.impl;

import com.kingpivot.base.product.dao.ProductDao;
import com.kingpivot.base.product.model.Product;
import com.kingpivot.base.product.service.ProductService;
import com.kingpivot.common.dao.BaseDao;
import com.kingpivot.common.service.BaseServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service("productService")
public class ProductServiceImpl extends BaseServiceImpl<Product, String> implements ProductService {

    @Resource(name = "productDao")
    private ProductDao productDao;

    @Override
    public BaseDao<Product, String> getDAO() {
        return this.productDao;
    }
}

