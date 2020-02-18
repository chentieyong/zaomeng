package com.kingpivot.common.jms.dto.message;

public class MessageRequest {
    private String objectDefineID;//对象类型
    private String objectID;//对象id
    private String objectName;//对象名称
    private String messageType;//消息类型
    private String receiverID;//接收人
    private String applicationID;
    private String messageCode;//语句编码

    public static class Builder {
        private String objectDefineID;//对象类型
        private String objectID;//对象id
        private String objectName;//对象名称
        private String messageType;//消息类型
        private String receiverID;//接收人
        private String applicationID;
        private String messageCode;//语句编码

        public Builder objectDefineID(String val) {
            objectDefineID = val;
            return this;
        }

        public Builder objectID(String val) {
            objectID = val;
            return this;
        }

        public Builder objectName(String val) {
            objectName = val;
            return this;
        }

        public Builder messageType(String val) {
            messageType = val;
            return this;
        }

        public Builder receiverID(String val) {
            receiverID = val;
            return this;
        }

        public Builder messageCode(String val) {
            messageCode = val;
            return this;
        }

        public Builder applicationID(String val) {
            applicationID = val;
            return this;
        }

        public MessageRequest build() {
            return new MessageRequest(this);
        }
    }

    private MessageRequest() {
    }

    private MessageRequest(Builder builder) {
        this.objectDefineID = builder.objectDefineID;
        this.objectID = builder.objectID;
        this.objectName = builder.objectName;
        this.messageType = builder.messageType;
        this.receiverID = builder.receiverID;
        this.applicationID = builder.applicationID;
        this.messageCode = builder.messageCode;
    }
}
