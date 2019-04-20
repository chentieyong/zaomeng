package com.kingpivot.base.objectFeatureData.service;

import com.kingpivot.base.objectFeatureData.model.ObjectFeatureData;
import com.kingpivot.common.service.BaseService;

public interface ObjectFeatureDataService extends BaseService<ObjectFeatureData, String> {

    Object[] getObjectFetureData(String objectID,String objectFeatureItemID1);
}
