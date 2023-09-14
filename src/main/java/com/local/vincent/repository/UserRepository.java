package com.local.vincent.repository;

import com.local.vincent.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface UserRepository extends JpaRepository<User, Integer> {

    /**
     * ineユーザーIDでユーザー検索
     */
    User findByLineUserId (String line_user_id);

}
