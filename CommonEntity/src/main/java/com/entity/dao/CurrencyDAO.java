package com.entity.dao;

import com.entity.model.Currency;
import com.entity.pdo.CurrencyPDO;
import org.springframework.data.repository.CrudRepository;

public interface CurrencyDAO extends CrudRepository<CurrencyPDO, Integer>  {

    default Currency findAllBy() {
        return findAllBy();
    }

    Currency findAllById(int id);
}
