package com.local.vincent.entity;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.Getter;
import lombok.Setter;

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
