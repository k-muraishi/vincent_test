package com.local.vincent.dao;

import com.local.vincent.entity.Password;
import com.local.vincent.repository.PasswordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Service
public class PasswordDao {
    private final PasswordRepository repository;
    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    public PasswordDao(PasswordRepository repository) {

        this.repository = repository;
    }

    /**
     * 全件検索
     * @return
     */
    public List<Password> getAllEntities() {

        return repository.findAll();
    }

    /**
     * パスワード登録
     */
    @Transactional
    public Integer insertPassword(String password){
        Integer id = entityManager.createNativeQuery("INSERT INTO vincent_test.password (password) values(?) RETURNING id")
            .setParameter(1, password)
            .executeUpdate();
        
        return id;
    }
}
