package com.local.vincent.repository;

import com.local.vincent.entity.Testentity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TestEntityRepository extends JpaRepository<Testentity, Long>{
}
