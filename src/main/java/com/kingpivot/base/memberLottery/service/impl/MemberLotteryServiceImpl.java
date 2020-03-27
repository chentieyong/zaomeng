package com.kingpivot.base.memberLottery.service.impl;

import com.kingpivot.base.memberLottery.dao.MemberLotteryDao;
import com.kingpivot.base.memberLottery.model.MemberLottery;
import com.kingpivot.base.memberLottery.service.MemberLotteryService;
import com.kingpivot.common.dao.BaseDao;
import com.kingpivot.common.service.BaseServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.sql.Timestamp;

@Service("memberLotteryService")
public class MemberLotteryServiceImpl extends BaseServiceImpl<MemberLottery, String> implements MemberLotteryService {

    @Resource(name = "memberLotteryDao")
    private MemberLotteryDao memberLotteryDao;

    @Override
    public BaseDao<MemberLottery, String> getDAO() {
        return this.memberLotteryDao;
    }

    @Override
    public Timestamp getJoinTimeByLotteryIDAndMemberID(String lotteryID, String memberID) {
        return memberLotteryDao.getJoinTimeByLotteryIDAndMemberID(lotteryID, memberID);
    }
}

