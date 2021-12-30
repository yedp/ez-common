package com.github.yedp.ez.common.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.TriggerContext;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.scheduling.support.SimpleTriggerContext;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author yedp
 * <p> date utils</p>
 */
public class DateUtils {
    private final static Logger log = LoggerFactory.getLogger(DateUtils.class);

    public static final String PATTERN_YM = "yyyy-MM";
    public static final String PATTERN_YMD = "yyyy-MM-dd";
    public static final String PATTERN_STANDARD = "yyyy-MM-dd HH:mm:ss";
    public static final String PATTERN_OBLIQUE = "yyyy/MM/dd HH:mm:ss";
    public static final String PATTERN_MILLIS = "yyyy-MM-dd HH:mm:ss:SSS";
    public static final String[] PATTERN_ARRAY = new String[]{PATTERN_STANDARD, "yyyy/MM/dd HH:mm:ss", PATTERN_YMD, "yyyy/MM/dd", PATTERN_YM, "yyyy-MM", PATTERN_MILLIS, "yyyy/MM/dd HH:mm:ss:SSS"};
//1、date str transform ---------------------------------------------------------------------------------------------------

    /**
     * <p>transform date to str </p>
     *
     * @param date    need transform date
     * @param pattern transform pattern
     * @return date string
     */
    public static String format(Date date, String pattern) {
        if (date == null || pattern == null) {
            return "";
        }
        return new SimpleDateFormat(pattern).format(date);
    }

    /**
     * <p>transform date to str </p>
     *
     * @param date need transform date
     * @return date string
     */
    public static String format(Date date) {
        return format(date, PATTERN_STANDARD);
    }


    /**
     * <p>transform str to date </p>
     *
     * @param timeStr time tring
     * @param pattern string pattern
     * @return date
     * @throws ParseException parse date Exception
     */
    public static Date parse(String timeStr, String pattern) throws ParseException {
        if (timeStr == null) {
            return null;
        }
        return new SimpleDateFormat(pattern).parse(timeStr);
    }

    /**
     * <p>transform str to date </p>
     *
     * @param timeStr time string
     * @return date 日期
     */
    public static Date parse(String timeStr) {
        if (timeStr.indexOf("/") > 0) {
            try {
                //数据库处理的优先级高些
                return parse(timeStr, PATTERN_OBLIQUE);
            } catch (ParseException e) {
            }
        }

        for (String parrern : PATTERN_ARRAY) {
            try {
                return parse(timeStr, parrern);
            } catch (Exception e) {
            }
        }
        return null;
    }

    //2、日期获取-------------------------------------------------------------------------------------------------

    /**
     * <p> get date field</p>
     *
     * @param date      date need
     * @param dateField date field
     * @return int  filed value
     */
    public static int getDateFieldValue(Date date, int dateField) {
        if (date == null) {
            return -1;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(dateField);
    }

    public static int getMinute(Date date) {
        return getDateFieldValue(date, Calendar.MINUTE);
    }

    public static int getHour(Date date) {
        return getDateFieldValue(date, Calendar.HOUR);
    }

    public static int getDayOfWeek(Date date) {
        return getDateFieldValue(date, Calendar.DAY_OF_WEEK) - 1;
    }

    public static int getDayOfMonth(Date date) {
        return getDateFieldValue(date, Calendar.DAY_OF_MONTH);
    }

    public static int getDayOfYear(Date date) {
        return getDateFieldValue(date, Calendar.DAY_OF_YEAR);
    }

    public static int getWeekOfMonth(Date date) {
        return getDateFieldValue(date, Calendar.WEEK_OF_MONTH);
    }

    public static int getWeekOfYear(Date date) {
        return getDateFieldValue(date, Calendar.WEEK_OF_YEAR);
    }

    public static int getMonth(Date date) {
        return getDateFieldValue(date, Calendar.MONTH) + 1;
    }

    public static int getYear(Date date) {
        return getDateFieldValue(date, Calendar.YEAR);
    }

    public static int getMinute() {
        return getDateFieldValue(curTime(), Calendar.MINUTE);
    }

    public static int getHour() {
        return getDateFieldValue(curTime(), Calendar.HOUR);
    }

    public static int getDayOfWeek() {
        return getDateFieldValue(curTime(), Calendar.DAY_OF_WEEK);
    }

    public static int getDayOfMonth() {
        return getDateFieldValue(curTime(), Calendar.DAY_OF_MONTH);
    }

    public static int getDayOfYear() {
        return getDateFieldValue(curTime(), Calendar.DAY_OF_YEAR);
    }

    public static int getWeekOfMonth() {
        return getDateFieldValue(curTime(), Calendar.WEEK_OF_MONTH);
    }

    public static int getWeekOfYear() {
        return getDateFieldValue(curTime(), Calendar.WEEK_OF_YEAR);
    }

    public static int getMonth() {
        return getDateFieldValue(curTime(), Calendar.MONTH) + 1;
    }

    public static int getYear() {
        return getDateFieldValue(curTime(), Calendar.YEAR);
    }


    public static Date curTime() {
        return Calendar.getInstance().getTime();
    }

    public static String curYearMonth() {
        return format(curTime(), PATTERN_YM);
    }

    public static String curDay() {
        return format(curTime(), PATTERN_YMD);
    }

    public static String curTimeStr() {
        return format(curTime(), PATTERN_STANDARD);
    }

    /**
     * <p>get first date of source date week  </p>
     *
     * @param date source date
     * @return first date of week
     */
    public static Date getFirstDateOfWeek(Date date) {
        Calendar calendar = new GregorianCalendar();
        calendar.setFirstDayOfWeek(Calendar.MONDAY);
        calendar.setTime(date);
        calendar.set(Calendar.DAY_OF_WEEK, calendar.getFirstDayOfWeek());// Monday
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        return calendar.getTime();
    }

    /**
     * <p>get last date of source date week  </p>
     *
     * @param date source date
     * @return last date of week
     */
    public static Date getLastDateOfWeek(Date date) {
        Calendar calendar = new GregorianCalendar();
        calendar.setFirstDayOfWeek(Calendar.MONDAY);
        calendar.setTime(date);
        calendar.set(Calendar.DAY_OF_WEEK, calendar.getFirstDayOfWeek() + 6);// Sunday
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        return calendar.getTime();
    }

    /**
     * <p>get first date of source date month  </p>
     *
     * @param date source date
     * @return first date of month
     */
    public static Date getFirstDateOfMonth(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.DATE, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        return calendar.getTime();
    }

    /**
     * <p>get last date of source date month  </p>
     *
     * @param date source date
     * @return last date of month
     */
    public static Date getLastDateOfMonth(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.add(Calendar.MONTH, 1);
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        return calendar.getTime();
    }

    /**
     * <p>get  source date and after source date of source month day list   </p>
     *
     * @param date source date
     * @return last day string list of month
     */
    public List<String> getLastDayListOfMonth(Date date) {
        List<String> list = new ArrayList<String>();
        SimpleDateFormat df = new SimpleDateFormat(PATTERN_YMD);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int month = calendar.get(Calendar.MONTH);
        while (calendar.get(Calendar.MONTH) == month) {
            list.add(df.format(calendar.getTime()));
            calendar.add(Calendar.DATE, 1);
        }
        return list;
    }


    /**
     * <p>get  day nums of month  </p>
     *
     * @param year  source year
     * @param month source month
     * @return day nums of month
     */
    public static int getDaysOfMonth(int year, int month) {
        int days = 0;
        if (month == 2) {
            if ((year % 4 == 0 && year % 100 != 0) || year % 400 == 0) {
                days = 29;
            } else {
                days = 28;
            }
        } else if (month == 4 || month == 6 || month == 9 || month == 11) {
            days = 30;
        } else {
            days = 31;
        }
        return days;
    }

    /**
     * <p>get  day nums of month  </p>
     *
     * @param date source date
     * @return day nums of month
     */
    public static int getDaysOfMonth(Date date) {
        if (date == null) {
            return 0;
        }
        int year = getYear(date);
        int month = getMonth(date);
        return getDaysOfMonth(year, month);
    }

    public static Date getStartOfDay(Date date) {
        if (date == null) {
            return null;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, calendar.getMinimum(Calendar.HOUR_OF_DAY));
        calendar.set(Calendar.MINUTE, calendar.getMinimum(Calendar.MINUTE));
        calendar.set(Calendar.SECOND, calendar.getMinimum(Calendar.SECOND));
        calendar.set(Calendar.MILLISECOND, calendar.getMinimum(Calendar.MILLISECOND));
        return calendar.getTime();
    }

    public static Date getEndOfDay(Date date) {
        if (date == null) {
            return null;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, calendar.getMaximum(Calendar.HOUR_OF_DAY));
        calendar.set(Calendar.MINUTE, calendar.getMaximum(Calendar.MINUTE));
        calendar.set(Calendar.SECOND, calendar.getMaximum(Calendar.SECOND));
        calendar.set(Calendar.MILLISECOND, calendar.getMaximum(Calendar.MILLISECOND));
        return calendar.getTime();
    }

    /**
     * 获取初始化时间
     *
     * @return startDate
     */
    public static Date getStartDate() {
        try {
            return new SimpleDateFormat(PATTERN_STANDARD).parse("1900-01-01 00:00:00");
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }

    }

    /**
     * 获取最终化时间
     *
     * @return endDate
     */
    public static Date getEndDate() {
        try {
            return new SimpleDateFormat(PATTERN_STANDARD).parse("2199-01-01 00:00:00");
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }

    }
//3、日期增减-------------------------------------------------------------------------------------------------

    /**
     * 日期添加
     *
     * @param date      source date
     * @param dateField field
     * @param amount    add amount
     * @return result date
     */
    public static Date addDate(Date date, int dateField, int amount) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(dateField, amount);
        return calendar.getTime();
    }

    public static Date addSecond(Date date, int amount) {
        return addDate(date, Calendar.SECOND, amount);
    }

    public static Date addMinute(Date date, int amount) {
        return addDate(date, Calendar.MINUTE, amount);
    }

    public static Date addHour(Date date, int amount) {
        return addDate(date, Calendar.HOUR, amount);
    }

    public static Date addDay(Date date, int amount) {
        return addDate(date, Calendar.DAY_OF_MONTH, amount);
    }

    public static Date addWeek(Date date, int amount) {
        return addDate(date, Calendar.WEEK_OF_YEAR, amount);
    }

    public static Date addMonth(Date date, int amount) {
        return addDate(date, Calendar.MONTH, amount);
    }

    public static Date subDate(Date date, int dateField, int amount) {
        return addDate(date, dateField, -amount);
    }

    public static Date subSecond(Date date, int amount) {
        return subDate(date, Calendar.SECOND, amount);
    }

    public static Date subMinute(Date date, int amount) {
        return subDate(date, Calendar.MINUTE, amount);
    }

    public static Date subHour(Date date, int amount) {
        return subDate(date, Calendar.HOUR, amount);
    }

    public static Date subDay(Date date, int amount) {
        return subDate(date, Calendar.DAY_OF_MONTH, amount);
    }

    public static Date subWeek(Date date, int amount) {
        return subDate(date, Calendar.WEEK_OF_YEAR, amount);
    }

    public static Date subMonth(Date date, int amount) {
        return subDate(date, Calendar.MONTH, amount);
    }

    //4、日期计算-------------------------------------------------------------------------------------------------------
    public static long minuteDiff(Date beginTime, Date endTime) {
        return (endTime.getTime() - beginTime.getTime()) / 60000;
    }

    public static long hourDiff(Date beginTime, Date endTime) {
        return (endTime.getTime() - beginTime.getTime()) / 3600000;
    }

    public static long dayDiff(Date beginTime, Date endTime) {
        return (endTime.getTime() - beginTime.getTime()) / 86400000;
    }

    public static Integer monthDiff(Date beginTime, Date endTime) {
        return (getYear(endTime) - getYear(beginTime)) * 12 + getMonth(endTime) - getMonth(beginTime);
    }

    public static int yearDiff(Date beginTime, Date endTime) {
        return getYear(endTime) - getYear(beginTime);
    }

    /**
     * 通过时间表达式获取下次执行时间
     *
     * @param cron
     * @return
     */
    public static Date nextDate(String cron) {
        if (StringUtils.isBlank(cron)) {
            return null;
        }
        try {
            TriggerContext triggerContext = new SimpleTriggerContext();
            CronTrigger cronTrigger = new CronTrigger(cron);
            Date sendTime = cronTrigger.nextExecutionTime(triggerContext);
            return sendTime;
        } catch (Exception e) {
            log.error("nextDate,cron:{}; e:{}", cron, e);
            return null;
        }
    }
}
