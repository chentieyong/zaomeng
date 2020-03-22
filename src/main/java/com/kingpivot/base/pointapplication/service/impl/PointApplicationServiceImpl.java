package com.kingpivot.base.pointapplication.service.impl;

import com.kingpivot.base.pointapplication.dao.PointApplicationDao;
import com.kingpivot.base.pointapplication.model.PointApplication;
import com.kingpivot.base.pointapplication.service.PointApplicationService;
import com.kingpivot.common.dao.BaseDao;
import com.kingpivot.common.service.BaseServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service("pointApplicationService")
public class PointApplicationServiceImpl extends BaseServiceImpl<PointApplication, String> implements PointApplicationService {

    @Resource(name = "pointApplicationDao")
    private PointApplicationDao pointApplicationDao;

    @Override
    public BaseDao<PointApplication, String> getDAO() {
        return this.pointApplicationDao;
    }

    @Override
    public int getNumberByAppIdAndPointName(String applicationID, String pointName) {
        Integer val = pointApplicationDao.getNumberByAppIdAndPointName(applicationID, pointName);
        if (val == null) {
            return 0;
        }
        return val.intValue();
    }
}
