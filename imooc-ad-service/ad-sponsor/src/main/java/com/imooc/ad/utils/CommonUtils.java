package com.imooc.ad.utils;


import com.imooc.ad.ad.exception.AdExpection;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.time.DateUtils;


import javax.xml.crypto.Data;
import java.util.Date;

public class CommonUtils {
    private static String[] parsePatterns = {
            "yyyy-MM-dd", "yyyy/MM/dd", "yyyy.MM.dd"
    };

    public static String md5(String value) {
        return DigestUtils.md5Hex(value).toUpperCase();
  }
    public static Date parseStringDate(String dateString) throws AdExpection {
        try {
            return DateUtils.parseDate(dateString, parsePatterns);
        }catch (Exception e) {
            throw new AdExpection(e.getMessage());
        }

    }
}
