package com.entity.dao;

import com.entity.model.Entity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class EntityDAO {

    @Autowired
    final JdbcTemplate jdbcTemplate;

    public EntityDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void saveEntity(final Entity entity) {
        String sql = "insert into entity values(" + entity.getId() + ",'"
                + entity.getCode() + "','" + entity.getName() + "')";
        System.out.println(sql);
        jdbcTemplate.update(sql);
    }

    public Entity updateEntity(final Entity entity) {
        String query = "update entity set name='" + entity.getName() + "',code='" + entity.getCode() + "' where id='" + entity.getId() + "' ";
        jdbcTemplate.update(query);
        return getEntityById(entity.getId());
    }

    public int deleteEntity(final Integer id) {
        String query = "delete from entity where id='" + id + "' ";
        return jdbcTemplate.update(query);
    }

    public Entity getEntityById(final Integer id) {
        String query = "select * from entity where id ='" + id + "' ";
        return jdbcTemplate.queryForObject(query, new EntityMapper());

    }

    public List<Entity> getEntities() {
        String query = "select * from entity";
        return jdbcTemplate.query(query, new EntityMapper());

    }

    public void saveAllEntities(List<Entity> entities) {
        for (Entity entity : entities) {
            //String sql = "insert into entity values(" + entity.getId() + ",'" + entity.getCode() + "','" + entity.getName() + "')";
            //String data = entity.getData().toString();
            String sql = "insert into entity values(" + entity.getId() + ",'" + entity.getCode() + "','" + entity.getName() + "')";
            System.out.println(sql);
            jdbcTemplate.update(sql);

        }
    }

    private class EntityMapper implements RowMapper<Entity> {

        @Override
        public Entity mapRow(ResultSet resultSet, int i) throws SQLException {
            Entity entity = new Entity();
            entity.setId(resultSet.getInt("id"));
            entity.setName(resultSet.getString("name"));
            entity.setCode(resultSet.getString("code"));
            return entity;
        }
    }
}
