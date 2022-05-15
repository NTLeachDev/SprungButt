package com.nleachdev.derivativedi.framework.core;

import com.nleachdev.derivativedi.framework.config.ContainerConfig;
import com.nleachdev.derivativedi.framework.repository.BeanDependencyRepository;
import com.nleachdev.derivativedi.framework.repository.BeanMetadataRepository;
import com.nleachdev.derivativedi.framework.repository.BeanRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public enum Container {
    INSTANCE;

    public static Container getInstance() {
        return INSTANCE;
    }

    private static final Logger logger = LoggerFactory.getLogger(Container.class);
    private static final BeanDependencyRepository dependencyRepo;
    private static final BeanMetadataRepository metadataRepo;
    private static final BeanRepository beanRepo;
    private ContainerConfig config;

    static {
        dependencyRepo = new BeanDependencyRepository();
        metadataRepo = new BeanMetadataRepository();
        beanRepo = new BeanRepository();
    }

    public void startContainer(final ContainerConfig providedConfig) {
        this.config = providedConfig;
        clear();
    }

    private void setupBeanMetadata() {
        metadataRepo.setupBeanMetadata(config);
    }

    private static void clear() {
        dependencyRepo.clear();
        metadataRepo.clear();
        beanRepo.clear();
    }
}
