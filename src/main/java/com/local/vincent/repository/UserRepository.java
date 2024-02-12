package com.local.vincent.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import com.local.vincent.entity.User;

@Transactional
public interface UserRepository extends JpaRepository<User, Integer> {

	/**
	 * ineユーザーIDでユーザー検索
	 */
	User findByLineUserId(String line_user_id);

}
