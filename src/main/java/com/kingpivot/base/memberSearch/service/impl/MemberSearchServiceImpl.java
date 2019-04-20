package com.kingpivot.base.memberSearch.service.impl;

import com.kingpivot.base.memberSearch.dao.MemberSearchDao;
import com.kingpivot.base.memberSearch.model.MemberSearch;
import com.kingpivot.base.memberSearch.service.MemberSearchService;
import com.kingpivot.common.dao.BaseDao;
import com.kingpivot.common.service.BaseServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service("memberSearchService")
public class MemberSearchServiceImpl extends BaseServiceImpl<MemberSearch, String> implements MemberSearchService {
    @Resource(name = "memberSearchDao")
    private MemberSearchDao memberSearchDao;

    @Override
    public BaseDao<MemberSearch, String> getDAO() {
        return this.memberSearchDao;
    }
}
