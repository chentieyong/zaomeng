package com.kingpivot.base.objectFeatureData.service.impl;

import com.kingpivot.base.objectFeatureData.dao.ObjectFeatureDataDao;
import com.kingpivot.base.objectFeatureData.model.ObjectFeatureData;
import com.kingpivot.base.objectFeatureData.service.ObjectFeatureDataService;
import com.kingpivot.common.dao.BaseDao;
import com.kingpivot.common.service.BaseServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Created by Administrator on 2016/6/2.
 */
@Service("objectFeatureDataService")
public class ObjectFeatureDataServiceImpl extends BaseServiceImpl<ObjectFeatureData, String> implements ObjectFeatureDataService {


    @Resource(name = "objectFeatureDataDao")
    private ObjectFeatureDataDao objectFeatureDataDao;


    public BaseDao<ObjectFeatureData, String> getDAO() {
        return this.objectFeatureDataDao;
    }

    @Override
    public double getObjectFetureData(String objectID, String objectFeatureItemID1) {
        Double val = objectFeatureDataDao.getObjectFetureData(objectID, objectFeatureItemID1);
        if (val == null) {
            return 0;
        }
        return val.doubleValue();
    }
}
