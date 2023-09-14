package com.local.vincent.repository;

import com.local.vincent.entity.UserMessage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserMessageRepository  extends JpaRepository<UserMessage, Integer> {
}
