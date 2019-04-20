package com.kingpivot.base.application.service;

import com.kingpivot.base.application.model.Application;
import com.kingpivot.common.service.BaseService;

public interface ApplicationService extends BaseService<Application, String> {
    String getNameByAppid(String id);
}
