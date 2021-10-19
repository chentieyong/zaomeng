package com.kingpivot.base.discuss.service.impl;

import com.kingpivot.base.discuss.dao.DiscussDao;
import com.kingpivot.base.discuss.model.Discuss;
import com.kingpivot.base.discuss.service.DiscussService;
import com.kingpivot.common.dao.BaseDao;
import com.kingpivot.common.service.BaseServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service("discussService")
public class DiscussServiceImpl extends BaseServiceImpl<Discuss, String> implements DiscussService {
    @Resource(name = "discussDao")
    private DiscussDao discussDao;

    @Override
    public BaseDao<Discuss, String> getDAO() {
        return this.discussDao;
    }
}
