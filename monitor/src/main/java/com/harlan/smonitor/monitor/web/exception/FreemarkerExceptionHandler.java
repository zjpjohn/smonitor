package com.harlan.smonitor.monitor.web.exception;

import freemarker.core.Environment;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Writer;

/**
 * 用来统一处理freemarker解析异常的工具类
 * 用于配置在配置文件中
 * @author zhaohl
 *
 */
public class FreemarkerExceptionHandler implements TemplateExceptionHandler {
	private static Logger logger = LoggerFactory.getLogger(FreemarkerExceptionHandler.class);

	@Override
	public void handleTemplateException(TemplateException ex, Environment en, Writer wr)	throws TemplateException {
		logger.error("freemarker 异常",ex);
	}

}
