package com.nleachdev.derivativedi.framework.component

import com.nleachdev.derivativedi.framework.annotation.Component
import com.nleachdev.derivativedi.framework.annotation.Get
import com.nleachdev.derivativedi.framework.annotation.Inject
import com.nleachdev.derivativedi.framework.annotation.Named
import org.slf4j.Logger
import org.slf4j.LoggerFactory

@Component
class TestingDependentService {
    private static final Logger logger = LoggerFactory.getLogger(TestingDependentService.class);
    private final TestingService testingService
    private final String name

    @Inject
    TestingDependentService(@Named("TestingServiceImpl") final TestingService testingService,
                            @Get("person.name") String name) {
        this.testingService = testingService
        this.name = name
    }

    void sure() {
        logger.info("yep")
        testingService.doSomeStuff()
    }

    String getName() {
        return name
    }
}
