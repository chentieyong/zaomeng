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
    public static final String JOBPOST_OBJECTDEFINEID = "00000000700f54dc017010575625000f";//职位需求对象定义id
    public static final String FRIENDNEED_OBJECTDEFIPOST = "00000000700f54dc01701061b0280011";//交友需求对象定义id
    public static final String PRODUCT_OBJECTDEFIPOST = "00000000700f54dc0170106db0c30013";//产品对象定义id
    public static final String GOODSCHANGE_OBJECTDEFIPOST = "0000000071155c49017115a6e7eb0002";//产品互换对象定义id
    public static final String BUYNEED_OBJECTDEFIPOST = "00000000700f54dc01701078ad270015";//产品求购对象定义id
    public static final String ALIPAY_GATEWAY = "https://openapi.alipay.com/gateway.do";//阿里云网关
    public static String QINIU_SECRETKEY = "Khq3bQQL_Bp6HwYAxiBqz2gferqtXdHs_Ts-m2I1";
    public static String QINIU_BUCKETNAME = "zaomeng";
    public static String QINIU_ACCESSKEY = "QaWF6IgWIeH5V-Kf9BOl825RvzFf2vWRnykN3V7A";
    public static String QINIU_LOOKHEAD = "http://qn.wgclm.com/";
    public static final String RECOMMAND_FULL_ERROR_CODE = "RECOMMAND_FULL_ERROR";//会员推荐异常语句code
    public static final String RECOMMAND_MEMBER_NAME = "推荐会员";//推荐会员name


    public static final String CAPITALPOST_POINT_USENAME = "资金投出发布";//资金投出发布-pointName
    public static final String CAPITALNEED_POINT_USENAME = "资金需求发布";//资金需求发布-pointName
    public static final String JOBNEED_POINT_USENAME = "职位求职发布";//职位求职发布-pointName
    public static final String JOBPOST_POINT_USENAME = "职位需求发布";//职位需求发布-pointName
    public static final String FRIENDNEED_POINT_USENAME = "交友需求发布";//交友需求发布-pointName
    public static final String PRODUCT_POINT_USENAME = "产品发布";//产品发布-pointName
    public static final String GOODSCHANGE_POINT_USENAME = "产品互换";//产品互换-pointName
    public static final String BUYNEED_POINT_USENAME = "产品求购发布";//产品求购发布-pointName

    public static String GAODE_WEBAPI_KEY = "193d69e4dbb74686e4bc949710a493b2";//高德key
    public static String TENXUNMAP_KEY = "X4LBZ-4AWCG-OA6Q7-IS3A6-CALX2-KFBED";//腾讯key
    public static String TENXUNMAP_URL = "http://apis.map.qq.com/ws/geocoder/v1/";//腾讯api_url
}
