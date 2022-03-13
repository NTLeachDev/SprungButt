package com.nleachdev.testingstuff.service;

import com.nleachdev.sprungbutt.annotation.InjectThings;
import com.nleachdev.sprungbutt.annotation.Thing;
import com.nleachdev.testingstuff.dao.SomeDao;
import com.nleachdev.testingstuff.domain.SomePojo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Thing
public class SomeService {
    private static final Logger logger = LoggerFactory.getLogger(SomeService.class);
    private final SomeDao someDao;

    @InjectThings
    public SomeService(final SomeDao someDao) {
        this.someDao = someDao;
    }

    public SomePojo getSomePojo() {
        return someDao.getSomePojo();
    }

    public void hitDB() {
        final Integer id = someDao.doStuff();
        logger.info("ID pulled: {}", id);

        final String name = someDao.getName();
        logger.info("Name pulled: {}", name);
    }
}
