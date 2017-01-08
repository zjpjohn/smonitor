package com.harlan.smonitor.monitor.web.exception;

import com.harlan.smonitor.api.Result;
import com.harlan.smonitor.monitor.common.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;


@Component
public class SpringExceptionHandler implements HandlerExceptionResolver{
	private static Logger logger = LoggerFactory.getLogger(SpringExceptionHandler.class);

	@Override
	public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
		logger.error("spring 捕获异常，抛出异常的方法是"+handler);
		logger.error("异常信息",ex);
		try {
			if(request.getHeader("Accept")!=null && request.getHeader("Accept").indexOf("application/json") > -1){
			    /***
			     * json请求统一返回格式
			     *  {"ok":true,"reg":true}
			     *  {"ok":true,"send":true}
			     *  {"ok":false}
			     */
				PrintWriter out = response.getWriter();
				response.setContentType(Constants.JSON_PRODUCES);
				Result res = new Result("发生异常");
				out.write(res.toString());
				out.close();
			    return null; 
			}else{
				ModelAndView mv =new ModelAndView("err");
				mv.addObject("msg", "很抱歉..出现错误了");
			    return mv;
			}
		} catch (IOException e) {
			logger.error("(处理Response时又产生异常)",e);
		}
		return null;
	}
	
	

}
