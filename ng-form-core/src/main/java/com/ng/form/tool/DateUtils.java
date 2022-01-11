
package com.ng.form.tool;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang.StringUtils;

/**
 * 日期处理
 *
 * @author lyf
 *
 * @date 2016年12月21日 下午12:53:33
 */
public class DateUtils {
	/** 时间格式(yyyy-MM-dd) */
	public final static String DATE_PATTERN = "yyyy-MM-dd";
	/** 时间格式(yyyy-MM-dd HH:mm:ss) */
	public final static String DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";

    /**
     * 日期格式化 日期格式为：yyyy-MM-dd
     * @param date  日期
     * @return  返回yyyy-MM-dd格式日期
     */
	public static String format(Date date) {
        return format(date, DATE_PATTERN);
    }

    /**
     * 日期格式化 日期格式为：yyyy-MM-dd
     * @param date  日期
     * @param pattern  格式，如：DateUtils.DATE_TIME_PATTERN
     * @return  返回yyyy-MM-dd格式日期
     */
    public static String format(Date date, String pattern) {
        if(date != null){
            SimpleDateFormat df = new SimpleDateFormat(pattern);
            return df.format(date);
        }
        return null;
    }



    public static String formatMineDate(Date date) {

        Calendar curr = Calendar.getInstance();

        int date_id = DateUtils.getDateId(date);
        int curr_id = DateUtils.getDateId(curr.getTime());
        //判断是否是今天
        if(date_id == curr_id) {
            return DateUtils.format(date , "HH:mm");
        }

        // 判断是否本周 或许当前周的周一
        curr.set(Calendar.DAY_OF_WEEK , 1);

        if(curr.getTime().before(date)) {
            // 本周
            String[] weeks = {"天","一","二","三","四","五","六"} ;
            curr.setTime(date);
            int dayOfWeek = curr.get(Calendar.DAY_OF_WEEK);
            return "星期" + weeks[dayOfWeek - 1] ;
        }

        return DateUtils.format(date , "yyyy-MM-dd HH:mm:ss");

    }
 

    public static Integer getDateId(Date date) {

    	Calendar calendar = Calendar.getInstance();
    	calendar.setTime(date);

    	int year = calendar.get(Calendar.YEAR) * 10000;
    	int month = (calendar.get(Calendar.MONTH) + 1) * 100 ;
    	int day =  calendar.get(Calendar.DAY_OF_MONTH);

    	if(month < 10) {
    		year = year * 10;
    	}


    	return year + month + day ;

    }

    /**
     * 字符串转换成日期
     * @param strDate 日期字符串
     * @param pattern 日期的格式，如：DateUtils.DATE_TIME_PATTERN
     */
    public static Date stringToDate(String strDate, String pattern) {
        if (StringUtils.isBlank(strDate)){
            return null;
        }

        SimpleDateFormat fmt = new SimpleDateFormat(pattern);
        try {
			return fmt.parse(strDate) ;
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return null;
    }

    
}
