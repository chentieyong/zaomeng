package com.kingpivot.common.jms;

public interface SendMessageService {
    void sendMemberLogMessage(String msg);

    void sendMemberLoginMessage(String msg);

    void sendZmPaySuccessMessage(String msg);

    void getMemberBonusMessage(String msg);
}
