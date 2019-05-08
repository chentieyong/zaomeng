package com.kingpivot.base.bonusDefine.service.impl;

import com.kingpivot.base.bonusDefine.dao.BonusDefineDao;
import com.kingpivot.base.bonusDefine.model.BonusDefine;
import com.kingpivot.base.bonusDefine.service.BonusDefineService;
import com.kingpivot.common.dao.BaseDao;
import com.kingpivot.common.service.BaseServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service("bonusDefineService")
public class BonusDefineServiceImpl extends BaseServiceImpl<BonusDefine, String> implements BonusDefineService {

	@Resource(name = "bonusDefineDao")
	private BonusDefineDao bonusDefineDao;

	@Override
	public BaseDao<BonusDefine, String> getDAO() {
		return this.bonusDefineDao;
	}
}


