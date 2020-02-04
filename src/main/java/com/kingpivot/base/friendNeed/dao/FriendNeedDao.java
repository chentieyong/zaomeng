package com.kingpivot.base.friendNeed.dao;

import com.kingpivot.base.friendNeed.model.FriendNeed;
import com.kingpivot.common.dao.BaseDao;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import javax.persistence.Table;

@Repository
@Table(name = "friendNeed")
@Qualifier("friendNeedDao")
public interface FriendNeedDao extends BaseDao<FriendNeed, String> {

}
