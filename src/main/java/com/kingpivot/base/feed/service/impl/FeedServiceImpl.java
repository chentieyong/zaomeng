package com.kingpivot.base.feed.service.impl;

import com.kingpivot.base.feed.dao.FeedDao;
import com.kingpivot.base.feed.model.Feed;
import com.kingpivot.base.feed.service.FeedService;
import com.kingpivot.common.dao.BaseDAO;
import com.kingpivot.common.service.impl.BaseService4HImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service("feedService")
public class FeedServiceImpl extends BaseService4HImpl<Feed, String> implements FeedService {

    @Resource(name = "feedDao")
    private FeedDao feedDao;

    @Override
    public BaseDAO<Feed, String> getDAO() {
        return this.feedDao;
    }
}
