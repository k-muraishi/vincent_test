package com.local.vincent.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.local.vincent.entity.UserMessage;
import com.local.vincent.repository.UserMessageRepository;

@Service
public class UserMessageDao {
	private final UserMessageRepository repository;

	@Autowired
	public UserMessageDao(UserMessageRepository repository) {

		this.repository = repository;
	}

	public List<UserMessage> getAllEntities() {

		return repository.findAll();
	}
}
