package com.kingpivot.base.memberInvoiceDefine.service.impl;

import com.kingpivot.base.memberInvoiceDefine.dao.MemberInvoiceDefineDao;
import com.kingpivot.base.memberInvoiceDefine.model.MemberInvoiceDefine;
import com.kingpivot.base.memberInvoiceDefine.service.MemberInvoiceDefineService;
import com.kingpivot.common.dao.BaseDao;
import com.kingpivot.common.service.BaseServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service("memberInvoiceDefineService")
public class MemberInvoiceDefineServiceImpl extends BaseServiceImpl<MemberInvoiceDefine, String> implements MemberInvoiceDefineService {
    @Resource(name = "memberInvoiceDefineDao")
    private MemberInvoiceDefineDao memberInvoiceDefineDao;

    @Override
    public BaseDao<MemberInvoiceDefine, String> getDAO() {
        return this.memberInvoiceDefineDao;
    }

}
