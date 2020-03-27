package com.kingpivot.base.lottery.service.impl;

import com.kingpivot.base.lottery.dao.LotteryDao;
import com.kingpivot.base.lottery.model.Lottery;
import com.kingpivot.base.lottery.service.LotteryService;
import com.kingpivot.common.dao.BaseDao;
import com.kingpivot.common.service.BaseServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service("lotteryService")
public class LotteryServiceImpl extends BaseServiceImpl<Lottery, String> implements LotteryService {

    @Resource(name = "lotteryDao")
    private LotteryDao lotteryDao;

    @Override
    public BaseDao<Lottery, String> getDAO() {
        return this.lotteryDao;
    }
}

