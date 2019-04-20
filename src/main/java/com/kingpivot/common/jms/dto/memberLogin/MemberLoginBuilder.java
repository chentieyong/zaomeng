package com.kingpivot.common.jms.dto.memberLogin;

public class MemberLoginBuilder extends MemberLoginBaseBuilder<MemberLoginBuilder, MemberLoginRequestBase> {
    @Override
    public MemberLoginRequestBase build() {
        MemberLoginRequestBase responseBase = new MemberLoginRequestBase();
        setBalance(responseBase);
        return responseBase;
    }
}
