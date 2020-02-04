package com.kingpivot.base.friendNeed.service.impl;

import com.kingpivot.base.friendNeed.dao.FriendNeedDao;
import com.kingpivot.base.friendNeed.model.FriendNeed;
import com.kingpivot.base.friendNeed.service.FriendNeedService;
import com.kingpivot.common.dao.BaseDao;
import com.kingpivot.common.service.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("friendNeedService")
public class FriendNeedServiceImpl extends BaseServiceImpl<FriendNeed, String> implements FriendNeedService {
    @Autowired
    private FriendNeedDao friendNeedDao;

    @Override
    public BaseDao<FriendNeed, String> getDAO() {
        return this.friendNeedDao;
    }
}
