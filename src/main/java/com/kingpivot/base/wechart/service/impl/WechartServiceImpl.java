package com.kingpivot.base.wechart.service.impl;

import com.kingpivot.base.wechart.dao.WechartDao;
import com.kingpivot.base.wechart.model.Wechart;
import com.kingpivot.base.wechart.service.WechartService;
import com.kingpivot.common.dao.BaseDao;
import com.kingpivot.common.service.BaseServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service("wechartService")
public class WechartServiceImpl extends BaseServiceImpl<Wechart, String> implements WechartService {

    @Resource(name = "wechartDao")
    private WechartDao wechartDao;

    @Override
    public BaseDao<Wechart, String> getDAO() {
        return this.wechartDao;
    }

    @Override
    public Wechart getWechartByPublicNo(String publicNo) {
        return wechartDao.getWechartByPublicNo(publicNo);
    }
}
