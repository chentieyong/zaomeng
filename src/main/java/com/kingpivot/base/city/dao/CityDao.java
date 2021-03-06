package com.kingpivot.base.city.dao;

import com.kingpivot.base.city.model.City;
import com.kingpivot.common.dao.BaseDao;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.persistence.Table;

@Repository
@Table(name = "city")
@Qualifier("cityDao")
public interface CityDao extends BaseDao<City, String> {

    @Query(value = "select name from city where id=?1 and isValid=1 and isLock=0",nativeQuery = true)
    String getNameById(String id);
}
