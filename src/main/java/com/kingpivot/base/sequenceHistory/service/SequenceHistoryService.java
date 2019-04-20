package com.kingpivot.base.sequenceHistory.service;

import com.kingpivot.base.sequenceHistory.model.SequenceHistory;
import com.kingpivot.common.service.BaseService;


public interface SequenceHistoryService extends BaseService<SequenceHistory, String> {
    void saveHistory(String seqId, Integer value, String objectId);
}
