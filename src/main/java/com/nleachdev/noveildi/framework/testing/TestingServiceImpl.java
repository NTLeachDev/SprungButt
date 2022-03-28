package com.nleachdev.noveildi.framework.testing;

import com.nleachdev.noveildi.framework.annotation.Component;
import com.nleachdev.noveildi.framework.annotation.Get;
import com.nleachdev.noveildi.framework.annotation.Inject;
import com.nleachdev.noveildi.framework.annotation.Named;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class TestingServiceImpl implements TestingService {
    private static final Logger logger = LoggerFactory.getLogger(TestingServiceImpl.class);
    private final Integer whatever;


    // @Get("person.name")
    private final String personName;

    @Inject
    public TestingServiceImpl(@Named("whatever") final Integer whatever,
                              @Get("person.name") final String personName) {
        this.whatever = whatever;
        this.personName = personName;
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
