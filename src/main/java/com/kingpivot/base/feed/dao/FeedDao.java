package com.kingpivot.base.feed.dao;

import com.kingpivot.base.feed.model.Feed;
import com.kingpivot.common.dao.BaseDao;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import javax.persistence.Table;

@Repository
@Table(name = "feed")
@Qualifier("feedDao")
public interface FeedDao extends BaseDao<Feed, String> {

}
