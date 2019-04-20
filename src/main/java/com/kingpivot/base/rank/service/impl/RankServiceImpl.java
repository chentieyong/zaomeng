package com.kingpivot.base.rank.service.impl;

import com.kingpivot.base.rank.dao.RankDao;
import com.kingpivot.base.rank.model.Rank;
import com.kingpivot.base.rank.service.RankService;
import com.kingpivot.common.dao.BaseDao;
import com.kingpivot.common.service.BaseServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service("rankService")
public class RankServiceImpl extends BaseServiceImpl<Rank, String> implements RankService {

    @Resource(name = "rankDao")
    private RankDao rankDao;

    @Override
    public BaseDao<Rank, String> getDAO() {
        return this.rankDao;
    }
}
