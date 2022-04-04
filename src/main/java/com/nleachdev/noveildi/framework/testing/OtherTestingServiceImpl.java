package com.nleachdev.noveildi.framework.testing;

import com.nleachdev.noveildi.framework.annotation.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class OtherTestingServiceImpl implements TestingService {
    private static final Logger logger = LoggerFactory.getLogger(OtherTestingServiceImpl.class);

    @Override
    public void doSomeStuff() {
        logger.info("yert");
    }

    @Override
    public Integer getANumber() {
        return null;
    }
}
