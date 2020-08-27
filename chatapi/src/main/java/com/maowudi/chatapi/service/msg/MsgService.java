package com.maowudi.chatapi.service.msg;

/**
 * 消息服务
 */
public interface MsgService {

    /**
     * 发送消息
     * @param msgText
     * @param userName
     * @param toUserName
     * @return
     */
    String sendMsg(String msgText,String userName, String toUserName);

}
