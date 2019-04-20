package com.kingpivot.base.attachment.dao;

import com.kingpivot.base.attachment.model.Attachment;
import com.kingpivot.common.dao.BaseDao;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import javax.persistence.Table;


@Repository
@Table(name = "attachment")
@Qualifier("attachmentDao")
public interface AttachmentDao extends BaseDao<Attachment, String> {

}