package com.kingpivot.base.collect.service;

import com.kingpivot.base.collect.model.Collect;
import com.kingpivot.common.service.BaseService;

public interface CollectService extends BaseService<Collect, String> {
    String getCollectByObjectIDAndMemberID(String objectID,String memberID);
}
