package com.kingpivot.base.attachment.service.impl;

import com.kingpivot.base.attachment.dao.AttachmentDao;
import com.kingpivot.base.attachment.model.Attachment;
import com.kingpivot.base.attachment.service.AttachmentService;
import com.kingpivot.common.dao.BaseDao;
import com.kingpivot.common.service.BaseServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service("attachmentService")
public class AttachmentServiceImpl extends BaseServiceImpl<Attachment, String> implements AttachmentService {
    @Resource(name = "attachmentDao")
    private AttachmentDao attachmentDao;

    @Override
    public BaseDao<Attachment, String> getDAO() {
        return this.attachmentDao;
    }
}
