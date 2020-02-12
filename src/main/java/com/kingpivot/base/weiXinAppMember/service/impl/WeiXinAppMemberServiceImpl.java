package com.kingpivot.base.weiXinAppMember.service.impl;

import com.kingpivot.base.weiXinAppMember.dao.WeiXinAppMemberDao;
import com.kingpivot.base.weiXinAppMember.model.WeiXinAppMember;
import com.kingpivot.base.weiXinAppMember.service.WeiXinAppMemberService;
import com.kingpivot.common.dao.BaseDao;
import com.kingpivot.common.service.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("weiXinAppMemberService")
public class WeiXinAppMemberServiceImpl extends BaseServiceImpl<WeiXinAppMember, String> implements WeiXinAppMemberService {

    @Autowired
    private WeiXinAppMemberDao weiXinAppMemberDao;

    @Override
    public BaseDao<WeiXinAppMember, String> getDAO() {
        return this.weiXinAppMemberDao;
    }

    @Override
    public WeiXinAppMember getWeiXinAppMemberByCode(String code) {
        return weiXinAppMemberDao.getWeiXinAppMemberByCode(code);
    }
}
