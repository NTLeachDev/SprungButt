package com.nleachdev.derivativedi.framework.config

import com.nleachdev.derivativedi.framework.annotation.Bean
import com.nleachdev.derivativedi.framework.annotation.Config

@Config
class TestConfig {

    @Bean
    Integer someInt() {
        return 21
    }

    @Bean
    Integer anotherInt() {
        return 32
    }
}
