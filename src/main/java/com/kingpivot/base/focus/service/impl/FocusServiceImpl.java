package com.kingpivot.base.focus.service.impl;

import com.kingpivot.base.focus.dao.FocusDao;
import com.kingpivot.base.focus.model.Focus;
import com.kingpivot.base.focus.service.FocusService;
import com.kingpivot.common.dao.BaseDao;
import com.kingpivot.common.service.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("focusService")
public class FocusServiceImpl extends BaseServiceImpl<Focus, String> implements FocusService {
    @Autowired
    private FocusDao focusDao;

    @Override
    public BaseDao<Focus, String> getDAO() {
        return this.focusDao;
    }
}
