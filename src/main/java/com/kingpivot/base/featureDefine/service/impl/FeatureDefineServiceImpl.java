package com.kingpivot.base.featureDefine.service.impl;

import com.kingpivot.base.featureDefine.dao.FeatureDefineDao;
import com.kingpivot.base.featureDefine.model.FeatureDefine;
import com.kingpivot.base.featureDefine.service.FeatureDefineService;
import com.kingpivot.common.dao.BaseDao;
import com.kingpivot.common.service.BaseServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Created by Administrator on 2016/9/18.
 */
@Service("featureDefineService")
public class FeatureDefineServiceImpl extends BaseServiceImpl<FeatureDefine, String> implements FeatureDefineService {
    @Resource(name = "featureDefineDao")
    private FeatureDefineDao featureDefineDao;

    @Override
    public BaseDao<FeatureDefine, String> getDAO() {
        return this.featureDefineDao;
    }
}
