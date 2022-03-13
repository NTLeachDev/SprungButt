package com.nleachdev.testingstuff.service;

import com.nleachdev.sprungbutt.annotation.InjectThings;
import com.nleachdev.sprungbutt.annotation.Thing;
import com.nleachdev.testingstuff.dao.SomeDao;
import com.nleachdev.testingstuff.domain.SomePojo;

@Thing
public class SomeService {
    private final SomeDao someDao;

    @InjectThings
    public SomeService(final SomeDao someDao) {
        this.someDao = someDao;
    }

    public SomePojo getSomePojo() {
        return someDao.getSomePojo();
    }
}
