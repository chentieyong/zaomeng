package com.kingpivot.common.model;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;
import java.sql.Timestamp;

@MappedSuperclass
public abstract class BaseModel<ID extends Serializable> implements Serializable, Cloneable{

	private static final long serialVersionUID = -83981606006814407L;

	@Column(name = "isValid",columnDefinition = "int default 1")
	private int isValid = 1;//是否有效 1有效 0无效

	@Column(name = "isLock",columnDefinition = "int default 0")
	private int isLock = 0; //是否锁定 0否 1是

	@Column(length=36)
	private String creator; //创建人

	private Timestamp createdTime;//创建时间

	@Column(length=36)
	private String modifier;//最后修改人

	private Timestamp modifiedTime;//最后修改时间

	@Column(length=36)
	private String auditor;//审核人

	private Timestamp auditTime;//审核时间


	public abstract ID getId();

	public int getIsValid() {
		return isValid;
	}

	public void setIsValid(int isValid) {
		this.isValid = isValid;
	}

	public int getIsLock() {
		return isLock;
	}

	public void setIsLock(int isLock) {
		this.isLock = isLock;
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public Timestamp getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(Timestamp createdTime) {
		this.createdTime = createdTime;
	}

	public String getModifier() {
		return modifier;
	}

	public void setModifier(String modifier) {
		this.modifier = modifier;
	}

	public Timestamp getModifiedTime() {
		return modifiedTime;
	}

	public void setModifiedTime(Timestamp modifiedTime) {
		this.modifiedTime = modifiedTime;
	}

	public String getAuditor() {
		return auditor;
	}

	public void setAuditor(String auditor) {
		this.auditor = auditor;
	}

	public Timestamp getAuditTime() {
		return auditTime;
	}

	public void setAuditTime(Timestamp auditTime) {
		this.auditTime = auditTime;
	}
}
