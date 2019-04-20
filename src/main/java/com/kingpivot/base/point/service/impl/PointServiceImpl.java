package com.kingpivot.base.point.service.impl;

import com.kingpivot.base.point.dao.PointDao;
import com.kingpivot.base.point.model.Point;
import com.kingpivot.base.point.service.PointService;
import com.kingpivot.common.dao.BaseDao;
import com.kingpivot.common.service.BaseServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service("pointService")
public class PointServiceImpl extends BaseServiceImpl<Point, String> implements PointService {

    @Resource(name = "pointDao")
    private PointDao pointDao;

    @Override
    public BaseDao<Point, String> getDAO() {
        return this.pointDao;
    }
}
