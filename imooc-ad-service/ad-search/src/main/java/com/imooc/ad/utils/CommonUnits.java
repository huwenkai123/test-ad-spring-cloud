package com.imooc.ad.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.time.DateUtils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.function.Supplier;

@Slf4j
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

    public static Date parseStringDate(String dateString) {
        try {
            DateFormat dateFormat = new SimpleDateFormat(
                    "EEE MMM dd HH:mm:ss zzz yyyy", Locale.US);
            return DateUtils.addHours(dateFormat.parse(dateString), -8);
        }catch (ParseException ex) {
            log.error("pass String error");
            return null;
        }
    }
}
