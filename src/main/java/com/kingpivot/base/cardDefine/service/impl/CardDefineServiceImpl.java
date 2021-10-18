package com.kingpivot.base.cardDefine.service.impl;

import com.kingpivot.base.cardDefine.dao.CardDefineDao;
import com.kingpivot.base.cardDefine.model.CardDefine;
import com.kingpivot.base.cardDefine.service.CardDefineService;
import com.kingpivot.common.dao.BaseDao;
import com.kingpivot.common.service.BaseServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service("cardDefineService")
public class CardDefineServiceImpl extends BaseServiceImpl<CardDefine, String> implements CardDefineService {

    @Resource(name = "cardDefineDao")
    private CardDefineDao cardDefineDao;

    @Override
    public BaseDao<CardDefine, String> getDAO() {
        return this.cardDefineDao;
    }
}
