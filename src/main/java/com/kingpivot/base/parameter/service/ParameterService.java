package com.kingpivot.base.parameter.service;

import com.kingpivot.base.parameter.model.Parameter;
import com.kingpivot.common.service.BaseService;

import java.util.List;

public interface ParameterService extends BaseService<Parameter, String> {

    String getParemeterValueByCode(String code);
}
