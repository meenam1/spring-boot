package com.entity.dao;

import com.entity.model.Field;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class TableDAO {

    @Autowired
    final JdbcTemplate jdbcTemplate;

    public TableDAO(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void createTable(final String tableName, final List<Field> fields) {
        final StringBuilder createTableQuery = new StringBuilder();
        createTableQuery.append("DROP TABLE IF EXISTS " + tableName + ";");
        createTableQuery.append("CREATE TABLE " + tableName + "(");
        fields.forEach(field -> createTableQuery.append(field.getName() + " " + field.getType() + ","));
        createTableQuery.append("PRIMARY KEY (id));");
        jdbcTemplate.execute(createTableQuery.toString());
    }

}
