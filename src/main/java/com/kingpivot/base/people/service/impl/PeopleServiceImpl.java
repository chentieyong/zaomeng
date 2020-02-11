package com.kingpivot.base.people.service.impl;

import com.kingpivot.base.people.dao.PeopleDao;
import com.kingpivot.base.people.model.People;
import com.kingpivot.base.people.service.PeopleService;
import com.kingpivot.common.dao.BaseDao;
import com.kingpivot.common.service.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("peopleService")
public class PeopleServiceImpl extends BaseServiceImpl<People, String> implements PeopleService {
    @Autowired
    private PeopleDao peopleDao;

    @Override
    public BaseDao<People, String> getDAO() {
        return this.peopleDao;
    }

}
