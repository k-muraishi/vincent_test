package com.local.vincent.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;

@Getter
@Setter
@Entity()
public class User {
    @Id
    /*ユーザーID*/
    private Integer id;

    /*lineユーザーID*/
    private String lineUserId;

    /*削除フラグ */
    private Integer deleteFlag;

    public User(){
        this.id = null;
        this.lineUserId = null;
        this.deleteFlag = null;
    }
}
