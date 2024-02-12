package com.local.vincent.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.local.vincent.entity.UserMessage;

public interface UserMessageRepository extends JpaRepository<UserMessage, Integer> {
}
