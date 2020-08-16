package com.kingpivot.base.pageNode.service;

import com.kingpivot.base.pageNode.model.PageNode;
import com.kingpivot.common.service.BaseService;

public interface PageNodeService extends BaseService<PageNode, String> {
    PageNode getPageNodeByViewUrl(String viewUrl);

}
