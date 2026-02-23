package com.easylive.utils;

import com.easylive.entity.enums.DateTimePatternEnum;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * @Description: 时间工具类
 * @Author: KunSpireUp
 */
public class DateUtils {

    private static final Object lockObj = new Object();

    private static Map<String, ThreadLocal<SimpleDateFormat>> sdfMap = new HashMap<>();

    private static SimpleDateFormat getSdf(final String pattern) {
        ThreadLocal<SimpleDateFormat> threadLocal = sdfMap.get(pattern);
        if (threadLocal == null) {
            synchronized (lockObj) {
                threadLocal = sdfMap.get(pattern);
                if (threadLocal == null) {
                    threadLocal = new ThreadLocal<SimpleDateFormat>() {
                        @Override
                        protected SimpleDateFormat initialValue() {
                            return new SimpleDateFormat(pattern);
                        }
                    };
                    sdfMap.put(pattern, threadLocal);
                }
            }
        }
        return threadLocal.get();
    }

    public static String format(Date date, String pattern) {
        return getSdf(pattern).format(date);
    }

    public static Date parse(String date, String pattern) {
        try {
            return getSdf(pattern).parse(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getBeforeDate(Integer day) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, -day);
        return format(calendar.getTime(), DateTimePatternEnum.YYYY_MM_DD.getPattern());
    }

    public static List<String> getBeforeDates(Integer beforeDays) {
        LocalDate endDate =  LocalDate.now();
        List<String> dateList = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        for (int i = beforeDays; i > 0; i--) {
            dateList.add(endDate.minusDays(i).format(formatter));
        }
        return dateList;
    }

    public static void main(String[] args) {
        System.out.println(getBeforeDates(7));
    }
}
