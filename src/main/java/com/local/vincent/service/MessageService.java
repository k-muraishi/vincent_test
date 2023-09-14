package com.local.vincent.service;

import org.springframework.stereotype.Service;

@Service
public class MessageService {

    /**
     * メッセージの構文を整理するためのメソッドなど
     */
    public String fixMessage(String messageText){
        String fixedMessageText = null;

        fixedMessageText = messageText;

        return fixedMessageText;
    }
}
