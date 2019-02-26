package com.imooc.ad.utils;

import java.util.Map;
import java.util.function.Supplier;

public class CommonUnits {

    public static <K, V> V getorCreate(K key, Map<K, V> map, Supplier<V> factory) {
        return map.computeIfAbsent(key, k -> factory.get());
    }
}
