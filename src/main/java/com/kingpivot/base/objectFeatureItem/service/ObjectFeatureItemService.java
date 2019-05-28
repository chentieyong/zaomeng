package com.kingpivot.base.objectFeatureItem.service;

import com.kingpivot.base.objectFeatureItem.model.ObjectFeatureItem;
import com.kingpivot.common.service.BaseService;

/**
 * Created by Administrator on 2016/6/2.
 */
public interface ObjectFeatureItemService extends BaseService<ObjectFeatureItem, String> {
    Object[] getFeatureItemDefineList(String objectID);

    Object[] getFeatureItemList(String objectID, String featureDefineID);

    Object getDefaultFeatureItem(String objectID);
}
