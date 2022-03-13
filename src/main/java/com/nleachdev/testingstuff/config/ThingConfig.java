package com.nleachdev.testingstuff.config;

import com.nleachdev.sprungbutt.annotation.GetProperty;
import com.nleachdev.sprungbutt.annotation.Thing;
import com.nleachdev.sprungbutt.annotation.ThingSetup;
import com.nleachdev.testingstuff.domain.SomePojo;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;

import javax.sql.DataSource;

@ThingSetup
public class ThingConfig {

    @GetProperty("person.name")
    public String name;

    @GetProperty("person.birth.year")
    public Integer birthYear;

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
    public SomePojo somePojo() {
        return new SomePojo(23, "who cares");
    }

    @Thing
    public String name() {
        return name;
    }

    @Thing
    public Integer birthYear() {
        return birthYear;
    }

    public Long notAThing() {
        return 20L;
    }
}
