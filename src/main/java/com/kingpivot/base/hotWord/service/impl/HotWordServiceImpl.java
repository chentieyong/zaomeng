package com.kingpivot.base.hotWord.service.impl;

import com.kingpivot.base.hotWord.dao.HotWordDao;
import com.kingpivot.base.hotWord.model.HotWord;
import com.kingpivot.base.hotWord.service.HotWordService;
import com.kingpivot.common.dao.BaseDao;
import com.kingpivot.common.service.BaseServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service("hotWordService")
public class HotWordServiceImpl extends BaseServiceImpl<HotWord, String> implements HotWordService {
    @Resource(name = "hotWordDao")
    private HotWordDao hotWordDao;

    @Override
    public BaseDao<HotWord, String> getDAO() {
        return this.hotWordDao;
    }
}
