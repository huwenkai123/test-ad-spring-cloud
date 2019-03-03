package com.imooc.ad.utils;

import java.util.Map;
import java.util.function.Supplier;

public class CommonUnits {

    public static <K, V> V getorCreate(K key, Map<K, V> map, Supplier<V> factory) {
        return map.computeIfAbsent(key, k -> factory.get());
    }

    public static String stringConcat(String... arg) {
        StringBuffer stringBuffer = new StringBuffer();
        for (String str : arg) {
            stringBuffer.append(str);
            stringBuffer.append("-");
        }
        stringBuffer.deleteCharAt(stringBuffer.length() - 1);
        return stringBuffer.toString();
    }
}
