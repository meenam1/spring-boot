package com.entity.dao;

import com.entity.pdo.EntityPDO;
import org.springframework.data.repository.CrudRepository;

public interface JPAEntityDAO extends CrudRepository<EntityPDO, Integer> {

    //Iterable<EntityPDO> findAll(Sort );
}
