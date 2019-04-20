package com.kingpivot.base.sequenceDefine.service;

import com.kingpivot.base.sequenceDefine.model.SequenceDefine;
import com.kingpivot.common.service.BaseService;


public interface SequenceDefineService extends BaseService<SequenceDefine, String> {

    String genCode(String code, String objectId);

}
