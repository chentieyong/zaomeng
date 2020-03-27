package com.kingpivot.base.memberLottery.service;

import com.kingpivot.base.memberLottery.model.MemberLottery;
import com.kingpivot.common.service.BaseService;

import java.sql.Timestamp;

public interface MemberLotteryService extends BaseService<MemberLottery, String> {

    Timestamp getJoinTimeByLotteryIDAndMemberID(String lotteryID, String memberID);
}
