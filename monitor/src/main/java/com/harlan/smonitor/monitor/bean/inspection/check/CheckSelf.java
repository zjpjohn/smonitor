package com.harlan.smonitor.monitor.bean.inspection.check;

import com.harlan.smonitor.monitor.bean.CheckItem;
import com.harlan.smonitor.monitor.core.job.inspection.CheckSelfServiceImpl;
import org.dom4j.Element;
import org.quartz.Job;


public class CheckSelf extends CheckItem {
	public CheckSelf(Element checkElement) {
		super(checkElement);
		
	}

	@Override
	public Class<? extends Job> getJobServiceImpl() {
		return CheckSelfServiceImpl.class;
	}

	@Override
	public void getAttrs(Element element) {

	}

	@Override
	public Element setAttrs(Element element) {
		return element;
	}

}
