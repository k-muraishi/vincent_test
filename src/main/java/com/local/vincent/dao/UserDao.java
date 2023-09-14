package com.local.vincent.dao;

import com.local.vincent.entity.User;
import com.local.vincent.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Service
public class UserDao {
    private final UserRepository repository;
    private final String userIdSeq = "user_id_seq";
    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    public UserDao(UserRepository repository) {

        this.repository = repository;
    }

    /**
    ユーザー全件検索
    */
    public List<User> getAllEntities() {

        return repository.findAll();
    }

    /**
     * ID検索
     */
    public User getUserById(Integer id){
        return repository.getById(id);
    }

    /**
     * lineユーザーID検索
     */
    public User findBylineUserId(String lineUserId){
        return repository.findByLineUserId(lineUserId);
    }

    /**
     * 新規登録
     */
    @Transactional
    public void insertUserWithSeq(String lineUserId) {
        entityManager.createNativeQuery("INSERT INTO vincent_test.user (line_user_id, delete_flag) VALUES (?, 0)")
            .setParameter(1, lineUserId)
            .executeUpdate();
    }

    /**
     * ユーザー削除
     */
    @Transactional
    public void deleteUser(String lineUserId) {
        entityManager.createNativeQuery("UPDATE vincent_test.user SET delete_flag = 1 WHERE line_user_id = ?")
            .setParameter(1, lineUserId)
            .executeUpdate();
    }
}

