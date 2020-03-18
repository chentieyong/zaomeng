package com.kingpivot.base.memberAddress.service;

import com.kingpivot.base.memberAddress.model.MemberAddress;
import com.kingpivot.common.service.BaseService;

public interface MemberAddressService extends BaseService<MemberAddress, String> {
    void updateMemberAddressDefault(String memberID, String notEqMemberAddressID);

    int getMaxOrderSeq(String memberID);
}
