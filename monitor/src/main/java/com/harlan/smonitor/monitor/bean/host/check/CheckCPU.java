package com.harlan.smonitor.monitor.bean.host.check;

import com.harlan.smonitor.monitor.bean.CheckItem;
import com.harlan.smonitor.monitor.common.Util;
import com.harlan.smonitor.monitor.core.job.host.CheckCPUServiceImpl;
import org.dom4j.Element;
import org.quartz.Job;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class CheckCPU extends CheckItem {
	private final static Logger logger = LoggerFactory.getLogger("detail");
	
	private Double exceed;

	public CheckCPU(Element checkElement) {
		super(checkElement);
		try {
			String str = checkElement.attributeValue("exceed");
			this.exceed = Double.valueOf(str); 
		} catch (Exception e) {
			logger.error("初始化{}对象出现异常:{}",this.getClass().getName(),e.getMessage());
		}
	}

	@Override
	public Class<? extends Job> getJobServiceImpl() {
		return CheckCPUServiceImpl.class;
	}

	@Override
	public void getAttrs(Element element) {
		if(!Util.isNull(element.attributeValue("exceed"))){
			this.exceed=Double.valueOf(element.attributeValue("exceed"));
		}
	}

	@Override
	public Element setAttrs(Element element) {
		element.addAttribute("exceed",exceed.toString());
		return element;
	}

	public Double getExceed() {
		return exceed;
	}
}
