package com.kingpivot.common.domain;

import io.swagger.annotations.ApiModelProperty;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;
import java.sql.Timestamp;

/**
 * 提取了部分公共字段，可以避免多个实体重复设置这些属性
 * @author chentieyong
 * @version v1.0
 * @time 2017年8月9日 下午1:50:22
 */
@MappedSuperclass
public abstract class AbstractBaseModel<ID extends Serializable> implements BaseModel<ID> {

	private static final long serialVersionUID = 1195969732659409799L;

	@ApiModelProperty(value="是否有效")
	@Column(name = "isValid",columnDefinition = "int default 1")
	private int isValid = 1;//是否有效 1有效 0无效

	@ApiModelProperty(value="是否锁定")
	@Column(name = "isLock",columnDefinition = "int default 0")
	private int isLock = 0; //是否锁定 0否 1是

	@ApiModelProperty(value="创建者ID")
	@Column()
	private String creator;
	
	@ApiModelProperty(value="创建时间")
	@Column(name="createdTime")
	private Timestamp createdTime;

	@ApiModelProperty(value="最后修改人")
	@Column(name="modifier")
	private String modifier;//最后修改人
	
	@ApiModelProperty(value="最后修改时间")
	@Column(name = "modifiedTime")
	private Timestamp modifiedTime;//最后修改时间

	@Column(length=36)
	private String auditor;//审核人

	@ApiModelProperty(value="审核时间")
	@Column(name = "auditTime")
	private Timestamp auditTime;//审核时间

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

	public static long getSerialVersionUID() {
		return serialVersionUID;
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
