package com.kingpivot.base.pageNode.service.impl;

import com.kingpivot.base.pageNode.dao.PageNodeDao;
import com.kingpivot.base.pageNode.model.PageNode;
import com.kingpivot.base.pageNode.service.PageNodeService;
import com.kingpivot.common.dao.BaseDao;
import com.kingpivot.common.service.BaseServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service("pageNodeService")
public class PageNodeServiceImpl extends BaseServiceImpl<PageNode, String> implements PageNodeService {

    @Resource(name = "pageNodeDao")
    private PageNodeDao pageNodeDao;

    @Override
    public BaseDao<PageNode, String> getDAO() {
        return this.pageNodeDao;
    }

    @Override
    public PageNode getPageNodeByViewUrl(String viewUrl) {
        return pageNodeDao.getPageNodeByViewUrl(viewUrl);
    }
}
