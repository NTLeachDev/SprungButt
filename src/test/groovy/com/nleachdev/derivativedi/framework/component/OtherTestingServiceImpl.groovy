package com.nleachdev.derivativedi.framework.component

import com.nleachdev.derivativedi.framework.annotation.Component
import com.nleachdev.derivativedi.framework.annotation.GetProp
import com.nleachdev.derivativedi.framework.annotation.Inject
import org.slf4j.Logger
import org.slf4j.LoggerFactory

@Component
class OtherTestingServiceImpl implements TestingService {
    private static final Logger logger = LoggerFactory.getLogger(OtherTestingServiceImpl.class)
    private Long someLong

    @Inject
    OtherTestingServiceImpl(@GetProp("some.prop.that.doesnt.exist:26") final Long someLong) {
        this.someLong = someLong
    }

    @Override
    void doSomeStuff() {
        logger.info("yert")
    }

    @Override
    Integer getSomeInt() {
        return null
    }

    Long getSomeLong() {
        return someLong
    }
}