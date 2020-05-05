package com.kingpivot.common.jms;

public interface SendMessageService {
    /**
     * 发送会员日志消息
     *
     * @param msg
     */
    void sendMemberLogMessage(String msg);

    /**
     * 发送会员登录消息
     *
     * @param msg
     */
    void sendMemberLoginMessage(String msg);

    /**
     * 早盟支付成功消息
     *
     * @param msg
     */
    void sendZmPaySuccessMessage(String msg);

    /**
     * 领红包消息
     *
     * @param msg
     */
    void getMemberBonusMessage(String msg);

    /**
     * 新增附件消息
     *
     * @param msg
     */
    void sendAddAttachmentMessage(String msg);

    /**
     * 新增message
     *
     * @param msg
     */
    void sendMessage(String msg);

    /**
     * 会员资金消息
     *
     * @param msg
     */
    void sendMemberBalance(String msg);

    /**
     * 消耗积分消息
     *
     * @param msg
     */
    void sendUsePointMessage(String msg);

    /**
     * 获取积分消息
     *
     * @param msg
     */
    void sendGetPointMessage(String msg);

    /**
     * 湖北商城购物分润消息
     *
     * @param msg
     */
    void sendHbShopBuyGoodsMessage(String msg);
}
