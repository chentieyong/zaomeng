package com.kingpivot.base.blessingTree.service.impl;

import com.kingpivot.base.blessingTree.dao.BlessingTreeDao;
import com.kingpivot.base.blessingTree.model.BlessingTree;
import com.kingpivot.base.blessingTree.service.BlessingTreeService;
import com.kingpivot.common.dao.BaseDao;
import com.kingpivot.common.service.BaseServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service("blessingTreeService")
public class BlessingTreeServiceImpl extends BaseServiceImpl<BlessingTree, String> implements BlessingTreeService {

    @Resource(name = "blessingTreeDao")
    private BlessingTreeDao blessingTreeDao;

    @Override
    public BaseDao<BlessingTree, String> getDAO() {
        return this.blessingTreeDao;
    }
}

