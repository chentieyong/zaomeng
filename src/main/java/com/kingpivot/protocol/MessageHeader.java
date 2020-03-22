package com.kingpivot.protocol;

import java.io.Serializable;

public class MessageHeader implements Serializable {

    private static final long serialVersionUID = 9035153501525586531L;

    private int code = Code.success.code;

    private String msg;

    public MessageHeader() {

    }

    public MessageHeader(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public static MessageHeader success() {
        return new MessageHeader(Code.success.code, "success");
    }

    public static MessageHeader success(String msg) {
        return new MessageHeader(Code.success.code, msg);
    }

    public static MessageHeader fail() {
        return new MessageHeader(Code.fail.code, "failed");
    }

    public static MessageHeader fail(String msg) {
        return new MessageHeader(Code.fail.code, msg);
    }

    public static MessageHeader fail(Code code) {
        return new MessageHeader(code.code, "failed");
    }

    public static MessageHeader fail(Code code, String msg) {
        return new MessageHeader(code.code, msg);
    }

    public static MessageHeader fail(int code, String msg) {
        return new MessageHeader(code, msg);
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    @Override
    public String toString() {
        return String.format("MessageHeader [code=%s, msg=%s]", code, msg);
    }

    public static enum Code {
        serverException(88888),//服务器异常
        success(0), //成功 除此之外全是失败
        fail(-1),//失败
        weixinAppMemberNull(-2),//微信app会员不存在
        unauth(10000),//未登录
        illegalParameter(10001),//参数不正确
        loginTypeIsNull(10002),//登录类型不能为空
        loginTypeIsError(10003),//登录类型不正确
        loginNameIsNull(10004),//登录名为空
        passwordIsNull(10004),//密码为空
        loginNameIsError(10005),//登录名异常
        passwordError(10006),//密码异常
        memberIsLock(10007),//会员被锁定
        siteIdIsNull(10008),//站点id为空
        siteIdError(10009),//站点id不正确
        deviceIdIsNull(10010),//设备id为空
        vCodeIsNull(10011),//验证码为空
        vCodeError(10012),//验证码错误
        phoneIsNull(10013),//手机号为空
        phoneIsUsed(10014),//手机号已使用
        sendTypeIsNull(10015),//发送类型为空
        focusIDIsNull(10016),//轮播id为空
        goodsCategoryIDIsNull(10017),//商品分类id为空
        goodsShopIdIsNull(10018),//店铺商品id为空
        goodsShopIdIsError(10019),//店铺商品id为空
        objectIdIsNull(10020),//对象id为空
        cartGoodsIDIsNull(10021),//购物车商品id为空
        cartGoodsIDIsError(10022),//购物车商品id不正确
        qtyIsNull(10023),//数量为空
        addressIsNull(10024),//地址为空
        contactPhoneIsNull(10025),//地址为空
        contactNameIsNull(10026),//地址为空
        memberShopIDIsNull(10027),//会员店铺id为空
        memberShopIDIsError(10028),//会员店铺id不正确
        navigatorIDIsNull(10029),//导航id为空
        objectDefineIDIsNull(10029),//对象定义id为空
        memberOrderIDIsNull(10030),//会员订单id为空
        memberOrderIDIsError(10031),//会员订单id不正确
        nameIsNull(10031),//名称不能为空
        shopCategoryIDIsNull(10032),//店铺分类id不能为空
        shopCategoryIDIsError(10033),//店铺分类id不正确
        shopFaceImageIsNull(10034),//名称不能为空
        businessImageIsNull(10035),//营业执照不能为空
        contactIsNull(10036),//联系人不能为空
        contactIdCardFaceImageIsNull(10037),//联系人身份证正面照不能为空
        contactIdCardBackImageIsNull(10038),//联系人身份证反面照不能为空
        articleIDIsNull(10039),//文章id为空
        articleIDIsError(10040),///文章id不正确
        objectIDNotNull(10041),//对象id不能为空
        objectFeatureItemID1IsNull(10042),//objectFeatureItemID1不能为空
        objectNameIsNull(10043),//对象名称不能为空
        collectIDIsNull(10044),//收藏id不能为空
        collectIDIsError(10045),//收藏id不能为空
        memberIDIsNull(10046),//收藏id不能为空
        contantIsNull(10047),//内容不能为空
        memberBonusIdIsNull(10048),//会员红包id不能为空
        memberBonusIdIsError(10049),//会员红包id不正确
        memberbonusIsUsed(10050),//会员红包已使用
        memberbonusIsTimeOut(10051),//会员红包已过期
        wecharterror(10052),
        sendError(10053),//发送失败
        paywayIDNotNull(10054),//支付机构ID不能为空
        paywayIDIsError(10055),//支付机构ID不正确
        amountNotNull(10056),//公司付款账户不能为空
        appTypeIsNull(10057),//appType不能为空
        memberIsCollect(10058),//会员已收藏
        messageTypeIsNull(10059),//消息类型不能为空
        applicationIdIsNull(10060),//应用id为空
        applicationIdIsError(10061),//应用不存在
        smsWayIdIsNull(10062),//短信通道id为空
        smsWayIdIsError(10063),//短信通道id不正确
        sendTypeError(10064),//发送类型不正确
        smsTemplateIsNull(10065),//模板不存在
        statusIsError(10067),//状态不正确
        memberOrderGoodsIDIsNull(10067),//订单商品id不存在
        memberOrderGoodsIDIsError(10068),//订单商品id不正确
        bonusIDIsNull(10069),//红包id为空
        bonusIDIsError(10071),//红包id不正确
        phoneIsError(10072),//手机号不存在
        codeIsNull(10073),//code为空
        messageIdIsNull(10074),//消息id为空
        messageIdIsError(10075),//消息id不正确
        memberOrderGoodsReturnIDIsNull(10076),//会员订单商品退货id为空
        memberOrderGoodsReturnIDIsError(10077),//会员订单商品退货id不正确
        weixinurlIsEmpty(10078),
        publicNoIsEmpty(10079),
        publicNoIsError(10080),
        storyIdIsNull(10081),//故事id为空
        storyIdIsError(10082),//故事不存在
        praiseIDIsNull(10083),//赞id为空
        praiseIDIsError(10084),//赞不存在
        weixinFormID(10085),//获取openid异常，请联系管理员
        pageIsNull(10086),//page为空
        sceneIsNull(10087),//scene为空
        publicNoIsNull(10088),//publicNo为空
        majorIDIsNull(10089),//majorIDIsNull为空
        memberMajorIDIsNull(10090),//memberMajorID为空
        memberMajorIDIsError(10091),//memberMajorID不正确
        majorIsApply(10092),//专业已申请
        capitalNeedIDIsNull(10093),//资金需求id为空
        capitalNeedIDIsError(10094),//资金需求不存在
        jobNeedIDIsNull(10095),//职位求职id为空
        jobNeedIDIsError(10096),//职位求职不存在
        friendNeedIDIsNull(10097),//交友需求id为空
        friendNeedIDIsError(10098),//交友需求不存在
        capitalPostIDIsNull(10099),//资金需求id为空
        capitalPostIDIsError(10110),//资金需求不存在
        jobPostIDIsNull(10111),//职位求职id为空
        jobPostIDIsError(10112),//职位需求不存在
        productIDIsNull(10113),//产品id为空
        productIDIsError(10114),//产品不存在
        buyNeedIDIsNull(10115),//产品求购id为空
        buyNeedIDIsError(10116),//产品求购不存在
        peopleIDIsNull(10117),//实名id为空
        peopleIDIsError(10118),//实名id不存在
        idNumberIsNull(10119),//身份证号为空
        faceImageIsNull(10120),//正面照为空
        backImageIsNull(10121),//反面照为空
        majorIsFull(10122),//专业申请已满
        shopListIsNull(10123),//店铺为空
        shopIDIsNull(10124),//店铺id为空
        shopIDIsError(10125),//店铺不存在
        shortNameIsNull(10126),//简称不能为空
        memberAddressIDIsNull(10127),//会员地址id为空
        memberAddressIDIsError(10128),//会员地址不存在
        invoiceTypeIsNull(10129),//发票类型不能为空
        taxTypeIsNull(10130),//发票税种不能为空
        taxCodeIsNull(10131),//纳税人识别号不能为空
        memberInvoiceDefineIDIsNull(10132),//会员发票id为空
        memberInvoiceDefineIDIsError(10133),//会员发票不存在
        pointNumberLess(10134),//积分个数不足
        ;
        public int code;

        Code(int code) {
            this.code = code;
        }

    }

}
