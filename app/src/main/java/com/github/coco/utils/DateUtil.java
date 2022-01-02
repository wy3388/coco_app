package com.github.coco.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

/**
 * Created on 2022/1/2.
 *
 * @author wy
 */
public class DateUtil {
    public static String format(Long second) {
        return SimpleDateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.MEDIUM).format(second);
    }
}
