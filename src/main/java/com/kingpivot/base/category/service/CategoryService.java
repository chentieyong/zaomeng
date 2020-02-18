package com.kingpivot.base.category.service;

import com.kingpivot.base.category.model.Category;
import com.kingpivot.common.service.BaseService;
import com.kingpivot.common.util.TreeInfo;
import com.kingpivot.common.util.TreeInfoDTO;

public interface CategoryService extends BaseService<Category, String> {
    TreeInfoDTO<TreeInfo> getTreeData(String rootId, String depth);

    String getIdByNameAndAppId(String name, String applicationID);
}
