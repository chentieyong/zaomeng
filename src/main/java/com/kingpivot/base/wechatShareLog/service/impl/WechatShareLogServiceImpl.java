package com.kingpivot.base.wechatShareLog.service.impl;

import com.kingpivot.base.wechatShareLog.dao.WechatShareLogDao;
import com.kingpivot.base.wechatShareLog.model.WechatShareLog;
import com.kingpivot.base.wechatShareLog.service.WechatShareLogService;
import com.kingpivot.common.dao.BaseDao;
import com.kingpivot.common.service.BaseServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service("wechatShareLogService")
public class WechatShareLogServiceImpl extends BaseServiceImpl<WechatShareLog, String> implements WechatShareLogService {

    @Resource(name = "wechatShareLogDao")
    private WechatShareLogDao wechatShareLogDao;

    @Override
    public BaseDao<WechatShareLog, String> getDAO() {
        return this.wechatShareLogDao;
    }
}
