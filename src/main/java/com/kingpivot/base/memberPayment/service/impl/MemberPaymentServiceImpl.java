package com.kingpivot.base.memberPayment.service.impl;

import com.kingpivot.base.memberPayment.dao.MemberPaymentDao;
import com.kingpivot.base.memberPayment.model.MemberPayment;
import com.kingpivot.base.memberPayment.service.MemberPaymentService;
import com.kingpivot.common.dao.BaseDao;
import com.kingpivot.common.service.BaseServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service("memberPaymentService")
public class MemberPaymentServiceImpl extends BaseServiceImpl<MemberPayment, String> implements MemberPaymentService {
    @Resource(name = "memberPaymentDao")
    private MemberPaymentDao memberPaymentDao;

    @Override
    public BaseDao<MemberPayment, String> getDAO() {
        return this.memberPaymentDao;
    }
}
