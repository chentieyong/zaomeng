package com.kingpivot.base.blessingTreeDetail.service.impl;

import com.kingpivot.base.blessingTreeDetail.dao.BlessingTreeDetailDao;
import com.kingpivot.base.blessingTreeDetail.model.BlessingTreeDetail;
import com.kingpivot.base.blessingTreeDetail.service.BlessingTreeDetailService;
import com.kingpivot.common.dao.BaseDao;
import com.kingpivot.common.service.BaseServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service("blessingTreeDetailService")
public class BlessingTreeDetailServiceImpl extends BaseServiceImpl<BlessingTreeDetail, String> implements BlessingTreeDetailService {

    @Resource(name = "blessingTreeDetailDao")
    private BlessingTreeDetailDao blessingTreeDetailDao;

    @Override
    public BaseDao<BlessingTreeDetail, String> getDAO() {
        return this.blessingTreeDetailDao;
    }
}

