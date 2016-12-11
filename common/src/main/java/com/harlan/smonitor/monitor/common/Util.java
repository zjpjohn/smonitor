package com.harlan.smonitor.monitor.common;

import org.apache.commons.lang3.time.FastDateFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.util.*;

public class Util {
	private final static FastDateFormat DATE_FRMAT_DEFAULT = FastDateFormat.getInstance("yyyyMMddHHmmss");
	private static Logger logger = LoggerFactory.getLogger(Util.class);
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

//	public static <V> Map<String,V> initServiceMapFromClassList(List<Class> class_list) {
//		Map<String,V> implMap=  new HashMap<String,V>();
//		for(Class implClass:class_list){
//			try {
//				String impl_type;
//
//				Field field=  implClass.getDeclaredField("TYPE");
//				impl_type= (String) field.get(null);
//				logger.debug("实现类 {} 的类型为 {}",implClass,impl_type);
//
//				implMap.put(impl_type,(V)implClass.newInstance());
//			} catch (Exception e) {
//				logger.error("通过实例化服务类异常：",e);
//				throw new RuntimeException("实现类"+implClass+"需要有静态的TYPE属性,并可实例化");
//			}
//		}
//		return implMap;
//	}

}
