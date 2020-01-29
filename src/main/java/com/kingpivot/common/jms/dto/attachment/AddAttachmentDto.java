package com.kingpivot.common.jms.dto.attachment;

public class AddAttachmentDto {
    private String objectID;//对象id
    private String objectName;//对象名称
    private String objectDefineID;//对象定义
    private int fileType;//文件类型
    private int showType;//公开类型
    private String urls;//图片链接

    public static class Builder {
        private String objectID;//对象id
        private String objectName;//对象名称
        private String objectDefineID;//对象定义
        private int fileType;//文件类型
        private int showType;//公开类型
        private String urls;//图片链接

        public Builder objectID(String val) {
            objectID = val;
            return this;
        }

        public Builder objectName(String val) {
            objectName = val;
            return this;
        }

        public Builder objectDefineID(String val) {
            objectDefineID = val;
            return this;
        }

        public Builder fileType(int val) {
            fileType = val;
            return this;
        }

        public Builder showType(int val) {
            showType = val;
            return this;
        }

        public Builder urls(String val) {
            urls = val;
            return this;
        }

        public AddAttachmentDto build() {
            return new AddAttachmentDto(this);
        }
    }

    private AddAttachmentDto() {
    }

    private AddAttachmentDto(Builder builder) {
        this.objectID = builder.objectID;//对象id
        this.objectName = builder.objectName;//对象名称
        this.objectDefineID = builder.objectDefineID;//对象定义
        this.fileType = builder.fileType;//文件类型
        this.showType = builder.showType;//公开类型
        this.urls = builder.urls;//图片链接
    }
}
