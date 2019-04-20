package com.kingpivot.base.application.service.impl;

import com.kingpivot.base.application.dao.ApplicationDao;
import com.kingpivot.base.application.model.Application;
import com.kingpivot.base.application.service.ApplicationService;
import com.kingpivot.common.dao.BaseDao;
import com.kingpivot.common.service.BaseServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service("applicationService")
public class ApplicationServiceImpl extends BaseServiceImpl<Application, String> implements ApplicationService {
	@Resource(name = "applicationDao")
	private ApplicationDao applicationDao;


	@Override
	public BaseDao<Application, String> getDAO() {
		return this.applicationDao;
	}


	@Override
	public String getNameByAppid(String id) {
		return applicationDao.getNameByAppid(id);
	}
}
