package com.nleachdev.noveildi.framework.util;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public final class CollectionUtils {
    private CollectionUtils(){}

    public static <K, V> void addToMapSet(final Map<K, Set<V>> map, final K key, final V value) {
        if (map == null || key == null || value == null) {
            return;
        }

        map.compute(key, (k, v) -> {
            if (v == null) {
                v = new HashSet<>();
            }
            v.add(value);
            return v;
        });
    }
}
