package com.kingpivot.base.navigator.service;

import com.kingpivot.base.navigator.model.Navigator;
import com.kingpivot.common.service.BaseService;
import com.kingpivot.common.util.TreeInfo;
import com.kingpivot.common.util.TreeInfoDTO;

public interface NavigatorService extends BaseService<Navigator, String> {
    TreeInfoDTO<TreeInfo> getTreeData(String rootId, String depth);
}
