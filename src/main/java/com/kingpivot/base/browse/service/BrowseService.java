package com.kingpivot.base.browse.service;

import com.kingpivot.base.browse.model.Browse;
import com.kingpivot.common.service.BaseService;

public interface BrowseService extends BaseService<Browse, String> {
    String getBrowseByObjectIDAndMemberID(String objectID,String memberID);

    int getBrowseNumByObjectDefineIDAndMemberID(String objectDefineID, String memberID);
}
