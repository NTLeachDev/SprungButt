package com.nleachdev.testingstuff;


import com.nleachdev.sprungbutt.SprungButt;
import com.nleachdev.testingstuff.domain.SomePojo;
import com.nleachdev.testingstuff.service.SomeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class SomeApplication {
    private static final Logger logger = LoggerFactory.getLogger(SomeApplication.class);

    public static void main(final String[] args) throws IOException {
        SprungButt.startEnvironment("com.nleachdev.testingstuff");

        final SomeService service = (SomeService) SprungButt.getThing(SomeService.class);
        final SomePojo somePojo = service.getSomePojo();
        logger.info("Some Pojo has been returned: {}", somePojo);
    }
}
