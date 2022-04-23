package com.nleachdev.derivativedi.framework.config;

import com.nleachdev.derivativedi.framework.annotation.Bean;
import com.nleachdev.derivativedi.framework.annotation.Config;
import com.nleachdev.derivativedi.framework.annotation.Profile;

import java.util.Arrays;
import java.util.List;

@Config
@Profile(profileName = "Test")
public class TestConfigForTestProfile {

    @Bean
    public List<String> stringList() {
        return Arrays.asList("Foo", "Bar");
    }
}
