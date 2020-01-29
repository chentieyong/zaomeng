package com.kingpivot.base.story.service.impl;

import com.kingpivot.base.story.dao.StoryDao;
import com.kingpivot.base.story.model.Story;
import com.kingpivot.base.story.service.StoryService;
import com.kingpivot.common.dao.BaseDao;
import com.kingpivot.common.service.BaseServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service("storyService")
public class StoryServiceImpl extends BaseServiceImpl<Story, String> implements StoryService {

    @Resource(name = "storyDao")
    private StoryDao storyDao;

    @Override
    public BaseDao<Story, String> getDAO() {
        return this.storyDao;
    }
}
