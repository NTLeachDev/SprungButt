package com.nleachdev.noveildi.framework.testing;

import com.nleachdev.noveildi.framework.annotation.Component;
import com.nleachdev.noveildi.framework.annotation.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class TestingServiceImpl implements TestingService {
    private static final Logger logger = LoggerFactory.getLogger(TestingServiceImpl.class);
    private final Integer whatever;

/*    @Get("person.name")
    private String personName;*/

    @Inject
    public TestingServiceImpl(final Integer whatever) {
        this.whatever = whatever;
    }

    @Override
    public void doSomeStuff() {
        logger.info("Doing some stuff");
    }

    @Override
    public Integer getANumber() {
        return whatever;
    }
}
