package com.kingpivot.common.jms.dto.memberLog;

public class MemberLogBuilder extends MemberLogBaseBuilder<MemberLogBuilder, MemberLogRequestBase> {
    @Override
    public MemberLogRequestBase build() {
        MemberLogRequestBase responseBase = new MemberLogRequestBase();
        setBalance(responseBase);
        return responseBase;
    }
}
