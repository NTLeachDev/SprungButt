package com.nleachdev.derivativedi.framework.component

import com.nleachdev.derivativedi.framework.annotation.Component
import com.nleachdev.derivativedi.framework.annotation.Get
import com.nleachdev.derivativedi.framework.annotation.Inject
import com.nleachdev.derivativedi.framework.annotation.Named
import org.slf4j.Logger
import org.slf4j.LoggerFactory

@Component
class TestingServiceImpl implements TestingService {
    private static final Logger logger = LoggerFactory.getLogger(TestingServiceImpl.class)
    private final Integer someInt
    private final String personName

    @Inject
    TestingServiceImpl(@Named("someInt") final Integer someInt,
                              @Get("person.name") final String personName) {
        this.someInt = someInt
        this.personName = personName
    }

    @Override
    void doSomeStuff() {
        logger.info("Doing some stuff with {}", personName)
    }

    @Override
    Integer getSomeInt() {
        return someInt
    }
}
