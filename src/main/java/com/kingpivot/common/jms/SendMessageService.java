package com.kingpivot.common.jms;

public interface SendMessageService {
    void sendMemberLogMessage(String msg);

    void sendMemberLoginMessage(String msg);

    void sendZmPaySuccessMessage(String msg);

    void sendZmGetMemberBonusMessage(String msg);
}
