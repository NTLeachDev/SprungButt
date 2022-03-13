package com.nleachdev.testingstuff.config;

import com.nleachdev.sprungbutt.annotation.Thing;
import com.nleachdev.sprungbutt.annotation.ThingSetup;
import com.nleachdev.testingstuff.domain.SomePojo;
import org.h2.jdbcx.JdbcDataSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;

import javax.sql.DataSource;

@ThingSetup
public class ThingConfig {

    @Thing
    public NamedParameterJdbcTemplate namedTemplate() {
        final DataSource dataSource = getDatasource();
        return new NamedParameterJdbcTemplate(dataSource);
    }

    private DataSource getDatasource() {
        final SingleConnectionDataSource dataSource = new SingleConnectionDataSource();
        dataSource.setUrl("jdbc:h2:mem:testdb");
        dataSource.setUsername("sa");
        dataSource.setPassword("password");
        return dataSource;
    }

    @Thing
    public Integer someNumber() {
        return 10;
    }

    @Thing
    public SomePojo somePojo() {
        return new SomePojo(23, "who cares");
    }

    public Long notAThing() {
        return 20L;
    }
}
