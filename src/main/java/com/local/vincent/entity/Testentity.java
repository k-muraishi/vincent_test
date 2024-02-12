package com.local.vincent.entity;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Testentity {
	@Id
	private long id;

	private String title;

}
