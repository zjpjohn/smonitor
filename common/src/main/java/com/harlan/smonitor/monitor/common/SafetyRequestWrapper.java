package com.harlan.smonitor.monitor.common;

import org.apache.commons.lang3.StringEscapeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

/***
 * 用于统一处理Request中的特殊字符
 * @author zhaohl
 *
 */
public class SafetyRequestWrapper extends HttpServletRequestWrapper {

	public static Logger logger = LoggerFactory.getLogger(SafetyRequestWrapper.class);

	public SafetyRequestWrapper(HttpServletRequest request) {
		super(request);
	}

	public String[] getParameterValues(String parameter) {
		String[] values = super.getParameterValues(parameter);
		if (values == null) {
			return null;
		}
		int count = values.length;
		String[] encoded_values = new String[count];
		for (int i = 0; i < count; i++) {
			encoded_values[i] = cleanXSS(values[i]);
		}
		return encoded_values;
	}

	public String getParameter(String parameter) {
		String value = super.getParameter(parameter);
		if (value == null) {
			return null;
		}
		logger.debug("param:{}，value:{}",parameter,value);
		return cleanXSS(value);
	}

	public String getHeader(String name) {
		String value = super.getHeader(name);
		if (value == null)
			return null;
		return cleanXSS(value);
	}
	private String cleanXSS(String value) {
		return StringEscapeUtils.escapeHtml4(value);
    }
}
