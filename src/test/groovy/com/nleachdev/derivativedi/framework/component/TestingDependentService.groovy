package com.nleachdev.derivativedi.framework.component

import com.nleachdev.derivativedi.framework.annotation.Component
import com.nleachdev.derivativedi.framework.annotation.Inject
import com.nleachdev.derivativedi.framework.annotation.Named
import org.slf4j.Logger
import org.slf4j.LoggerFactory

@Component
class TestingDependentService {
    private static final Logger logger = LoggerFactory.getLogger(TestingDependentService.class);
    private final TestingService testingService

    @Inject
    TestingDependentService(@Named("TestingServiceImpl") final TestingService testingService) {
        this.testingService = testingService
    }

    void sure() {
        logger.info("yep")
        testingService.doSomeStuff()
    }
}
