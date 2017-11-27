package com.lnjecit.generator.util;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

/*******
 * 支持所有时间转换的类
 *
 * @author Administrator
 *
 */
public class DateUtil {

	/** 年-月-日 时:分:秒 显示格式 */
	// 备注:如果使用大写HH标识使用24小时显示格式,如果使用小写hh就表示使用12小时制格式。
	public static String DATE_TO_STRING_DETAIAL_PATTERN = "yyyy-MM-dd HH:mm:ss";

	/** 年-月-日 显示格式 */
	public static String DATE_TO_STRING_SHORT_PATTERN = "yyyy-MM-dd";

	/**
	 * 年月日_时 显示格式
	 */
	public static String DATE_TO_STRING_PATTERN = "yyyyMMdd_HH";

	public static SimpleDateFormat simpleDateFormat;

	/**
	 * Date类型转为指定格式的String类型
	 *
	 * @param source
	 * @param pattern
	 * @return
	 */
	public static String DateToString(Date source, String pattern) {
		simpleDateFormat = new SimpleDateFormat(pattern);
		return simpleDateFormat.format(source);
	}

	/**
	 * 将日期转换为时间戳(unix时间戳,单位秒)
	 *
	 * @param date
	 * @return
	 */
	public static long dateToTimeStamp(Date date) {
		Timestamp timestamp = new Timestamp(date.getTime());
		return timestamp.getTime() / 1000;
	}

	/**
	 * 获得当前unix时间戳(单位秒)
	 * @return 当前unix时间戳
	 */
	public static long currentTimeStamp() {
		return System.currentTimeMillis() / 1000;
	}

}
