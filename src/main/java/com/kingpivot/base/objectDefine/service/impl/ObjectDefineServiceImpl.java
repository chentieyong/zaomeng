package com.kingpivot.base.objectDefine.service.impl;

import com.kingpivot.base.objectDefine.dao.ObjectDefineDao;
import com.kingpivot.base.objectDefine.model.ObjectDefine;
import com.kingpivot.base.objectDefine.service.ObjectDefineService;
import com.kingpivot.common.dao.BaseDao;
import com.kingpivot.common.service.BaseServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service("objectDefineService")
public class ObjectDefineServiceImpl extends BaseServiceImpl<ObjectDefine, String> implements ObjectDefineService {
    @Resource(name = "objectDefineDao")
    private ObjectDefineDao objectDefineDao;

    @Override
    public BaseDao<ObjectDefine, String> getDAO() {
        return this.objectDefineDao;
    }
}
