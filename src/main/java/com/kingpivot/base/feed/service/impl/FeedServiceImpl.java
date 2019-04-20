package com.kingpivot.base.feed.service.impl;

import com.kingpivot.base.feed.dao.FeedDao;
import com.kingpivot.base.feed.model.Feed;
import com.kingpivot.base.feed.service.FeedService;
import com.kingpivot.common.dao.BaseDao;
import com.kingpivot.common.service.BaseServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service("feedService")
public class FeedServiceImpl extends BaseServiceImpl<Feed, String> implements FeedService {

    @Resource(name = "feedDao")
    private FeedDao feedDao;

    @Override
    public BaseDao<Feed, String> getDAO() {
        return this.feedDao;
    }
}
