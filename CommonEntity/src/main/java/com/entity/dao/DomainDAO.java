package com.entity.dao;

import com.entity.pdo.DomainPDO;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DomainDAO extends JpaRepository<DomainPDO, Integer> {
}
