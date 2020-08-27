package com.maowudi.chatapi.entity.msg;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 消息实体类
 */
@Data
public class Msg implements Serializable {

    /**
     * 消息ID
     */
    private Integer msgId;

    /**
     * 消息内容 ： 暂时只能是文字
     */
    private String msgText;

    /**
     * 暂时不需要用户，只用当前访问名称
     */
    private String userName;

    /**
     * @ 对象
     */
    private String toUserName;

    /**
     * 消息类型，群发1 ， @ 为2
     */
    private Integer msgType;

    /**
     * 创建时间即发送时间
     */
    private Date createTime;
}
