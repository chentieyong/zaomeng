package com.kingpivot.common.jms.dto.point;

public class GetPointRequest {
    private String memberID;
    private String objectDefineID;
    private String pointName;

    public static class Builder {
        private String memberID;
        private String objectDefineID;
        private String pointName;

        public Builder objectDefineID(String val) {
            objectDefineID = val;
            return this;
        }

        public Builder memberID(String val) {
            memberID = val;
            return this;
        }

        public Builder pointName(String val) {
            pointName = val;
            return this;
        }

        public GetPointRequest build() {
            return new GetPointRequest(this);
        }
    }

    private GetPointRequest() {
    }

    private GetPointRequest(Builder builder) {
        this.objectDefineID = builder.objectDefineID;
        this.memberID = builder.memberID;
        this.pointName = builder.pointName;
    }

    public String getMemberID() {
        return memberID;
    }

    public void setMemberID(String memberID) {
        this.memberID = memberID;
    }

    public String getObjectDefineID() {
        return objectDefineID;
    }

    public void setObjectDefineID(String objectDefineID) {
        this.objectDefineID = objectDefineID;
    }

    public String getPointName() {
        return pointName;
    }

    public void setPointName(String pointName) {
        this.pointName = pointName;
    }
}
