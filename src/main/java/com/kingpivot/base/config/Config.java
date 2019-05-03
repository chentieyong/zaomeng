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
    public static final String MESSAGE_OBJECTDEFINEID = "402892e96a5895ef016a5896b76b0002";//消息对象定义id
    public static final String ALIPAY_GATEWAY = "https://openapi.alipay.com/gateway.do";//阿里云网关
    public static String QINIU_SECRETKEY = "ZovEAZW_9kX6-9SZm3__u2VbzkYUU6gU8Hv7DQjR";
    public static String QINIU_BUCKETNAME = "sleep";
    public static String QINIU_ACCESSKEY = "UbTtaC6dRMAUMnmx7cpZRmBiYrJa2-IkJ0bQMxQW";
    public static String QINIU_LOOKHEAD = "http://wj.haoju.me/";
}
