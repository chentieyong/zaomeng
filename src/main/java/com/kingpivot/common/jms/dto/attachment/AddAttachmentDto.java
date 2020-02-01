package com.kingpivot.common.jms.dto.attachment;

public class AddAttachmentDto {
    private String objectID;//对象id
    private String objectName;//对象名称
    private String objectDefineID;//对象定义
    private int fileType;//文件类型
    private int showType;//公开类型
    private String urls;//图片链接
    private String delUrls;//老图片链接

    public static class Builder {
        private String objectID;//对象id
        private String objectName;//对象名称
        private String objectDefineID;//对象定义
        private int fileType;//文件类型
        private int showType;//公开类型
        private String urls;//图片链接
        private String delUrls;//老图片链接

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

        public Builder delUrls(String val) {
            delUrls = val;
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
        this.delUrls = builder.delUrls;//老图片链接
    }

    public String getObjectID() {
        return objectID;
    }

    public void setObjectID(String objectID) {
        this.objectID = objectID;
    }

    public String getObjectName() {
        return objectName;
    }

    public void setObjectName(String objectName) {
        this.objectName = objectName;
    }

    public String getObjectDefineID() {
        return objectDefineID;
    }

    public void setObjectDefineID(String objectDefineID) {
        this.objectDefineID = objectDefineID;
    }

    public int getFileType() {
        return fileType;
    }

    public void setFileType(int fileType) {
        this.fileType = fileType;
    }

    public int getShowType() {
        return showType;
    }

    public void setShowType(int showType) {
        this.showType = showType;
    }

    public String getUrls() {
        return urls;
    }

    public void setUrls(String urls) {
        this.urls = urls;
    }

    public String getDelUrls() {
        return delUrls;
    }

    public void setDelUrls(String delUrls) {
        this.delUrls = delUrls;
    }
}
