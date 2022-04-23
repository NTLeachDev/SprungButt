package com.nleachdev.derivativedi.framework.component

import com.nleachdev.derivativedi.framework.annotation.Component
import org.slf4j.Logger
import org.slf4j.LoggerFactory

@Component
class OtherTestingServiceImpl implements TestingService {
    private static final Logger logger = LoggerFactory.getLogger(OtherTestingServiceImpl.class);

    @Override
    void doSomeStuff() {
        logger.info("yert")
    }

    @Override
    Integer getSomeInt() {
        return null
    }
}