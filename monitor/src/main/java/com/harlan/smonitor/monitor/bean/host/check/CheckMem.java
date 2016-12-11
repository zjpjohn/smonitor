package com.harlan.smonitor.monitor.bean.host.check;

import com.harlan.smonitor.monitor.bean.CheckItem;
import com.harlan.smonitor.monitor.core.job.host.CheckMemServiceImpl;
import org.dom4j.Element;
import org.quartz.Job;


public class CheckMem extends CheckItem {

	private Integer exceed;

	public CheckMem(Element checkElement) {
		super(checkElement);
	}

	@Override
	public Class<? extends Job> getJobServiceImpl() {
		return CheckMemServiceImpl.class;
	}

	@Override
	public void getAttrs(Element element) {
		this.exceed = Integer.valueOf(element.attributeValue("exceed"));
	}

	@Override
	public Element setAttrs(Element element) {
		element.addAttribute("exceed",exceed.toString());
		return element;
	}

	public Integer getExceed() {
		return exceed;
	}
}
