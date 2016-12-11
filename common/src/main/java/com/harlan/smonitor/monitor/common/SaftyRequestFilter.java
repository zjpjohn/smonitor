package com.harlan.smonitor.monitor.common;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public class SaftyRequestFilter implements Filter {
	FilterConfig filterConfig = null;
	@Override
	public void destroy() {
		this.filterConfig = null;

	}
	@Override
	public void doFilter(ServletRequest squ, ServletResponse sqs, FilterChain fc)throws IOException, ServletException {
		fc.doFilter(new SafetyRequestWrapper((HttpServletRequest) squ), sqs);
	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		this.filterConfig = filterConfig;
	}

}
