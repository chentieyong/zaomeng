package com.kingpivot.base.memberInvoiceDefine.service;

import com.kingpivot.base.memberInvoiceDefine.model.MemberInvoiceDefine;
import com.kingpivot.common.service.BaseService;

public interface MemberInvoiceDefineService extends BaseService<MemberInvoiceDefine, String> {

    void updateMemberInvoiceDefineDefault(String memberID, String notEqMemberInvoiceDefineID);

    Integer getMaxOrderSeq(String memberID);
}
