package com.kingpivot.base.people.dao;

import com.kingpivot.base.people.model.People;
import com.kingpivot.common.dao.BaseDao;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import javax.persistence.Table;

@Repository
@Table(name = "people")
@Qualifier("peopleDao")
public interface PeopleDao extends BaseDao<People,String> {
}
