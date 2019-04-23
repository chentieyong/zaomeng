
package com.kingpivot.common.weixinPay;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * description: 微信支付回调
 */
@XStreamAlias("xml")
public class WxPayResult {
	private String appid;
	@XStreamAlias("bank_type")
	private String bankType;
	@XStreamAlias("cash_fee")
	private String cashFee;
	@XStreamAlias("fee_type")
	private String feeType;
	@XStreamAlias("is_subscribe")
	private String isSubscribe;
	@XStreamAlias("mch_id")
	private String mchId;
	@XStreamAlias("nonce_str")
	private String nonceStr;
	private String openid;
	@XStreamAlias("out_trade_no")
	private String outTradeNo;
	@XStreamAlias("result_code")
	private String resultCode;
	@XStreamAlias("return_code")
	private String returnCode;
	private String sign;
	@XStreamAlias("time_end")
	private String timeEnd;
	@XStreamAlias("total_fee")
	private String totalFee;
	@XStreamAlias("trade_type")
	private String tradeType;
	@XStreamAlias("transaction_id")
	private String transactionId;
	@XStreamAlias("coupon_count")
	private String coupon_count;
	@XStreamAlias("coupon_fee")
	private String coupon_fee;
	@XStreamAlias("coupon_fee_0")
	private String coupon_fee_0;
	@XStreamAlias("attach")
	private String attach;
	@XStreamAlias("coupon_id_0")
	private String coupon_id_0;
   
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -1227026039888867970L;
	
	/**
	 * @return the appid
	 */
	public String getAppid() {
		return appid;
	}
	/**
	 * @param appid the appid to set
	 */
	public void setAppid(String appid) {
		this.appid = appid;
	}
	/**
	 * @return the bankType
	 */
	public String getBankType() {
		return bankType;
	}
	/**
	 * @param bankType the bankType to set
	 */
	public void setBankType(String bankType) {
		this.bankType = bankType;
	}
	/**
	 * @return the cashFee
	 */
	public String getCashFee() {
		return cashFee;
	}
	/**
	 * @param cashFee the cashFee to set
	 */
	public void setCashFee(String cashFee) {
		this.cashFee = cashFee;
	}
	/**
	 * @return the feeType
	 */
	public String getFeeType() {
		return feeType;
	}
	/**
	 * @param feeType the feeType to set
	 */
	public void setFeeType(String feeType) {
		this.feeType = feeType;
	}
	/**
	 * @return the isSubscribe
	 */
	public String getIsSubscribe() {
		return isSubscribe;
	}
	/**
	 * @param isSubscribe the isSubscribe to set
	 */
	public void setIsSubscribe(String isSubscribe) {
		this.isSubscribe = isSubscribe;
	}
	/**
	 * @return the mchId
	 */
	public String getMchId() {
		return mchId;
	}
	/**
	 * @param mchId the mchId to set
	 */
	public void setMchId(String mchId) {
		this.mchId = mchId;
	}
	/**
	 * @return the nonceStr
	 */
	public String getNonceStr() {
		return nonceStr;
	}
	/**
	 * @param nonceStr the nonceStr to set
	 */
	public void setNonceStr(String nonceStr) {
		this.nonceStr = nonceStr;
	}
	/**
	 * @return the openid
	 */
	public String getOpenid() {
		return openid;
	}
	/**
	 * @param openid the openid to set
	 */
	public void setOpenid(String openid) {
		this.openid = openid;
	}
	/**
	 * @return the outTradeNo
	 */
	public String getOutTradeNo() {
		return outTradeNo;
	}
	/**
	 * @param outTradeNo the outTradeNo to set
	 */
	public void setOutTradeNo(String outTradeNo) {
		this.outTradeNo = outTradeNo;
	}
	/**
	 * @return the resultCode
	 */
	public String getResultCode() {
		return resultCode;
	}
	/**
	 * @param resultCode the resultCode to set
	 */
	public void setResultCode(String resultCode) {
		this.resultCode = resultCode;
	}
	/**
	 * @return the returnCode
	 */
	public String getReturnCode() {
		return returnCode;
	}
	/**
	 * @param returnCode the returnCode to set
	 */
	public void setReturnCode(String returnCode) {
		this.returnCode = returnCode;
	}
	/**
	 * @return the sign
	 */
	public String getSign() {
		return sign;
	}
	/**
	 * @param sign the sign to set
	 */
	public void setSign(String sign) {
		this.sign = sign;
	}
	/**
	 * @return the timeEnd
	 */
	public String getTimeEnd() {
		return timeEnd;
	}
	/**
	 * @param timeEnd the timeEnd to set
	 */
	public void setTimeEnd(String timeEnd) {
		this.timeEnd = timeEnd;
	}
	/**
	 * @return the totalFee
	 */
	public String getTotalFee() {
		return totalFee;
	}
	/**
	 * @param totalFee the totalFee to set
	 */
	public void setTotalFee(String totalFee) {
		this.totalFee = totalFee;
	}
	/**
	 * @return the tradeType
	 */
	public String getTradeType() {
		return tradeType;
	}
	/**
	 * @param tradeType the tradeType to set
	 */
	public void setTradeType(String tradeType) {
		this.tradeType = tradeType;
	}
	/**
	 * @return the transactionId
	 */
	public String getTransactionId() {
		return transactionId;
	}
	/**
	 * @param transactionId the transactionId to set
	 */
	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}

	public String getCoupon_count() {
		return coupon_count;
	}

	public void setCoupon_count(String coupon_count) {
		this.coupon_count = coupon_count;
	}

	public String getCoupon_fee() {
		return coupon_fee;
	}

	public void setCoupon_fee(String coupon_fee) {
		this.coupon_fee = coupon_fee;
	}

	public String getAttach() {
		return attach;
	}

	public void setAttach(String attach) {
		this.attach = attach;
	}

	public String getCoupon_fee_0() {
		return coupon_fee_0;
	}

	public void setCoupon_fee_0(String coupon_fee_0) {
		this.coupon_fee_0 = coupon_fee_0;
	}

	public String getCoupon_id_0() {
		return coupon_id_0;
	}

	public void setCoupon_id_0(String coupon_id_0) {
		this.coupon_id_0 = coupon_id_0;
	}
}