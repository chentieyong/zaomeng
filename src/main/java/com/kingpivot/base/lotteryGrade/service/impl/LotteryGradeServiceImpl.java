package com.kingpivot.base.lotteryGrade.service.impl;

import com.kingpivot.base.lotteryGrade.dao.LotteryGradeDao;
import com.kingpivot.base.lotteryGrade.model.LotteryGrade;
import com.kingpivot.base.lotteryGrade.service.LotteryGradeService;
import com.kingpivot.common.dao.BaseDao;
import com.kingpivot.common.service.BaseServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service("lotteryGradeService")
public class LotteryGradeServiceImpl extends BaseServiceImpl<LotteryGrade, String> implements LotteryGradeService {

    @Resource(name = "lotteryGradeDao")
    private LotteryGradeDao lotteryGradeDao;

    @Override
    public BaseDao<LotteryGrade, String> getDAO() {
        return this.lotteryGradeDao;
    }

    @Override
    public List<LotteryGrade> getLotteryGradeByLotteryID(String lotteryID) {
        return lotteryGradeDao.getLotteryGradeByLotteryID(lotteryID);
    }
}

