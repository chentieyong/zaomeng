package com.kingpivot.base.config;


import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Component
@Lazy(false)
public class Config {
    public static final String ENCODE_KEY = "kpbase";
    public static final String GOODSSHOP_OBJECTDEFINEID = "422429993732";//店铺商品对象定义id
    public static final String ARTICLE_OBJECTDEFINEID = "422429993731";//文章对象定义id
    public static final String MEMBERORDER_OBJECTDEFINEID = "8a2f462a591162d101591595925b1cc0";//订单对象定义id
    public static final String ALIPAY_GATEWAY = "https://openapi.alipay.com/gateway.do";//阿里云网关
}
