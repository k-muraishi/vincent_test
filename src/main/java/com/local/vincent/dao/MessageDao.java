package com.local.vincent.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.local.vincent.entity.Message;
import com.local.vincent.repository.MessageRepository;

@Service
public class MessageDao {
	private final MessageRepository repository;
	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	public MessageDao(MessageRepository repository) {

		this.repository = repository;
	}

	/**
	 * 全件検索
	 * @return
	 */
	public List<Message> getAllEntities() {

		return repository.findAll();
	}

	/**
	 * メッセージ登録
	 */
	@Transactional
	public void insertMessage(String lineUserId, String messageText, Integer passwordId) {
		entityManager
				.createNativeQuery(
						"INSERT INTO vincent_test.message (line_user_id, message_text, password) VALUES (?, ?, ?)")
				.setParameter(1, lineUserId)
				.setParameter(2, messageText)
				.setParameter(3, passwordId)
				.executeUpdate();
	}
}
