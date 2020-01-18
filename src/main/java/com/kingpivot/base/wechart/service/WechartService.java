package com.kingpivot.base.wechart.service;

import com.kingpivot.base.wechart.model.Wechart;
import com.kingpivot.common.service.BaseService;

public interface WechartService extends BaseService<Wechart, String> {
    Wechart getWechartByPublicNo(String publicNo);
}
