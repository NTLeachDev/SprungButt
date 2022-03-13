package com.nleachdev.testingstuff.config;

import com.nleachdev.sprungbutt.annotation.Thing;
import com.nleachdev.sprungbutt.annotation.ThingSetup;
import com.nleachdev.testingstuff.domain.SomePojo;

@ThingSetup
public class ThingConfig {

    @Thing
    public Integer someNumber() {
        return 10;
    }

    @Thing
    public SomePojo somePojo() {
        return new SomePojo(23, "who cares");
    }
}
