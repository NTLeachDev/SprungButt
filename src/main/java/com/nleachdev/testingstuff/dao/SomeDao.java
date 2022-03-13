package com.nleachdev.testingstuff.dao;

import com.nleachdev.sprungbutt.annotation.InjectThings;
import com.nleachdev.sprungbutt.annotation.Thing;
import com.nleachdev.testingstuff.domain.SomePojo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;

@Thing
public class SomeDao {
    private static final Logger logger = LoggerFactory.getLogger(SomeDao.class);
    private final SomePojo somePojo;
    private final NamedParameterJdbcTemplate namedTemplate;

    @InjectThings
    public SomeDao(final SomePojo somePojo,
                   final NamedParameterJdbcTemplate namedTemplate) {
        this.somePojo = somePojo;
        this.namedTemplate = namedTemplate;
    }

    public SomePojo getSomePojo() {
        return somePojo;
    }

    public Integer doStuff() {
        final String schema =
                " CREATE SCHEMA TEST_SCHEMA AUTHORIZATION sa";

        namedTemplate.getJdbcOperations().execute(schema);

        final String sql =
                " CREATE TABLE TESTING (" +
                        " NAME VARCHAR(20)," +
                        " ID NUMBER" +
                        ")";
        namedTemplate.getJdbcOperations().execute(sql);

        final String insert =
                " INSERT INTO TESTING" +
                " (NAME, ID)" +
                " VALUES (:name, :id)";

        final SqlParameterSource params = new MapSqlParameterSource("name", "Nicholas Leach")
                .addValue("id", 26);
        namedTemplate.update(insert, params);

        final String select =
                " SELECT ID" +
                " FROM TESTING" +
                " LIMIT 1";

        try {
            return namedTemplate.getJdbcOperations().queryForObject(select, Integer.class);
        } catch (final IncorrectResultSizeDataAccessException e) {
            logger.error("Error selecting id", e);
            return null;
        }
    }

    public String getName() {
        final String sql =
                " SELECT NAME" +
                " FROM TESTING" +
                " LIMIT 1";

        try {
            return namedTemplate.getJdbcOperations().queryForObject(sql, String.class);
        } catch (final IncorrectResultSizeDataAccessException e) {
            logger.error("Error selecting id", e);
            return null;
        }
    }
}
