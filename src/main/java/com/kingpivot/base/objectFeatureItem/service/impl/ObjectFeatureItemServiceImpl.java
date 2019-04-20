package com.kingpivot.base.objectFeatureItem.service.impl;

import com.kingpivot.base.objectFeatureItem.dao.ObjectFeatureItemDao;
import com.kingpivot.base.objectFeatureItem.model.ObjectFeatureItem;
import com.kingpivot.base.objectFeatureItem.service.ObjectFeatureItemService;
import com.kingpivot.common.dao.BaseDao;
import com.kingpivot.common.service.BaseServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Created by Administrator on 2016/6/2.
 */
@Service("objectFeatureItemService")
public class ObjectFeatureItemServiceImpl extends BaseServiceImpl<ObjectFeatureItem, String> implements ObjectFeatureItemService {


    @Resource(name = "objectFeatureItemDao")
    private ObjectFeatureItemDao objectFeatureItemDao;

    public BaseDao<ObjectFeatureItem, String> getDAO() {
        return this.objectFeatureItemDao;
    }

    @Override
    public Object[] getFeatureItemDefineList(String objectID) {
        return objectFeatureItemDao.getFeatureItemDefineList(objectID);
    }

    @Override
    public Object[] getFeatureItemList(String objectID, String featureDefineID) {
        return objectFeatureItemDao.getFeatureItemList(objectID, featureDefineID);
    }
}
