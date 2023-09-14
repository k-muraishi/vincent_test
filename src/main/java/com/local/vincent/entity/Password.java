package com.local.vincent.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;

@Getter
@Setter
@Entity
public class Password {
    @Id
    /*パスワードテーブルID*/
    private Integer id;

    /*メッセージID*/
    private Integer messageId;

    /*パスワード*/
    private String password;
}
