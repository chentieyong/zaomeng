package com.kingpivot.common.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;

import java.io.Serializable;

@NoRepositoryBean
public interface BaseDao<T,ID extends Serializable> extends JpaSpecificationExecutor<T>,JpaRepository<T, ID> {

}
