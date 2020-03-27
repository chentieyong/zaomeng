package com.kingpivot.base.raffle.service.impl;

import com.kingpivot.base.raffle.dao.RaffleDao;
import com.kingpivot.base.raffle.model.Raffle;
import com.kingpivot.base.raffle.service.RaffleService;
import com.kingpivot.common.dao.BaseDao;
import com.kingpivot.common.service.BaseServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service("raffleService")
public class RaffleServiceImpl extends BaseServiceImpl<Raffle, String> implements RaffleService {

    @Resource(name = "raffleDao")
    private RaffleDao raffleDao;

    @Override
    public BaseDao<Raffle, String> getDAO() {
        return this.raffleDao;
    }
}

