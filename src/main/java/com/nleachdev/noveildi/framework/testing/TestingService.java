package com.nleachdev.noveildi.framework.testing;

import com.nleachdev.noveildi.framework.annotation.Component;
import com.nleachdev.noveildi.framework.annotation.Inject;

@Component
public class TestingService {
    private final TestingDependencyService dependencyService;

    @Inject
    public TestingService(final TestingDependencyService dependencyService) {
        this.dependencyService = dependencyService;
    }
}
