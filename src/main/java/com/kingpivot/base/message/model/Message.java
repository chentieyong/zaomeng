package com.kingpivot.base.message.model;

import com.kingpivot.common.model.BaseModel;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "message")
public class Message extends BaseModel<String> {
    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    @Column(length = 36)
    private String id;//主键

    @Column()
    private String name;//名称

    @Column()
    private String shortName;//简称

    @Column()
    private String messageType;//站内信类型

    @Column()
    private int senderType;//发送人类型

    @Column()
    private String senderID;//发送人ID

    @Column(length = 100)
    public String senderName;

    @Column(length = 100)
    private String objectName;

    @Column(length = 100)
    private String username;

    @Column()
    private int receiverType;//接受人类型

    @Column()
    private String receiverID;//接受人ID

    @Column(columnDefinition = "int default 0")
    private int isRead;//是否阅读

    @Column()
    private Timestamp sendDate;//发送时间

    @Column()
    private Timestamp receivedDate;//接受时间

    @Column(length = 36)
    private String objectDefineID;//对象定义ID

    @Column(length = 36)
    private String objectID;//对象ID

    @Column()
    private String objectType;//对象类型

    @Column(length = 200)
    private String description;

    @Column(length = 100)
    private String label;

    @Column(length = 10)
    private String code;

    @Column()
    private Timestamp readTime;//阅读时间


    @Override
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public String getMessageType() {
        return messageType;
    }

    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }

    public int getSenderType() {
        return senderType;
    }

    public void setSenderType(int senderType) {
        this.senderType = senderType;
    }

    public String getSenderID() {
        return senderID;
    }

    public void setSenderID(String senderID) {
        this.senderID = senderID;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public String getObjectName() {
        return objectName;
    }

    public void setObjectName(String objectName) {
        this.objectName = objectName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getReceiverType() {
        return receiverType;
    }

    public void setReceiverType(int receiverType) {
        this.receiverType = receiverType;
    }

    public String getReceiverID() {
        return receiverID;
    }

    public void setReceiverID(String receiverID) {
        this.receiverID = receiverID;
    }

    public int getIsRead() {
        return isRead;
    }

    public void setIsRead(int isRead) {
        this.isRead = isRead;
    }

    public Timestamp getSendDate() {
        return sendDate;
    }

    public void setSendDate(Timestamp sendDate) {
        this.sendDate = sendDate;
    }

    public Timestamp getReceivedDate() {
        return receivedDate;
    }

    public void setReceivedDate(Timestamp receivedDate) {
        this.receivedDate = receivedDate;
    }

    public String getObjectDefineID() {
        return objectDefineID;
    }

    public void setObjectDefineID(String objectDefineID) {
        this.objectDefineID = objectDefineID;
    }

    public String getObjectID() {
        return objectID;
    }

    public void setObjectID(String objectID) {
        this.objectID = objectID;
    }

    public String getObjectType() {
        return objectType;
    }

    public void setObjectType(String objectType) {
        this.objectType = objectType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Timestamp getReadTime() {
        return readTime;
    }

    public void setReadTime(Timestamp readTime) {
        this.readTime = readTime;
    }

    public enum MessageType {
        TASK_VERIFY("4191ba35a84248b8bb02a103aecd56fc"),//任务审核
        TASK_RATIFY("b6c8d9d859304e138a52282e9ee1c867"),//任务批准
        CHANGE_REMIND("d822c19796ff433e8eeb2649d171b204"),//变更提醒
        TASK_REMIND("e3b8da148adc435683a9df0c80080fa6"),//任务提醒
        APPROVE_MESSAGE("7a8fd504654c478ca6129d8a03f1a74b"),//审批消息
        NOTICE_MESSAGE("b95fd6f74d8345b1acf2f2994e5335f1"),//公告
        SYSTEM_MESSAGE("38954962a15b440cade0d707800d4c87"),//系统消息
        REMIND_MESSAGE("494965450a2e4e38b2c4f4daa0cff730");//消息提醒
        private String type;

        MessageType(String type) {
            this.type = type;
        }

        public String getType() {
            return this.type;
        }
    }

}
