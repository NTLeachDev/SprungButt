package com.nleachdev.noveildi.framework.testing;

import com.nleachdev.noveildi.framework.annotation.*;

@Component
@Config
public class TestingConfig {

    @Bean
    public Integer whatever(@Get("some.property.name") final Integer dependencyService) {
        return 21;
    }
}
