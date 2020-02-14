package com.kingpivot.base.config;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Component
@Lazy(false)
public class Config {
    public static final String ENCODE_KEY = "kpbase";
    public static final String LOCAL_FILE_SERVER_BUCKET = "kingjs";
    public static final String GOODSSHOP_OBJECTDEFINEID = "422429993732";//店铺商品对象定义id
    public static final String MEMBER_OBJECTDEFINEID = "00000000700458b2017005302511000c";//会员对象定义id
    public static final String ARTICLE_OBJECTDEFINEID = "422429993731";//文章对象定义id
    public static final String MEMBERORDER_OBJECTDEFINEID = "8a2f462a591162d101591595925b1cc0";//订单对象定义id
    public static final String MESSAGE_OBJECTDEFINEID = "402892e96a5895ef016a5896b76b0002";//消息对象定义id
    public static final String STORY_OBJECTDEFINEID = "ff8080816fb1a833016fefb931cb00aa";//故事对象定义id
    public static final String CAPITALPOST_OBJECTDEFINEID = "00000000700f54dc0170104488620006";//资金投出(资源发布)对象定义id
    public static final String CAPITALNEED_OBJECTDEFINEID = "00000000700f54dc0170104c44d7000b";//资金需求对象定义id
    public static final String JOBNEED_OBJECTDEFINEID = "00000000700f54dc017010537833000d";//职位求职对象定义id
    public static final String JOBNEED_OBJECTDEFIPOST = "00000000700f54dc017010575625000f";//职位需求对象定义id
    public static final String FRIENDNEED_OBJECTDEFIPOST = "00000000700f54dc01701061b0280011";//交友需求对象定义id
    public static final String PRODUCT_OBJECTDEFIPOST = "00000000700f54dc0170106db0c30013";//产品对象定义id
    public static final String BUYNEED_OBJECTDEFIPOST = "00000000700f54dc01701078ad270015";//产品求购对象定义id
    public static final String ALIPAY_GATEWAY = "https://openapi.alipay.com/gateway.do";//阿里云网关
    public static String QINIU_SECRETKEY = "Khq3bQQL_Bp6HwYAxiBqz2gferqtXdHs_Ts-m2I1";
    public static String QINIU_BUCKETNAME = "zaomeng";
    public static String QINIU_ACCESSKEY = "QaWF6IgWIeH5V-Kf9BOl825RvzFf2vWRnykN3V7A";
    public static String QINIU_LOOKHEAD = "http://qn.wgclm.com/";
}
