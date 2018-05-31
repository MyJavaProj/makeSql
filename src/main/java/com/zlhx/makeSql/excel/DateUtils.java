package com.zlhx.makeSql.excel;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

public class DateUtils
{
  public static int getField(int fieldType)
  {
    Calendar calendar = new GregorianCalendar();
    return calendar.get(fieldType);
  }
  
  public static Calendar construct(Integer year, Integer month, Integer day, Integer offset)
  {
    Calendar calendar = new GregorianCalendar();
    calendar.set(year.intValue(), month.intValue(), day.intValue(), 0, 0, 0);
    if (offset != null) {
      calendar.add(5, offset.intValue());
    }
    return calendar;
  }
  
  public static Date getNewDate(Integer year, Integer month, Integer day, String partten)
  {
    Calendar calendar = Calendar.getInstance();
    calendar.set(year.intValue(), month.intValue(), day.intValue(), 0, 0, 0);
    return parseDateString(formateDate(calendar.getTime(), partten), partten);
  }
  
  public static Date getDate(int plusOrSubtractDays, Date date, String partten)
  {
    Calendar cal = Calendar.getInstance();
    cal.setTime(date);
    cal.add(5, plusOrSubtractDays);
    return parseDateString(formateDate(cal.getTime(), partten), partten);
  }
  
  public static Date getDate(int type, int num, Date date, String partten)
  {
    Calendar cal = Calendar.getInstance();
    cal.setTime(date);
    cal.add(type, num);
    return parseDateString(formateDate(cal.getTime(), partten), partten);
  }
  
  public static Date addAndSubtractDate(int years, int month, int plusOrSubtractDays, int hours, int minute, int seconds, Date date, String partten)
  {
    Calendar cal = Calendar.getInstance();
    cal.setTime(date);
    cal.add(1, years);
    cal.add(2, month);
    cal.add(5, plusOrSubtractDays);
    cal.add(11, hours);
    cal.add(12, minute);
    cal.add(13, seconds);
    return parseDateString(formateDate(cal.getTime(), partten), partten);
  }
  
  public static String addAndSubDate(int years, int month, int plusOrSubtractDays, int hours, int minute, int seconds, Date date, String pattern)
  {
    Calendar cal = Calendar.getInstance();
    cal.setTime(date);
    cal.add(1, years);
    cal.add(2, month);
    cal.add(5, plusOrSubtractDays);
    cal.add(11, hours);
    cal.add(12, minute);
    cal.add(13, seconds);
    return formateDate(cal.getTime(), pattern);
  }
  
  public static String addAndSubtractDate(int plusOrSubtractDays, Date date)
  {
    Calendar cal = Calendar.getInstance();
    cal.setTime(date);
    cal.add(5, plusOrSubtractDays);
    return formateDate(cal.getTime(), "yyyy-MM-dd");
  }
  
  public static String addAndSubtractMonth(int plusOrSubtractMonth, Date date, String pattern)
  {
    Calendar cal = Calendar.getInstance();
    cal.setTime(date);
    cal.add(2, plusOrSubtractMonth);
    return formateDate(cal.getTime(), pattern);
  }
  
  public static int getMonthOfYear()
  {
    Calendar calendar = Calendar.getInstance();
    Date date = new Date();
    calendar.setTime(date);
    return calendar.get(2);
  }
  
  public static int getDayOfWeek()
  {
    Calendar calendar = Calendar.getInstance();
    Date date = new Date();
    calendar.setTime(date);
    return calendar.get(7) - 1;
  }
  
  public static Date getToday(String partten)
  {
    Date today = new Date();
    return parseDateString(formateDate(today, partten), partten);
  }
  
  public static String formateDate(Date date, String pattern)
  {
    return new SimpleDateFormat(pattern).format(date);
  }
  
  public static Date parseDateString(String dateStr, String partten)
  {
    SimpleDateFormat sdf = new SimpleDateFormat(partten);
    Date date = null;
    try
    {
      date = sdf.parse(dateStr);
    }
    catch (ParseException e)
    {
      throw new RuntimeException(e.getMessage());
    }
    return date;
  }
  
  public static long dateDiff(Date d1, Date d2)
  {
    long DAY = 86400000L;
    return (d1.getTime() - d2.getTime()) / 86400000L;
  }
  
  public static long dateDiff(Date d1)
  {
    long DAY = 86400000L;
    Date d2 = new Date();
    return (d1.getTime() - d2.getTime()) / 86400000L;
  }
  
  public static long diffSecond(Date inDate)
  {
    if (inDate == null) {
      return 0L;
    }
    long diff = new Date().getTime() - inDate.getTime();
    long diffSec = diff / 1000L;
    return diffSec;
  }
  
  public static long diffSecond(Date date1, Date date2)
  {
    if ((date1 == null) || (date2 == null)) {
      return 0L;
    }
    long diff = date2.getTime() - date1.getTime();
    long diffSec = diff / 1000L;
    return diffSec;
  }
  
  @Deprecated
  public static final Date string2Time(String dateString, String patten)
    throws ParseException
  {
    DateFormat dateFormat = new SimpleDateFormat(patten, Locale.CHINA);
    dateFormat.setLenient(false);
    Date timeDate = dateFormat.parse(dateString);
    Date dateTime = new Date(timeDate.getTime());
    return dateTime;
  }
  
  public static final Date stringToTime(String dateString, String patten)
  {
    DateFormat dateFormat = new SimpleDateFormat(patten, Locale.CHINA);
    dateFormat.setLenient(false);
    Date timeDate = null;
    try
    {
      timeDate = dateFormat.parse(dateString);
    }
    catch (ParseException e)
    {
      e.printStackTrace();
      throw new RuntimeException(e.getMessage());
    }
    Date dateTime = new Date(timeDate.getTime());
    return dateTime;
  }
  
  public static String getWeekOfDate(Date date)
  {
    String[] weekDaysName = { "周日", "周一", "周二", "周三", "周四", "周五", "周六" };
    
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(date);
    int intWeek = calendar.get(7) - 1;
    return weekDaysName[intWeek];
  }
  
  public static String getFirstDay()
  {
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    Calendar lastDate = Calendar.getInstance();
    lastDate.set(5, 1);
    return sdf.format(lastDate.getTime());
  }
}
