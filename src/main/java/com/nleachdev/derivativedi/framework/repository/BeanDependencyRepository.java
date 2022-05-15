package com.nleachdev.derivativedi.framework.repository;

import com.nleachdev.derivativedi.framework.domain.ConfiguredBean;
import com.nleachdev.derivativedi.framework.domain.Dependency;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;

public class BeanDependencyRepository {
    private static final Logger logger = LoggerFactory.getLogger(BeanDependencyRepository.class);
    private final Map<String, Dependency[]> depsPerBean;
    private final Map<String, Constructor<?>> constructorPerBean;
    private final Map<String, ConfiguredBean> configuredBeanMap;

    public BeanDependencyRepository() {
        this.depsPerBean = new HashMap<>();
        this.constructorPerBean = new HashMap<>();
        this.configuredBeanMap = new HashMap<>();
    }

    public void clear() {
        this.depsPerBean.clear();
        this.constructorPerBean.clear();
        this.configuredBeanMap.clear();
    }
}
