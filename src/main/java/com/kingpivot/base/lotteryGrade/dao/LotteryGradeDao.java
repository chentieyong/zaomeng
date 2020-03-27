package com.kingpivot.base.lotteryGrade.dao;

import com.kingpivot.base.lotteryGrade.model.LotteryGrade;
import com.kingpivot.common.dao.BaseDao;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.persistence.Table;
import java.util.List;

@Repository
@Table(name = "lotteryGrade")
@Qualifier("lotteryGradeDao")
public interface LotteryGradeDao extends BaseDao<LotteryGrade, String> {

    @Query(value = "SELECT * FROM lotteryGrade WHERE lotteryID=?1 AND isValid=1 AND isLock=0 order by orderSeq asc", nativeQuery = true)
    List<LotteryGrade> getLotteryGradeByLotteryID(String lotteryID);
}