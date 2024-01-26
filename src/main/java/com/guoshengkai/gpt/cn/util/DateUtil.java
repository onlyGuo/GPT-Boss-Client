package com.guoshengkai.gpt.cn.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtil {

    /**
     * 一天的时间(毫秒值)
     */
    public static final long DAY_TIME = 1000L * 60 * 60 * 24;

    /**
     * 昨天
     * @return
     */
    public static Date yesterday(){
        return parse(yesterdayTime().getTime(), "yyyy-MM-dd");
    }

    /**
     * 某个时间段的上一天
     * @param date
     * @return
     */
    public static Date yesterday(Date date){
        return parse(yesterdayTime(date).getTime(), "yyyy-MM-dd");
    }

    /**
     * 某个时间段的上一天的这个时间
     * @param date
     *      时间段
     * @return
     */
    public static Date yesterdayTime(Date date){
        return new Date(date.getTime() - DAY_TIME);
    }

    /**
     * 昨天的这个时候
     * @return
     */
    public static Date yesterdayTime(){
        return new Date(System.currentTimeMillis() - DAY_TIME);
    }

    /**
     * 获得本周第一天
     * @return
     */
    public static Date getWeekStartDate(){
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        return cal.getTime();
    }

    /**
     * 获得本月第一天
     * @return
     */
    public static Date getMonthStartDate(){
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MONTH, 0);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        return cal.getTime();
    }

    /**
     * 获得本年第一天
     */
    public static Date getYearStartDate(){
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.YEAR, 0);
        cal.set(Calendar.DAY_OF_YEAR, 1);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        return cal.getTime();
    }

    /**
     * 获得本周最后一天
     */
    public static Date getWeekEndDate(){
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        return cal.getTime();
    }

    /**
     * 获得本月最后一天
     */
    public static Date getMonthEndDate(){
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        return cal.getTime();
    }

    /**
     * 获得本年最后一天
     */
    public static Date getYearEndDate(){
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_YEAR, cal.getActualMaximum(Calendar.DAY_OF_YEAR));
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        return cal.getTime();
    }

    /**
     * 获得某一天的开始时间
     */
    public static Date getDayStartDate(Date date){
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        return cal.getTime();
    }

    /**
     * 获得某一天的结束时间
     */
    public static Date getDayEndDate(Date date){
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        return cal.getTime();
    }

    /**
     * 获取指定时间相对于格林威治时间中经历的所有天数
     * @param date
     * @return
     */
    public static int getAllDay(Date date){
        return (int) (date.getTime() / DAY_TIME);
    }

    /**
     * 获取当前时间是几号
     * @param date
     * @return
     */
    public static int getDay(Date date){
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(Calendar.DAY_OF_MONTH);
    }

    /**
     * 获取当前时间是几月份
     * @param date
     * @return
     */
    public static int getMonth(Date date){
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(Calendar.MONTH) + 1;
    }

    /**
     * 获取当前时间年份
     * @param date
     * @return
     */
    public static int getYear(Date date){
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(Calendar.YEAR);
    }

    /**
     * 普通格式化：yyyy-MM-dd
     * @return
     */
    public static String format(){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return simpleDateFormat.format(System.currentTimeMillis());
    }

    /**
     * 普通格式化：yyyy-MM-dd
     * @return
     */
    public static String format(long time){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return simpleDateFormat.format(time);
    }

    /**
     * 普通格式化：yyyy-MM-dd
     * @return
     */
    public static String format(Date date){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return simpleDateFormat.format(date);
    }

    /**
     * 带时分秒：yyyy-MM-dd HH:mm:ss
     * @return
     */
    public static String formatAll(){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return simpleDateFormat.format(System.currentTimeMillis());
    }

    /**
     * 带时分秒：yyyy-MM-dd HH:mm:ss
     * @return
     */
    public static String formatAll(long time){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return simpleDateFormat.format(time);
    }

    /**
     * 带时分秒：yyyy-MM-dd HH:mm:ss
     * @return
     */
    public static String formatAll(Date date){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return simpleDateFormat.format(date);
    }

    /**
     * 中文：yyyy年MM月dd日
     * @return
     */
    public static String formatCN(){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日");
        return simpleDateFormat.format(System.currentTimeMillis());
    }

    /**
     * 中文：yyyy年MM月dd日
     * @return
     */
    public static String formatCN(long time){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日");
        return simpleDateFormat.format(time);
    }

    /**
     * 中文：yyyy年MM月dd日
     * @return
     */
    public static String formatCN(Date date){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日");
        return simpleDateFormat.format(date);
    }

    /**
     * 中文带时分秒：yyyy年MM月dd日 HH时mm分ss秒
     * @return
     */
    public static String formatCNAll(){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH时mm分ss秒");
        return simpleDateFormat.format(System.currentTimeMillis());
    }

    /**
     * 中文带时分秒：yyyy年MM月dd日 HH时mm分ss秒
     * @return
     */
    public static String formatCNAll(long time){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH时mm分ss秒");
        return simpleDateFormat.format(time);
    }

    /**
     * 中文带时分秒：yyyy年MM月dd日 HH时mm分ss秒
     * @return
     */
    public static String formatCNAll(Date date){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH时mm分ss秒");
        return simpleDateFormat.format(date);
    }

    /**
     * 自定义
     * @param pra
     * @return
     */
    public static String formatPramm(String pra){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pra);
        return simpleDateFormat.format(System.currentTimeMillis());
    }

    /**
     * 自定义
     * @param pra
     * @return
     */
    public static String formatPramm(long time, String pra){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pra);
        return simpleDateFormat.format(new Date(time));
    }

    /**
     * 自定义
     * @param pra
     * @return
     */
    public static String formatPramm(Date time, String pra){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pra);
        return simpleDateFormat.format(time);
    }

    /**
     * 时间戳转格式化的标准时间
     * @param time
     *      时间戳
     * @param pra
     *      格式化表达式
     * @return
     */
    public static Date parse(long time, String pra){
        String s = formatPramm(time, pra);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pra);
        try {
            return simpleDateFormat.parse(s);
        } catch (ParseException e) {
            throw new IllegalArgumentException(e);
        }
    }

    /**
     * 日期时间字符串转日期对象
     * @param dateStr
     *      日期时间字符串
     * @param pra
     *      格式化表达式
     * @return
     */
    public static Date parse(String dateStr, String pra){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pra);
        try {
            return simpleDateFormat.parse(dateStr);
        } catch (ParseException e) {
            throw new IllegalArgumentException(e);
        }
    }

    public static Object addDay(Date date, int i) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, i);
        return calendar.getTime();
    }
}
