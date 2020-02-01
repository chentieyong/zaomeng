package com.kingpivot.base.major.service.impl;

import com.kingpivot.base.major.dao.MajorDao;
import com.kingpivot.base.major.model.Major;
import com.kingpivot.base.major.service.MajorService;
import com.kingpivot.common.dao.BaseDao;
import com.kingpivot.common.service.BaseServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service("majorService")
public class MajorServiceImpl extends BaseServiceImpl<Major, String> implements MajorService {

    @Resource(name = "majorDao")
    private MajorDao majorDao;

    @Override
    public BaseDao<Major, String> getDAO() {
        return this.majorDao;
    }
}
