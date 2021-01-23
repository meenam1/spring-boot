package com.adjustment.dao;

import com.adjustment.pdo.AdjustmentPDO;
import org.springframework.data.repository.CrudRepository;

public interface AdjustmentDAO extends CrudRepository<AdjustmentPDO, Integer> {

    //Iterable<EntityPDO> findAll(Sort );
}
