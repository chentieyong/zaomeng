package com.kingpivot.base.lotteryGrade.service;

import com.kingpivot.base.lotteryGrade.model.LotteryGrade;
import com.kingpivot.common.service.BaseService;

import java.util.List;

public interface LotteryGradeService extends BaseService<LotteryGrade, String> {

    List<LotteryGrade> getLotteryGradeByLotteryID(String lotteryID);

}
