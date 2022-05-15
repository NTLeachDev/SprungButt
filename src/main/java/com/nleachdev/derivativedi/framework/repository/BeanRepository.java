package com.nleachdev.derivativedi.framework.repository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class BeanRepository {
    private static final Logger logger = LoggerFactory.getLogger(BeanRepository.class);
    private final Map<String, Object> instancePerBeanName;

    public BeanRepository() {
        this.instancePerBeanName = new HashMap<>();
    }

    public void clear() {
        this.instancePerBeanName.clear();
    }
}
