package com.kingpivot.base.pointdefine.service.impl;

import com.kingpivot.base.pointdefine.dao.PointDefineDao;
import com.kingpivot.base.pointdefine.model.PointDefine;
import com.kingpivot.base.pointdefine.service.PointDefineService;
import com.kingpivot.common.dao.BaseDao;
import com.kingpivot.common.service.BaseServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service("pointDefineService")
public class PointDefineServiceImpl extends BaseServiceImpl<PointDefine, String> implements PointDefineService {

    @Resource(name = "pointDefineDao")
    private PointDefineDao pointDefineDao;

    @Override
    public BaseDao<PointDefine, String> getDAO() {
        return this.pointDefineDao;
    }
}
