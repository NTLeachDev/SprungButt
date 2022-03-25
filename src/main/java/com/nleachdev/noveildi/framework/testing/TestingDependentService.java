package com.nleachdev.noveildi.framework.testing;

import com.nleachdev.noveildi.framework.annotation.Component;
import com.nleachdev.noveildi.framework.annotation.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class TestingDependentService {
    private static final Logger logger = LoggerFactory.getLogger(TestingDependentService.class);
    private final TestingService testingService;

    @Inject
    public TestingDependentService(final TestingService testingService) {
        this.testingService = testingService;
    }

    public void sure() {
        logger.info("yep");
        testingService.doSomeStuff();
    }
}
