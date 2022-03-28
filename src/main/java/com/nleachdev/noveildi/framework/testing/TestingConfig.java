package com.nleachdev.noveildi.framework.testing;

import com.nleachdev.noveildi.framework.annotation.Bean;
import com.nleachdev.noveildi.framework.annotation.Config;

@Config
public class TestingConfig {

    //@Bean
    public Integer whatever() {
        return 21;
    }

    @Bean
    public Integer anotherInteger() {
        return 32;
    }
}
