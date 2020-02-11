package com.kingpivot.base.city.service.impl;

import com.kingpivot.base.city.dao.CityDao;
import com.kingpivot.base.city.model.City;
import com.kingpivot.base.city.service.CityService;
import com.kingpivot.common.dao.BaseDao;
import com.kingpivot.common.service.BaseServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service("cityService")
public class CityServiceImpl extends BaseServiceImpl<City, String> implements CityService {
    @Resource(name = "cityDao")
    private CityDao cityDao;

    @Override
    public BaseDao<City, String> getDAO() {
        return this.cityDao;
    }

    @Override
    public String getNameById(String id) {
        return cityDao.getNameById(id);
    }
}
