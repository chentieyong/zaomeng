package com.kingpivot.protocol;

import com.google.common.collect.Maps;
import com.kingpivot.protocol.MessageHeader.Code;

import java.io.Serializable;
import java.util.Map;


public class MessagePacket implements Serializable{
    
    private static final long serialVersionUID = -3375850163636689873L;

    private MessageHeader header ;
    
    private Object   body;
    
    public MessagePacket(){}


    
    public MessagePacket(MessageHeader header , Object body){
        this.header = header;
/*        Map<String, Object> rsMap = Maps.newHashMap();
        rsMap.put("msg", body);
        this.body = rsMap;*/
        this.body = body;
    }
    public MessagePacket(MessageHeader header , Object body, Boolean isFail){
        this.header = header;
        Map<String, Object> rsMap = Maps.newHashMap();
        rsMap.put("msg", body);
        this.body = rsMap;

    }


    
    public static MessagePacket newSuccess(Object body){
        MessagePacket packet = new MessagePacket(MessageHeader.success(),body);
        return packet;
    }
    
    public static MessagePacket newSuccess(Object body, String msg){
        MessagePacket packet = new MessagePacket(MessageHeader.success(msg),body);
        return packet;
    }
    
    public static MessagePacket newFail(String message){
        MessagePacket packet = new MessagePacket(MessageHeader.fail(message),null);
        return packet;
    }
    
    public static MessagePacket newFail(){
        MessagePacket packet = new MessagePacket(MessageHeader.fail(),null);
        return packet;
    }
    
    public static MessagePacket newFail(Code code){
        MessagePacket packet = new MessagePacket(MessageHeader.fail(code),null);
        return packet;
    }
    
    public static MessagePacket newFail(Code code, String message){
        Boolean bool= new Boolean("1");
        MessagePacket packet = new MessagePacket(MessageHeader.fail(code,message),message,bool);
        return packet;
    }

    public static MessagePacket newFail(int code, String message){
        Boolean bool= new Boolean("1");
        MessagePacket packet = new MessagePacket(MessageHeader.fail(code,message),message,bool);
        return packet;
    }

    
    public MessageHeader getHeader() {
        return header;
    }

    public void setHeader(MessageHeader header) {
        this.header = header;
    }
    public Object getBody() {
        return body;
    }
    
    public void setBody(Object body) {
        this.body = body;
    }
    

}
