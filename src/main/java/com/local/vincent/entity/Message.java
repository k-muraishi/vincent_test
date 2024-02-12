package com.local.vincent.entity;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Message {
	@Id
	/*メッセージID*/
	private Integer id;

	/*ユーザーメッセージ中間テーブルID*/
	private Integer userMessageId;

	/*送信ユーザーID*/
	private Integer sendUserId;

	/*署名*/
	private String sendUserName;

	/*受信ユーザーID*/
	private Integer receptUserId;

	/*宛名*/
	private String receptUserName;

	/*メッセージ内容*/
	private String messageText;

	/*パスワードID */
	private Integer passwordId;

	/*送信ステータス*/
	private Integer sendStatus;

	/*既読フラグ*/
	private Integer readFlag;

	/*お気に入りステータス*/
	private Integer favoriteStatus;
}
