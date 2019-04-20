package com.kingpivot.base.memberShop.service.impl;

import com.kingpivot.base.memberShop.dao.MemberShopDao;
import com.kingpivot.base.memberShop.model.MemberShop;
import com.kingpivot.base.memberShop.service.MemberShopService;
import com.kingpivot.common.dao.BaseDao;
import com.kingpivot.common.service.BaseServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service("memberShopService")
public class MemberShopServiceImpl extends BaseServiceImpl<MemberShop, String> implements MemberShopService {

    @Resource(name = "memberShopDao")
    private MemberShopDao memberShopDao;

    @Override
    public BaseDao<MemberShop, String> getDAO() {
        return this.memberShopDao;
    }
}
