package com.nleachdev.testingstuff.dao;

import com.nleachdev.sprungbutt.annotation.InjectThings;
import com.nleachdev.sprungbutt.annotation.Thing;
import com.nleachdev.testingstuff.domain.SomePojo;

@Thing
public class SomeDao {
    public SomePojo somePojo;

    @InjectThings
    public SomeDao(final SomePojo somePojo) {
        this.somePojo = somePojo;
    }

    public void doStuff() {
        
    }
}
