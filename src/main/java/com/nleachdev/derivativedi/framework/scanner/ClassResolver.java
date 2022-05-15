package com.nleachdev.derivativedi.framework.scanner;

import java.util.Set;

public interface ClassResolver {
    Set<Class<?>> getRelevantClasses();
}
