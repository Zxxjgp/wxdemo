package com.wx.ioc.wxdemo.common;

import com.wx.ioc.wxdemo.controller.Test;

/**
 * 接收到的消息类型
 * @author HSHY-394
 */
public enum MessageType {
    TEXT,//文本消息
    IMAGE,//图片消息
    VOICE,//语音消息
    VIDEO,//视频消息
    SHORTVIDEO,//小视频消息
    LOCATION,//地理位置消息
    LINK,//链接消息
    EVENT,//事件消息
    TEST
}
