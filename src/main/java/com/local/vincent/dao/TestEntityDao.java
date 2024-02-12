package com.local.vincent.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.local.vincent.entity.Testentity;
import com.local.vincent.repository.TestEntityRepository;

@Service
public class TestEntityDao {
	private final TestEntityRepository repository;

	@Autowired
	public TestEntityDao(TestEntityRepository repository) {

		this.repository = repository;
	}

	public List<Testentity> getAllEntities() {

		return repository.findAll();
	}
}
