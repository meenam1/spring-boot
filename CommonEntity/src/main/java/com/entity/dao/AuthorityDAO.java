package com.entity.dao;

import com.entity.pdo.AuthorityPDO;
import com.entity.model.Authority;
import org.springframework.data.repository.CrudRepository;

public interface AuthorityDAO extends CrudRepository<AuthorityPDO, Integer> {

    default Authority findAllBy() {
        return findAllBy();
    }

    AuthorityPDO findById(int id);
}
