package com.kingpivot.base.memberWish.dao;

import com.kingpivot.base.memberWish.model.MemberWish;
import com.kingpivot.common.dao.BaseDao;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import javax.persistence.Table;

@Repository
@Table(name = "memberWish")
@Qualifier("memberWishDao")
public interface MemberWishDao extends BaseDao<MemberWish, String> {

}