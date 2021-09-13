package com.kingpivot.base.memberWish.service.impl;

import com.kingpivot.base.memberWish.dao.MemberWishDao;
import com.kingpivot.base.memberWish.model.MemberWish;
import com.kingpivot.base.memberWish.service.MemberWishService;
import com.kingpivot.common.dao.BaseDao;
import com.kingpivot.common.service.BaseServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service("memberWishService")
public class MemberWishServiceImpl extends BaseServiceImpl<MemberWish, String> implements MemberWishService {

    @Resource(name = "memberWishDao")
    private MemberWishDao memberWishDao;

    @Override
    public BaseDao<MemberWish, String> getDAO() {
        return this.memberWishDao;
    }
}

