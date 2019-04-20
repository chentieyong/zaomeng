package com.kingpivot.base.signin.dao;

import com.kingpivot.base.signin.model.Signin;
import com.kingpivot.common.dao.BaseDao;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import javax.persistence.Table;

@Repository
@Table(name = "signin")
@Qualifier("signinDao")
public interface SigninDao extends BaseDao<Signin, String> {
}