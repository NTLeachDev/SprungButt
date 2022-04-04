package com.nleachdev.noveildi.framework;

import com.nleachdev.noveildi.framework.core.Container;
import com.nleachdev.noveildi.framework.model.ContainerConfiguration;
import com.nleachdev.noveildi.framework.testing.TestingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class Testing {
    private static final Logger logger = LoggerFactory.getLogger(Testing.class);

    public static void main(final String[] args) {
        final ContainerConfiguration config = ContainerConfiguration.getConfig(Testing.class);
        Container.getInstance().startContainer(config);
        final TestingService testingService = Container.getInstance().getBean(TestingService.class, "TestingServiceImpl");
        testingService.doSomeStuff();
        logger.info("A number: {}", testingService.getANumber());
    }
}