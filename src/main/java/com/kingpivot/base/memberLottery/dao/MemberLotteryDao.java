package com.kingpivot.base.memberLottery.dao;

import com.kingpivot.base.memberLottery.model.MemberLottery;
import com.kingpivot.common.dao.BaseDao;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.persistence.Table;
import java.sql.Timestamp;

@Repository
@Table(name = "memberLottery")
@Qualifier("memberLotteryDao")
public interface MemberLotteryDao extends BaseDao<MemberLottery, String> {

    @Query(value = "SELECT joinTime FROM memberLottery WHERE lotteryID=?1 AND memberID=?2 AND isValid=1 AND isLock=0", nativeQuery = true)
    Timestamp getJoinTimeByLotteryIDAndMemberID(String lotteryID, String memberID);
}