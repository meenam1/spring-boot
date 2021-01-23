package com.entity.pdo;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.usertype.UserType;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Objects;

public class JsonNodeType implements UserType {
    private final ObjectMapper objectMapper;

    public JsonNodeType() {
        objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules();
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
    }

    @Override
    public int[] sqlTypes() {
        return new int[]{Types.JAVA_OBJECT};
    }

    @Override
    public Class returnedClass() {
        return JsonNode.class;
    }

    @Override
    public boolean equals(final Object x, final Object y) throws HibernateException {
        return Objects.equals(x, y);
    }

    @Override
    public int hashCode(final Object x) throws HibernateException {
        return Objects.nonNull(x) ? x.hashCode() : 0;
    }

    @Override
    public Object nullSafeGet(ResultSet resultSet, String[] strings,
     SharedSessionContractImplementor sharedSessionContractImplementor,
      Object o) throws HibernateException, SQLException {
        //return null;
        final String json = resultSet.getString(strings[0]);
        if (StringUtils.isBlank(json)) {
            return JsonNodeFactory.instance.objectNode();
        }
        try {
            return objectMapper.readTree(json);
        } catch (final Exception error) {
            //log.error("Error getting JsonNode: {}" + error.getMessage());
            //throw new JpaException("Error getting JsonNode: " + json, error);
        }
        return  null;
    }

    @Override
    public void nullSafeSet(PreparedStatement preparedStatement,
                            Object o, int i,
                            SharedSessionContractImplementor sharedSessionContractImplementor)
            throws HibernateException, SQLException {

        if (Objects.isNull(o)) {
            preparedStatement.setNull(i, Types.OTHER);
        } else {
            try {
                preparedStatement.setObject(i, objectMapper.writeValueAsString(o), Types.OTHER);
            } catch (final Exception error) {
                //log.error("Error while converting JsonNode to String: {}" + error.getMessage());
                //throw new JpaException("Error converting JsonNode to String.", error);
            }
        }

    }

    @Override
    public boolean isMutable() {
        return true;
    }

    /*@Override
    public Object nullSafeGet(final ResultSet rs, final String[] names,
                              final SessionImplementor session,
                              final Object owner) throws HibernateException, SQLException {

        return null;
    }

    @Override
    public void nullSafeSet(final PreparedStatement st,
                            final Object value, final int index,
                            final SessionImplementor session) throws HibernateException, SQLException {

    }*/
    @Override
    public Object deepCopy(final Object value) throws HibernateException {
        if (Objects.isNull(value) || !(value instanceof JsonNode)) {
            return null;
        }
        try {
            final String copy = objectMapper.writeValueAsString(value);
            return objectMapper.readTree(copy);
        } catch (final Exception error) {
            //log.error("Error while creating deep copy of JsonNode: {}" + error.getMessage());
            //throw new JpaException("Error creating a deep copy of JsonNode.", error);
        }
        return null;
    }

    @Override
    public Serializable disassemble(final Object value) throws HibernateException {
        if (Objects.isNull(value)) {
            return null;
        }
        final Object copy = deepCopy(value);
        if (copy instanceof Serializable) {
            return (Serializable) copy;
        }
        //throw new JpaException("Could not disassemble '" + value.getClass() + "' - not Serializable!");
        return null;
    }

    @Override
    public Object assemble(final Serializable cached, final Object owner) throws HibernateException {
        return deepCopy(cached);
    }

    @Override
    public Object replace(final Object original, final Object target, final Object owner) throws HibernateException {
        return deepCopy(original);
    }
}

