package com.kingpivot.base.sequenceHistory.service.impl;

import com.kingpivot.base.sequenceHistory.dao.SequenceHistoryDao;
import com.kingpivot.base.sequenceHistory.model.SequenceHistory;
import com.kingpivot.base.sequenceHistory.service.SequenceHistoryService;
import com.kingpivot.common.dao.BaseDao;
import com.kingpivot.common.service.BaseServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service("sequenceHistoryService")
public class SequenceHistoryServiceImpl extends BaseServiceImpl<SequenceHistory, String> implements SequenceHistoryService {

    @Resource(name = "sequenceHistoryDao")
    private SequenceHistoryDao sequenceHistoryDao;


    @Override
    public BaseDao<SequenceHistory, String> getDAO() {
        return this.sequenceHistoryDao;
    }

    @Override
    public void saveHistory(String seqId, Integer value, String objectId) {
        SequenceHistory sequenceHistory = new SequenceHistory();
        sequenceHistory.setSequenceDefineID(seqId);
        sequenceHistory.setCurrentValue(value);
        sequenceHistory.setObjectID(objectId);
        this.save(sequenceHistory);
    }
}
