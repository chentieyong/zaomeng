package com.kingpivot.base.article.model;

import com.kingpivot.common.model.BaseModel;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "article")
public class Article extends BaseModel<String> {
    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    @Column(length = 36)
    private String id;//主键

    @Column(length = 200)
    private String title;//标题

    @Column(length = 100)
    private String subTitle;//副标题

    @Column(columnDefinition = "TEXT")
    private String content;//内容

    @Column(length = 36)
    private String navigatorID;//导航ID

    @Column(length = 36)
    private String categoryID;//类别ID

    @Column(length = 1000)
    private String description;

    @Column(length = 50)
    private String author;

    @Column
    private Integer isRead = 0;

    @Column(length = 36)
    private String companyID;

    @Column
    private Integer isPublish = 0;

    @Column
    private Integer orderSeq = 0;

    @Column(length = 100)
    private String listImage;//列表图

    @Column(length = 100)
    private String faceImage;//压题图

    @Column(length = 200)
    private String videoURL;//视频

    @Column(length = 200)
    private String videoImage;//视频

    @Column(columnDefinition = "INT default 0")
    private Integer readTimes;//阅读次数

    @Column
    private Integer browseTimes;

    @Column
    private String wxMsgId;

    @Column
    private Timestamp beginDate;

    @Column
    private Timestamp endDate;

    @Override
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubTitle() {
        return subTitle;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCategoryID() {
        return categoryID;
    }

    public void setCategoryID(String categoryID) {
        this.categoryID = categoryID;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Integer getIsRead() {
        return isRead;
    }

    public void setIsRead(Integer isRead) {
        this.isRead = isRead;
    }

    public String getCompanyID() {
        return companyID;
    }

    public void setCompanyID(String companyID) {
        this.companyID = companyID;
    }

    public Integer getIsPublish() {
        return isPublish;
    }

    public void setIsPublish(Integer isPublish) {
        this.isPublish = isPublish;
    }

    public String getListImage() {
        return listImage;
    }

    public void setListImage(String listImage) {
        this.listImage = listImage;
    }

    public String getFaceImage() {
        return faceImage;
    }

    public void setFaceImage(String faceImage) {
        this.faceImage = faceImage;
    }

    public String getVideoURL() {
        return videoURL;
    }

    public void setVideoURL(String videoURL) {
        this.videoURL = videoURL;
    }

    public Integer getReadTimes() {
        return readTimes;
    }

    public void setReadTimes(Integer readTimes) {
        this.readTimes = readTimes;
    }

    public Integer getBrowseTimes() {
        return browseTimes;
    }

    public void setBrowseTimes(Integer browseTimes) {
        this.browseTimes = browseTimes;
    }

    public String getWxMsgId() {
        return wxMsgId;
    }

    public void setWxMsgId(String wxMsgId) {
        this.wxMsgId = wxMsgId;
    }

    public Timestamp getBeginDate() {
        return beginDate;
    }

    public void setBeginDate(Timestamp beginDate) {
        this.beginDate = beginDate;
    }

    public Timestamp getEndDate() {
        return endDate;
    }

    public void setEndDate(Timestamp endDate) {
        this.endDate = endDate;
    }

    public String getNavigatorID() {
        return navigatorID;
    }

    public void setNavigatorID(String navigatorID) {
        this.navigatorID = navigatorID;
    }

    public String getVideoImage() {
        return videoImage;
    }

    public void setVideoImage(String videoImage) {
        this.videoImage = videoImage;
    }

    public Integer getOrderSeq() {
        return orderSeq;
    }

    public void setOrderSeq(Integer orderSeq) {
        this.orderSeq = orderSeq;
    }
}
