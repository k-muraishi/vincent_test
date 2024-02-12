package com.local.vincent.entity;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.Getter;
import lombok.Setter;

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

	public User() {
		this.id = null;
		this.lineUserId = null;
		this.deleteFlag = null;
	}
}
