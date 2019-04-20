package com.kingpivot.base.signin.service.impl;

import com.kingpivot.base.signin.dao.SigninDao;
import com.kingpivot.base.signin.model.Signin;
import com.kingpivot.base.signin.service.SigninService;
import com.kingpivot.common.dao.BaseDao;
import com.kingpivot.common.service.BaseServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service("signinService")
public class SigninServiceImpl extends BaseServiceImpl<Signin, String> implements SigninService {

    @Resource(name = "signinDao")
    private SigninDao signinDao;

    @Override
    public BaseDao<Signin, String> getDAO() {
        return this.signinDao;
    }
}