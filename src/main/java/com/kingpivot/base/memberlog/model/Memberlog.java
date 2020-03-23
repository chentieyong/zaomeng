package com.kingpivot.base.memberlog.model;

import com.kingpivot.common.model.BaseModel;

import java.sql.Timestamp;

public class Memberlog extends BaseModel<String> {
    private static final long serialVersionUID = 8881439443364101571L;
    private String id;//主键
    private String memberID;
    private String siteID;
    private Timestamp operateTime;
    private String description;
    private String browserType;
    private String operateType;
    private String locationID;
    private String deviceID;
    private String versionID;//版本ID

    @Override
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMemberID() {
        return memberID;
    }

    public void setMemberID(String memberID) {
        this.memberID = memberID;
    }

    public String getSiteID() {
        return siteID;
    }

    public void setSiteID(String siteID) {
        this.siteID = siteID;
    }

    public Timestamp getOperateTime() {
        return operateTime;
    }

    public void setOperateTime(Timestamp operateTime) {
        this.operateTime = operateTime;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getBrowserType() {
        return browserType;
    }

    public void setBrowserType(String browserType) {
        this.browserType = browserType;
    }

    public String getOperateType() {
        return operateType;
    }

    public void setOperateType(String operateType) {
        this.operateType = operateType;
    }

    public String getLocationID() {
        return locationID;
    }

    public void setLocationID(String locationID) {
        this.locationID = locationID;
    }

    public String getDeviceID() {
        return deviceID;
    }

    public void setDeviceID(String deviceID) {
        this.deviceID = deviceID;
    }

    public String getVersionID() {
        return versionID;
    }

    public void setVersionID(String versionID) {
        this.versionID = versionID;
    }

    public enum MemberOperateType {
        MEMBER_LOGIN("会员登录"),GETFOCUSPICTURELIST("轮播列表"),ADDGOODSSHOPTOCART("店铺商品加入购物车"),UPDATECARTGOODSNUMBER("更改购物车商品数量"),
        REMOVECARTGOODS("删除购物车商品"),GETCARTGOODSLIST("获取购物车商品列表"),CREATEMEMBERORDER("店铺商品生成订单"),SELECTCARTGOODS("勾选购物车商品"),
        APPLYMEMBERSHOP("申请会员店铺"),UPDATEMEMBERSHOP("修改会员店铺"),GETMEMBERORDERLIST("获取会员订单列表"),GETMEMBERORDERDETAIL("获取会员订单详情"),
        GETMEMBERSHOPLIST("获取会员店铺列表"),GETMEMBERSHOPDETAIL("获取会员店铺详情"),REMOVEMEMBERSHOP("删除会员店铺"),GETCOLLECTLIST("获取收藏列表"),
        REMOVECOLLECT("删除收藏"),SUBMITONEFEE("提交一个意见反馈"),GETMYMEMBERBONUSLIST("获取我的会员红包列表"),USEMEMBERBONUS("使用会员红包"),
        APPAPPLYMEMBERORDERPAY("APP申请订单支付"),SUBMITONESIGNIN("会员签到"),GETSIGNINLIST("获取签到记录"),GETMYPOINTLIST("获取我的积分列表"),
        GETMYMESSAGELIST("获取我的消息列表"),GETMESSAGEDETAIL("获取消息详情"),CANCELMEMBERORDER("取消订单"),CONFIRMMEMBERORDER("确认收货"),
        GETCANGETBONUSDEFINELIST("获取可领红包定义列表"),MEMBERGETBONUS("会员领红包"),UPDATEMEMBERINFO("修改会员信息"),UPDATELOGINPASSWORD("修改登录密码")
        ,APPLYRETURNMEMBERORDER("申请订单退单"),GETMEMBERRANKINFO("获取会员等级信息"),GETMEMBERINFO("获取会员信息"),READONEMESSAGE("阅读一个消息"),
        GETNOREADMESSAGENUM("获取未阅读消息数量"),GETMEMBERSTATISTICS("获取会员统计信息"),MEMBERSIGNININIT("会员签到初始化"),SUBMITONESTORY("提交一个故事")
        ,GETMEMBERORDERGOODSRETURNLIST("获取会员订单商品退货列表"),GETMEMBERORDERGOODSRETURNDETAIL("获取会员订单商品退货详情"),UPDATEONESTORY("修改一个故事")
        ,DELETEONESTORY("删除一个故事"),ADDCOLLECT("加入收藏"),ADDPRAISE("加入赞"),REMOVEPRAISE("删除赞"),GETPRAISELIST("获取会员赞列表")
        ,GETOBJECTPRAISELIST("获取对象赞列表"),GETSTORYLIST("获取故事列表"),GETSTORYDETAIL("获取故事详情"),GETMYCHILDRENMEMBERLIST("获取我的下级会员列表"),
        GETMAJORLIST("获取专业列表"),APPLYONEMAJOR("申请一个专业身份"),UPDATEONEMEMBERMAJOR("修改一个会员专业"),DELETEONEMEMBERMAJOR("删除一个会员专业"),
        CANCELMEMBERRECOMMAND("取消会员推荐关系"),SUBMITONECAPITALPOST("提交一个资金投出"),SUBMITONECAPITALNEED("提交一个资金需求"),SUBMITONEJOBNEED("职位求职")
        ,SUBMITONEJOBPOST("提交一个职位需求"),SUBMITONEFRIENDNEED("提交一个交友需求"),SUBMITONEPRODUCT("提交一个产品"),SUBMITONEPBUYNEED("提交一个产品求购"),
        SUBMITREALNAME("提交一个会员实名"),GETMEMBERCARTGOODSNUM("获取会员购物车商品数量"),SUBMITONEMEMBERADDRESS("提交一个会员地址"),UPDATEONEMEMBERADDRESS("修改一个会员地址"),
        REMOVEONEMEMBERADDRESS("删除会员地址"),GETMEMBERADDRESSLIST("获取会员地址列表"),GETMEMBERADDRESSDETAIL("获取会员地址详情"),APPLYMEMBERORDERGOODSRETURN("申请订单商品退货"),
        SUBMITONEMEMBERINVOICEDEFINE("创建会员发票"),UPDATEONEMEMBERINVOICEDEFINE("修改会员发票"),REMOVEONEMEMBERINVOICEDEFINE("删除会员发票")
        ,GETMEMBERINVOICEDEFINELIST("获取会员发票列表"),GETMEMBERINVOICEDEFINEDETAIL("获取会员发票详情"),GETUSEPOINTNUMBER("获取使用积分个数");
        private String oname;

        MemberOperateType(String oname) {
            this.oname = oname;
        }

        public String getOname() {
            return this.oname;
        }
    }
}