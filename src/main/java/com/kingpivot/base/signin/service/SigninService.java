package com.kingpivot.base.signin.service;

import com.kingpivot.base.signin.model.Signin;
import com.kingpivot.common.service.BaseService;

public interface SigninService extends BaseService<Signin, String> {
    String getTodaySigninID(String memberID);
}