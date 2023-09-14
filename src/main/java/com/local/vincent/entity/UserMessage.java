package com.local.vincent.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;

@Getter
@Setter
@Entity
public class UserMessage {
    @Id
    /*ユーザーメッセージ中間テーブルID*/
    private Integer id;

    /*ユーザーID*/
    private Integer sendUserId;

    /*メッセージID*/
    private Integer messageId;

}
