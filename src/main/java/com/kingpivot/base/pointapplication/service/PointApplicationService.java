package com.kingpivot.base.pointapplication.service;

import com.kingpivot.base.pointapplication.model.PointApplication;
import com.kingpivot.common.service.BaseService;


public interface PointApplicationService extends BaseService<PointApplication, String> {
    int getNumberByAppIdAndPointName(String applicationID, String pointName);
}
