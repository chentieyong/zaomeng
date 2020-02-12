package com.kingpivot.base.weiXinAppMember.dao;

import com.kingpivot.base.weiXinAppMember.model.WeiXinAppMember;
import com.kingpivot.common.dao.BaseDao;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.persistence.Table;

@Repository
@Table(name = "weiXinAppMember")
@Qualifier("weiXinAppMemberDao")
public interface WeiXinAppMemberDao extends BaseDao<WeiXinAppMember, String> {

    @Query(value = "select * from weiXinAppMember where code=?1 and isValid=1 and isLock=0", nativeQuery = true)
    WeiXinAppMember getWeiXinAppMemberByCode(String code);
}
