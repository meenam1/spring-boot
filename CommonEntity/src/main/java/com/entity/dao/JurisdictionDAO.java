package com.entity.dao;

import com.entity.pdo.JurisdictionPDO;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JurisdictionDAO extends JpaRepository<JurisdictionPDO, Integer> {

    JurisdictionPDO findByCode(String code);
}
