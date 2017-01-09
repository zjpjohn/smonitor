package com.harlan.smonitor.monitor.common;

import org.apache.commons.lang3.time.FastDateFormat;

import java.util.*;

public class Util {
	private final static FastDateFormat DATE_FRMAT_DEFAULT = FastDateFormat.getInstance("yyyyMMddHHmmss");
	/**
	 * 将字符串时间转化为date
	 * @param date_string 格式为yyyyMMddHHmmss
	 * @return 日期
	 * @throws Exception
	 */
	public static Date string2Date(String date_string) throws Exception{
		if(!isNull(date_string)){
			return DATE_FRMAT_DEFAULT.parse(date_string);
		}else{
			return null;
		}
	}
	/**
	 * 将date转化为时间字符串
	 * @param date 日期 默认时间格式yyyyMMddHHmmss
	 * @return 字符串
	 */
	public static String date2String(Date date){
		if(date != null){
			return DATE_FRMAT_DEFAULT.format(date);
		}else{
			return null;
		}
	}
	/**
	 * 将date转化为时间字符串
	 * @param date 日期
	 * @param pattern 匹配的字符串
	 * @return 字符串
	 */
	public static String date2String(Date date,String pattern){
		if(date != null){
			return FastDateFormat.getInstance(pattern).format(date);
		}else{
			return null;
		}
	}
	
	public static boolean isNull(String str) {
		return str == null || str.length() <= 0;
}

	/**
	 * 注意：此方法只是用于字符串类型的Obejct的判断空否
	 * @param strObj 字符串类型的object
	 * @return
	 */
	public static boolean notNull(Object strObj) {
		return !isNull(strObj);
	}

	/**
	 * 判断是否空，只是用于String类型
	 * @param stringObj String类型的变量
	 * @return
	 */
	public static boolean isNull(Object stringObj) {
		if(stringObj instanceof String){
			return isNull((String)stringObj);
		}else{
			return stringObj==null;
		}

	}
}
