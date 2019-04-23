package com.kingpivot.base.signin.dao;

import com.kingpivot.base.signin.model.Signin;
import com.kingpivot.common.dao.BaseDao;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.persistence.Table;

@Repository
@Table(name = "signin")
@Qualifier("signinDao")
public interface SigninDao extends BaseDao<Signin, String> {

    @Query(value = "SELECT id FROM signin WHERE memberID=?1 \n" +
            "AND DATE_FORMAT(createdTime,'%Y-%m-%d')=DATE_FORMAT(NOW(),'%Y-%m-%d')\n" +
            "AND isValid=1 AND isLock=0 limit 1",nativeQuery = true)
    String getTodaySigninID(String memberID);
}