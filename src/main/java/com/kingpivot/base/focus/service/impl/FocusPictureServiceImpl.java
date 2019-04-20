package com.kingpivot.base.focus.service.impl;

import com.kingpivot.base.focus.dao.FocusPictureDao;
import com.kingpivot.base.focus.model.FocusPicture;
import com.kingpivot.base.focus.service.FocusPictureService;
import com.kingpivot.common.dao.BaseDao;
import com.kingpivot.common.service.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("focusPictureService")
public class FocusPictureServiceImpl extends BaseServiceImpl<FocusPicture, String> implements FocusPictureService {
    @Autowired
    private FocusPictureDao focusPictureDao;

    @Override
    public BaseDao<FocusPicture, String> getDAO() {
        return this.focusPictureDao;
    }
}
