package com.kingpivot.common.jms.dto.memberBalance;

import java.math.BigDecimal;

public class MemberBalanceRequest {
    private String memberID;//会员id
    private String applicationID;
    private String siteID;
    private Integer operateType;//1湖北手工补资金  -1其他
    private String objectID;//操作对象id
    private String objectDefineID;
    private String objectName;
    private BigDecimal amount;//操作总金额
    private Integer point;//积分个数
    private String description;//描述"
    private Integer type;//1湖北手工补资金

    public static class Builder {
        private String memberID;//会员id
        private String applicationID;
        private String siteID;
        private Integer operateType;//1湖北手工补资金  -1其他
        private String objectID;//操作对象id
        private String objectDefineID;
        private String objectName;
        private BigDecimal amount;//操作总金额
        private Integer point;//积分个数
        private String description;//描述"
        private Integer type;//1湖北手工补资金

        public Builder memberID(String val) {
            memberID = val;
            return this;
        }

        public Builder applicationID(String val) {
            applicationID = val;
            return this;
        }

        public Builder siteID(String val) {
            siteID = val;
            return this;
        }

        public Builder operateType(Integer val) {
            operateType = val;
            return this;
        }

        public Builder objectID(String val) {
            objectID = val;
            return this;
        }

        public Builder objectDefineID(String val) {
            objectDefineID = val;
            return this;
        }

        public Builder objectName(String val) {
            objectName = val;
            return this;
        }

        public Builder amount(BigDecimal val) {
            amount = val;
            return this;
        }

        public Builder point(Integer val) {
            point = val;
            return this;
        }

        public Builder description(String val) {
            description = val;
            return this;
        }

        public Builder type(Integer val) {
            type = val;
            return this;
        }

        public MemberBalanceRequest build() {
            return new MemberBalanceRequest(this);
        }

    }

    private MemberBalanceRequest(Builder builder) {
        this.memberID = builder.memberID;//会员id
        this.applicationID = builder.applicationID;
        this.siteID = builder.siteID;
        this.operateType = builder.operateType;//1湖北手工补资金  -1其他
        this.objectID = builder.objectID;//操作对象id
        this.objectDefineID = builder.objectDefineID;
        this.objectName = builder.objectName;
        this.amount = builder.amount;//操作总金额
        this.point = builder.point;//积分个数
        this.description = builder.description;//描述"
        this.type = builder.type;//1湖北手工补资金
    }

    public String getMemberID() {
        return memberID;
    }

    public void setMemberID(String memberID) {
        this.memberID = memberID;
    }

    public String getApplicationID() {
        return applicationID;
    }

    public void setApplicationID(String applicationID) {
        this.applicationID = applicationID;
    }

    public String getSiteID() {
        return siteID;
    }

    public void setSiteID(String siteID) {
        this.siteID = siteID;
    }

    public Integer getOperateType() {
        return operateType;
    }

    public void setOperateType(Integer operateType) {
        this.operateType = operateType;
    }

    public String getObjectID() {
        return objectID;
    }

    public void setObjectID(String objectID) {
        this.objectID = objectID;
    }

    public String getObjectDefineID() {
        return objectDefineID;
    }

    public void setObjectDefineID(String objectDefineID) {
        this.objectDefineID = objectDefineID;
    }

    public String getObjectName() {
        return objectName;
    }

    public void setObjectName(String objectName) {
        this.objectName = objectName;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Integer getPoint() {
        return point;
    }

    public void setPoint(Integer point) {
        this.point = point;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }
}
