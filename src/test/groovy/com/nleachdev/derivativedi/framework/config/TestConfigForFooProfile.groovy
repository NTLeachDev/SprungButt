package com.nleachdev.derivativedi.framework.config

import com.nleachdev.derivativedi.framework.annotation.Bean
import com.nleachdev.derivativedi.framework.annotation.Config
import com.nleachdev.derivativedi.framework.annotation.Profile

@Config
@Profile(profileName = "Foo")
class TestConfigForFooProfile {

    @Bean
    BigDecimal someBigDecimal() {
        return BigDecimal.ONE;
    }
}
