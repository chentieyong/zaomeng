package com.kingpivot.base.focus.dao;

import com.kingpivot.base.focus.model.FocusPicture;
import com.kingpivot.common.dao.BaseDao;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import javax.persistence.Table;

@Repository
@Table(name = "focus")
@Qualifier("focusPictureDao")
public interface FocusPictureDao extends BaseDao<FocusPicture, String> {
}
