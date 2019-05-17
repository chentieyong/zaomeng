package com.kingpivot.base.parameter.service.impl;

import com.kingpivot.base.parameter.dao.ParameterDao;
import com.kingpivot.base.parameter.model.Parameter;
import com.kingpivot.base.parameter.service.ParameterService;
import com.kingpivot.common.dao.BaseDao;
import com.kingpivot.common.service.BaseServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service("parameterService")
public class ParameterServiceImpl extends BaseServiceImpl<Parameter, String> implements ParameterService {

    @Resource(name = "parameterDao")
    private ParameterDao parameterDao;

    @Override
    public BaseDao<Parameter, String> getDAO() {
        return this.parameterDao;
    }

    @Override
    public String getParemeterValueByCode(String code) {
        return parameterDao.getParemeterValueByCode(code);
    }

}
