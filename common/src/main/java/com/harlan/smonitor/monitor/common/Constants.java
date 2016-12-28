package com.harlan.smonitor.monitor.common;

import java.io.File;

public class Constants {

	/**
	 * 程序中的编码，统一utf8
	 */
	public static final String CHARSET = "UTF-8";

	/**
	 * spring mvc 程序中的json返回格式
	 */
	public static final String JSON_PRODUCES = "application/json;charset=utf-8";

	/**
	 * checkItem的执行状态，0是执行，1是暂停
	 */
	public static final int CHECK_RUN = 0;
	public static final int CHECK_PAUSE = 1;


	public static final String ECHN_TOOL_HOME = System.getenv("ECHN_TOOL_HOME") + File.separator;
	public static final String SOURCE_DATABASED_PWD_FILE_NAME = ".dbpwdconfig";
	public static final String SOURCE_HOST_PWD_FILE_NAME = ".hostpwdconfig";
	 static{
			if(ECHN_TOOL_HOME == null || "".equals(ECHN_TOOL_HOME)){
				throw new RuntimeException("请配置存放数据库和主机密码配置文件的目录:ECHN_TOOL_HOME环境变量");
			}
		}
}
